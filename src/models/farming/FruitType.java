package models.farming;

import models.enums.Seasons;

public enum FruitType {
    APRICOT(59, true, 38, Seasons.SPRING),
    CHERRY(80, true, 38, Seasons.SPRING),
    BANANA(150, true, 75, Seasons.SUMMER),
    MANGO(130, true, 100, Seasons.SUMMER),
    ORANGE(100, true, 38, Seasons.SUMMER),
    PEACH(140, true, 38, Seasons.SUMMER),
    APPLE(100, true, 38, Seasons.FALL),
    POMEGRANATE(140, true, 38, Seasons.FALL),
    OAK_RESIN(150, false, 0, Seasons.SPECIAL),
    MAPLE_SYRUP(200, false, 0, Seasons.SPECIAL),
    PINE_TAR(100, false, 0, Seasons.SPECIAL),
    SAP(2, true, -2, Seasons.SPECIAL),
    COMMON_MUSHROOM(40, true, 38, Seasons.SPECIAL),
    MYSTIC_SYRUP(1000, true, 500, Seasons.SPECIAL);

    private final int basePrice;
    private final boolean isEdible;
    private final int energy;
    private final Seasons season;

    FruitType(int basePrice, boolean isEdible, int energy, Seasons season) {
        this.basePrice = basePrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.season = season;
    }

    public int getBasePrice() { return basePrice; }
    public boolean isEdible() { return isEdible; }
    public int getEnergy() { return energy; }
    public Seasons getSeason() { return season; }
}
