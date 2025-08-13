package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.ItemType;

public enum SyrupType implements ItemType {
//    MAPLE_SYRUP("MAPLE_SYRUP", "items/syrup/maple_syrup.png", 0, 120),
//    OAK_RESIN  ("OAK_RESIN",   "items/syrup/oak_resin.png",   0, 90),
//    PINE_TAR   ("PINE_TAR",    "items/syrup/pine_tar.png",    0, 80);
    ;

    private final String id, icon;
    private final int maxStack, sellPrice;
    SyrupType(String id, String icon, int maxStack, int sellPrice) {
        this.id = id; this.icon = icon; this.maxStack = maxStack; this.sellPrice = sellPrice;
    }
    @Override public String id() { return id; }
    @Override public String iconPath() { return icon; }
    @Override public int maxStack() { return maxStack; }
    public int sellPrice() { return sellPrice; }
}
