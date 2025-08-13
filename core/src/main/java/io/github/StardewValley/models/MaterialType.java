package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Types.MaterialTypes;

public enum MaterialType implements MaterialTypes, ItemType {
    Wood("Wood", "wood.png", 0),
    Stone("Stone", "stone.png", 0),
    Coal("Coal", "coal.png", 0),
    CopperOre("Copper Ore", "copper_ore.png", 0),
    IronOre("Iron Ore", "iron_ore.png", 0),
    IronBar("Iron Bar", "iron_bar.png", 0),
    GoldBar("Gold Ore", "items/Gold_Bar.png", 0),
    ;

    private final String name;
    private final String iconPath;
    private final int maxStack;

    MaterialType(String name, String iconPath, int maxStack) {
        this.name = name;
        this.iconPath = iconPath;
        this.maxStack = maxStack;
    }

    // سازنده‌های سازگار با کد قدیمی
    MaterialType(String name) { this(name, "materials/" + name.toLowerCase().replace(' ', '_') + ".png", 999); }

    @Override public boolean isTool() { return false; }
    @Override public String getName() { return this.name; }

    // === ItemType ===
    @Override public String id() { return name; }
    @Override public String iconPath() { return iconPath; }
    @Override public int maxStack() { return maxStack; }
}
