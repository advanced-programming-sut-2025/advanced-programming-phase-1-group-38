package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.ItemType;

public enum FruitType implements ItemType {
    APPLE("APPLE", "trees/apple.png", 0, 80);
//    ORANGE("ORANGE", "trees/fruit/orange.png", 0, 70);

    private final String id, icon;
    private final int maxStack, sellPrice;
    FruitType(String id, String icon, int maxStack, int sellPrice) {
        this.id = id; this.icon = icon; this.maxStack = maxStack; this.sellPrice = sellPrice;
    }
    @Override public String id() { return id; }
    @Override public String iconPath() { return icon; }
    @Override public int maxStack() { return maxStack; }
    public int sellPrice() { return sellPrice; }
}
