package com.turqmelon.LevelUp;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class LevelUp extends JavaPlugin {

    private static LevelUp instance;
    private static Permission permission;
    private static Economy economy;

    private static List<Promotion> promotionList = new ArrayList<>();

    @Override
    public void onEnable() {

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()){
            saveDefaultConfig();
        }

        instance = this;
        if (!setupPermissions()){
            getLogger().log(Level.WARNING, "Failed to load permissions.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupEconomy()){
            getLogger().log(Level.WARNING, "Failed to load economy.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ConfigurationSection section = getConfig().getConfigurationSection("promotions");
        if (section != null){
            Set<String> ranks = section.getKeys(false);
            if (ranks != null && !ranks.isEmpty()){
                for(String rank : ranks){
                    String path = "promotions." + rank;
                    String next = getConfig().getString(path + ".nextrank");
                    int price = getConfig().getInt(path + ".price");
                    getPromotionList().add(new Promotion(rank, next, price));
                }
            }
        }
        getLogger().log(Level.INFO, "Loaded " + getPromotionList().size() + " promotion(s).");

        getCommand("levelup").setExecutor(new LevelUpCommand());

    }

    public static Promotion getPromotion(String name){
        for(Promotion promotion : getPromotionList()){
            if (promotion.getRank().equalsIgnoreCase(name)){
                return promotion;
            }
        }
        return null;
    }

    public static LevelUp getInstance() {
        return instance;
    }

    private boolean setupEconomy() {

        RegisteredServiceProvider<Economy> econProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (econProvider != null){
            economy = econProvider.getProvider();
        }

        return (economy != null);
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public static Permission getPermission() {
        return permission;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static List<Promotion> getPromotionList() {
        return promotionList;
    }
}
