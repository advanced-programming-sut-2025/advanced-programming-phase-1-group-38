package models.enums.Types;

import models.enums.Seasons;

public enum FishType {
    SALMON("Salmon", 75, Seasons.FALL, false),
    SARDINE ("Sardine", 40, Seasons.FALL, false),
    SHAD ("Shad", 60, Seasons.FALL, false),
    BLUE_DISCUS ("Blue Discus", 120, Seasons.FALL, false),
    MIDNIGHT_CARP ("Midnight Carp", 150, Seasons.WINTER, false),
    SQUID ("Squid", 80, Seasons.WINTER, false),
    TUNA ("Tuna", 100, Seasons.WINTER, false),
    PERCH ("Perch", 55, Seasons.WINTER, false),
    FLOUNDER ("Flounder", 100, Seasons.SPRING, false),
    LIONFISH ("Lionfish", 100, Seasons.SPRING, false),
    HERRING ("Herring", 30, Seasons.SPRING, false),
    GHOSTFISH ("Ghostfish", 45, Seasons.SPRING, false),
    TILAPIA ("Tilapia", 75, Seasons.SUMMER, false),
    DORADO ("Dorado", 100, Seasons.SUMMER, false),
    SUNFISH ("Sunfish", 30, Seasons.SUMMER, false),
    RAINBOW_TROUT ("Rainbow Trout", 65, Seasons.SUMMER, false),
    LEGEND ("Legend", 5000, Seasons.SPRING, true),
    GLACIERFISH ("Glacierfish", 1000, Seasons.WINTER, true),
    ANGLER ("Angler", 900, Seasons.FALL, true),
    CRIMSONFISH ("Crimsonfish", 1500, Seasons.SUMMER, true);

    private final String name;
    private final int basePrice;
    private final Seasons season;
    private final boolean isLegendary;

    FishType(String name, int basePrice, Seasons season, boolean isLegendary) {
        this.name = name;
        this.basePrice = basePrice;
        this.season = season;
        this.isLegendary = isLegendary;
    }

    public String getName() {
        return name;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Seasons getSeason() {
        return season;
    }

    public boolean isLegendary() {
        return isLegendary;
    }
}
