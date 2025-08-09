package io.github.StardewValley.models.farming;

public enum SyrupType implements TreeProductType {
    OAK_RESIN("Oak Resin", 150, false, 0),
    MAPLE_SYRUP("Maple Syrup", 200, false, 0),
    PINE_TAR("Pine Tar", 100, false, 0),
    SAP("Sap", 2, false, 0),
    MYSTIC_SYRUP("Mystic Syrup", 1000, true, 500);

    private final String name;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;

    SyrupType(String name, int baseSellPrice, boolean isEdible, int energy) {
        this.name = name;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
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
