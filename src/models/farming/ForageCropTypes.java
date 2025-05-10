package models.farming;

import models.enums.Seasons;

public enum ForageCropTypes {
    COMMON_MUSHROOM("Common Mushroom", Seasons.SPECIAL, 40, 38),
    DAFFODIL("Daffodil", Seasons.SPRING, 30, 0),
    DANDELION("Dandelion", Seasons.SPRING, 40, 25),
    LEEK("Leek", Seasons.SPRING, 60, 40),
    MOREL("Morel", Seasons.SPRING, 150, 20),
    SALMONBERRY("Salmonberry", Seasons.SPRING, 5, 25),
    SPRING_ONION("Spring Onion", Seasons.SPRING, 8, 13),
    WILD_HORSERADISH("Wild Horseradish", Seasons.SPRING, 50, 13),
    FIDDLEHEAD_FERN("Fiddlehead Fern", Seasons.SUMMER, 90, 25),
    GRAPE("Grape", Seasons.SUMMER, 80, 38),
    RED_MUSHROOM("Red Mushroom", Seasons.SUMMER, 75, -50),
    SPICE_BERRY("Spice Berry", Seasons.SUMMER, 80, 25),
    SWEET_PEA("Sweet Pea", Seasons.SUMMER, 50, 0),
    BLACKBERRY("Blackberry", Seasons.FALL, 25, 25),
    CHANTERELLE("Chanterelle", Seasons.FALL, 160, 75),
    HAZELNUT("Hazelnut", Seasons.FALL, 40, 38),
    PURPLE_MUSHROOM("Purple Mushroom", Seasons.FALL, 90, 30),
    WILD_PLUM("Wild Plum", Seasons.FALL, 80, 25),
    CROCUS("Crocus", Seasons.WINTER, 60, 0),
    CRYSTAL_FRUIT("Crystal Fruit", Seasons.WINTER, 150, 63),
    HOLLY("Holly", Seasons.WINTER, 80, -37),
    SNOW_YAM("Snow Yam", Seasons.WINTER, 100, 30),
    WINTER_ROOT("Winter Root", Seasons.WINTER, 70, 25);

    private final String name;
    private final Seasons season;
    private final int price;
    private final int energy;

    ForageCropTypes(String name, Seasons season, int price, int energy) {
        this.name = name;
        this.season = season;
        this.price = price;
        this.energy = energy;
    }

    public String getName() { return name; }
    public Seasons getSeason() { return season; }
    public int getPrice() { return price; }
    public int getEnergy() { return energy; }
}
