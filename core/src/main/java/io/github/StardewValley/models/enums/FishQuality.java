package io.github.StardewValley.models.enums;

public enum FishQuality {
    NORMAL(1.0),
    SILVER(1.25),
    GOLD(1.5),
    IRIDIUM(2.0);

    private final double priceMultiplier;

    FishQuality(double multiplier) {
        this.priceMultiplier = multiplier;
    }

    public double getMultiplier() {
        return priceMultiplier;
    }

    public static FishQuality fromScore(double score) {
        if (score <= 0.5) return NORMAL;
        if (score <= 0.7) return SILVER;
        if (score <= 0.9) return GOLD;
        return IRIDIUM;
    }
}
