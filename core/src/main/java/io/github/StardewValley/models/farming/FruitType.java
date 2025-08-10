package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.enums.Types.MaterialTypes;

public enum FruitType implements TreeProductType, MaterialTypes {
    APRICOT("Apricot", 59, true, 38),
    CHERRY("Cherry", 80, true, 38),
    BANANA("Banana", 150, true, 75),
    MANGO("Mango", 130, true, 100),
    ORANGE("Orange", 100, true, 38),
    PEACH("Peach", 140, true, 38),
    APPLE("Apple", 100, true, 38),
    POMEGRANATE("Pomegranate", 140, true, 38),
    COMMON_MUSHROOM("Common Mushroom", 40, true, 38);

    private final String name;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;

    FruitType(String name, int baseSellPrice, boolean isEdible, int energy) {
        this.name = name;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() { return name; }
    @Override
    public int getBaseSellPrice() { return baseSellPrice; }
    @Override
    public boolean isEdible() { return isEdible; }
    @Override
    public int getEnergy() { return energy; }

    @Override
    public String toString() {
        return name;
    }
}
