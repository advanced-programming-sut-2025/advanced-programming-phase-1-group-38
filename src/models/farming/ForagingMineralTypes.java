package models.farming;

public enum ForagingMineralTypes {
    QUARTZ("Quartz", 25),
    EARTH_CRYSTAL("Earth Crystal", 50),
    FROZEN_TEAR("Frozen Tear", 75),
    FIRE_QUARTZ("Fire Quartz", 100),
    EMERALD("Emerald", 250),
    AQUAMARINE("Aquamarine", 180),
    RUBY("Ruby", 250),
    AMETHYST("Amethyst", 100),
    TOPAZ("Topaz", 80),
    JADE("Jade", 200),
    DIAMOND("Diamond", 750),
    PRISMATIC_SHARD("Prismatic Shard", 2000),
    COPPER("Copper", 5),
    IRON("Iron", 10),
    GOLD("Gold", 25),
    IRIDIUM("Iridium", 100),
    COAL("Coal", 15);

    private final String displayName;
    private final int sellPrice;

    ForagingMineralTypes(String displayName, int sellPrice) {
        this.displayName = displayName;
        this.sellPrice = sellPrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
