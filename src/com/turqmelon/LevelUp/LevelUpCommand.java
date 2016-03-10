package com.turqmelon.LevelUp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;


public class LevelUpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if ((sender instanceof Player)){
            Player player = (Player)sender;

            if (player.hasPermission("levelup.use")){

                if (LevelUp.getPermission().hasGroupSupport()){

                    Promotion promotion = null;
                    for(String group : LevelUp.getPermission().getPlayerGroups(player)){
                        promotion = LevelUp.getPromotion(group);
                        if (promotion != null)break;
                    }

                    if (promotion != null){

                        int price = promotion.getCost();
                        double balance = LevelUp.getEconomy().getBalance(player);
                        if (balance >= price){

                            LevelUp.getEconomy().withdrawPlayer(player, price);
                            LevelUp.getPermission().playerRemoveGroup(player, promotion.getRank());
                            LevelUp.getPermission().playerAddGroup(player, promotion.getNextRank());

                            sender.sendMessage("§aYou've leveled up to §f" + promotion.getNextRank() + "§a for §f$" + promotion.getCost() + "§a!");
                            LevelUp.getInstance().getLogger().log(Level.INFO, player.getName() + " leveled up to "  + promotion.getNextRank() + " for $" + promotion.getCost() + ".");
                        }
                        else{
                            sender.sendMessage("§cYou can't afford to level up to §f" + promotion.getNextRank() + "§c!");
                            sender.sendMessage("§cThat would cost §f$" + price + "§c.");
                        }

                    }
                    else{
                        sender.sendMessage("§cThere's nothing to level up to!");
                    }

                }
                else{
                    sender.sendMessage("§cCurrent permission plugin doesn't offer group support.");
                }

            }
            else{
                sender.sendMessage("§cYou don't have permission.");
            }

        }
        else{
            sender.sendMessage("§cYou need to be a player.");
        }

        return true;
    }
}
