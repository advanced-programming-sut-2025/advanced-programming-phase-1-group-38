package io.github.StardewValley.models;

public final class GameEconomy {
    private int gold;

    public GameEconomy(int startingGold) { this.gold = startingGold; }

    public int getGold() { return gold; }
    public void addGold(int amount) { gold = Math.max(0, gold + amount); }
    public boolean spendGold(int amount) {
        if (amount <= gold) { gold -= amount; return true; }
        return false;
    }
}
