package com.turqmelon.LevelUp;


public class Promotion {

    private String rank;
    private String nextRank;
    private int cost;

    public Promotion(String rank, String nextRank, int cost) {
        this.rank = rank;
        this.nextRank = nextRank;
        this.cost = cost;
    }

    public String getRank() {
        return rank;
    }

    public String getNextRank() {
        return nextRank;
    }

    public int getCost() {
        return cost;
    }
}
