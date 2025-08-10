package io.github.StardewValley.models;

public final class GameEconomy {
    private static int gold = 250; // start money; tune as you like

    private GameEconomy() {}
    public static int getGold() { return gold; }
    public static void addGold(int amount) { gold = Math.max(0, gold + amount); }
    public static boolean spendGold(int amount) {
        if (amount <= gold) { gold -= amount; return true; }
        return false;
    }
}
