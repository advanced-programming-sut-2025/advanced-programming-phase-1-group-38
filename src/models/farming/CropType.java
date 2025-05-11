package models.farming;

import models.enums.Seasons;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CropType {
    // REGULAR CROPS
    BLUE_JAZZ("Blue Jazz", "Jazz Seeds", "1-2-2-2", true, 0, 50, true, 45, List.of(Seasons.SPRING), false, false),
    CARROT("Carrot", "Carrot Seeds", "1-1-1", true, 0, 35, true, 75, List.of(Seasons.SPRING), false, false),
    CAULIFLOWER("Cauliflower", "Cauliflower Seeds", "1-2-4-4-1", true, 0, 175, true, 75, List.of(Seasons.SPRING), true, false),
    COFFEE_BEAN("Coffee Bean", "Coffee Bean", "1-2-2-3-2", false, 2, 15, false, 0, List.of(Seasons.SPRING, Seasons.SUMMER), false, false),
    GARLIC("Garlic", "Garlic Seeds", "1-1-1-1", true, 0, 60, true, 20, List.of(Seasons.SPRING), false, false),
    GREEN_BEAN("Green Bean", "Bean Starter", "1-1-1-3-4", false, 3, 40, true, 25, List.of(Seasons.SPRING), false, false),
    KALE("Kale", "Kale Seeds", "1-2-2-1", true, 0, 110, true, 50, List.of(Seasons.SPRING), false, false),
    PARSNIP("Parsnip", "Parsnip Seeds", "1-1-1-1", true, 0, 35, true, 25, List.of(Seasons.SPRING), false, false),
    POTATO("Potato", "Potato Seeds", "1-1-1-2-1", true, 0, 80, true, 25, List.of(Seasons.SPRING), false, false),
    RHUBARB("Rhubarb", "Rhubarb Seeds", "2-2-2-3-4", true, 0, 220, false, 0, List.of(Seasons.SPRING), false, false),
    STRAWBERRY("Strawberry", "Strawberry Seeds", "1-1-2-2-2", false, 4, 120, true, 50, List.of(Seasons.SPRING), false, false),
    TULIP("Tulip", "Tulip Bulb", "1-1-2-2", true, 0, 30, true, 45, List.of(Seasons.SPRING), false, false),
    UNMILLED_RICE("Unmilled Rice", "Rice Shoot", "1-2-2-3", true, 0, 30, true, 3, List.of(Seasons.SPRING), false, false),
    BLUEBERRY("Blueberry", "Blueberry Seeds", "1-3-3-4-2", false, 4, 50, true, 25, List.of(Seasons.SUMMER), false, false),
    CORN("Corn", "Corn Seeds", "2-3-3-3-3", false, 4, 50, true, 25, List.of(Seasons.SUMMER, Seasons.FALL), false, false),
    HOPS("Hops", "Hops Starter", "1-1-2-3-4", false, 1, 25, true, 45, List.of(Seasons.SUMMER), false, false),
    HOT_PEPPER("Hot Pepper", "Pepper Seeds", "1-1-1-1-1", false, 3, 40, true, 13, List.of(Seasons.SUMMER), false, false),
    MELON("Melon", "Melon Seeds", "1-2-3-3-3", true, 0, 250, true, 113, List.of(Seasons.SUMMER), true, false),
    POPPY("Poppy", "Poppy Seeds", "1-2-2-2", true, 0, 140, true, 45, List.of(Seasons.SUMMER), false, false),
    RADISH("Radish", "Radish Seeds", "2-1-2-1", true, 0, 90, true, 45, List.of(Seasons.SUMMER), false, false),
    RED_CABBAGE("Red Cabbage", "Red Cabbage Seeds", "2-1-2-2-2", true, 0, 260, true, 75, List.of(Seasons.SUMMER), false, false),
    STARFRUIT("Starfruit", "Starfruit Seeds", "2-3-2-3-3", true, 0, 750, true, 125, List.of(Seasons.SUMMER), false, false),
    SUMMER_SPANGLE("Summer Spangle", "Spangle Seeds", "1-2-3-1", true, 0, 90, true, 45, List.of(Seasons.SUMMER), false, false),
    SUMMER_SQUASH("Summer Squash", "Summer Squash Seeds", "1-1-1-2-1", false, 3, 45, true, 63, List.of(Seasons.SUMMER), false, false),
    SUNFLOWER("Sunflower", "Sunflower Seeds", "1-2-3-2", true, 0, 80, true, 45, List.of(Seasons.SUMMER, Seasons.FALL), false, false),
    TOMATO("Tomato", "Tomato Seeds", "2-2-2-2-3", false, 4, 60, true, 20, List.of(Seasons.SUMMER), false, false),
    WHEAT("Wheat", "Wheat Seeds", "1-1-1-1", true, 0, 25, false, 0, List.of(Seasons.SUMMER, Seasons.FALL), false, false),
    AMARANTH("Amaranth", "Amaranth Seeds", "1-2-2-2", true, 0, 150, true, 50, List.of(Seasons.FALL), false, false),
    ARTICHOKE("Artichoke", "Artichoke Seeds", "2-2-1-2-1", true, 0, 160, true, 30, List.of(Seasons.FALL), false, false),
    BEET("Beet", "Beet Seeds", "1-1-2-2", true, 0, 100, true, 30, List.of(Seasons.FALL), false, false),
    BOK_CHOY("Bok Choy", "Bok Choy Seeds", "1-1-1-1", true, 0, 80, true, 25, List.of(Seasons.FALL), false, false),
    BROCCOLI("Broccoli", "Broccoli Seeds", "2-2-2-2", false, 4, 70, true, 63, List.of(Seasons.FALL), false, false),
    CRANBERRIES("Cranberries", "Cranberry Seeds", "1-2-1-1-2", false, 5, 75, true, 38, List.of(Seasons.FALL), false, false),
    EGGPLANT("Eggplant", "Eggplant Seeds", "1-1-1-1", false, 5, 60, true, 20, List.of(Seasons.FALL), false, false),
    FAIRY_ROSE("Fairy Rose", "Fairy Seeds", "1-4-4-3", true, 0, 290, true, 45, List.of(Seasons.FALL), false, false),
    GRAPE("Grape", "Grape Starter", "1-1-2-3-3", false, 3, 80, true, 38, List.of(Seasons.FALL), false, false),
    PUMPKIN("Pumpkin", "Pumpkin Seeds", "1-2-3-4-3", true, 0, 320, false, 0, List.of(Seasons.FALL), true, false),
    YAM("Yam", "Yam Seeds", "1-3-3-3", true, 0, 160, true, 45, List.of(Seasons.FALL), false, false),
    SWEET_GEM_BERRY("Sweet Gem Berry", "Rare Seed", "2-4-6-6-6", true, 0, 3000, false, 0, List.of(Seasons.FALL), false, false),
    POWDERMELON("Powdermelon", "Powdermelon Seeds", "1-2-1-2-1", true, 0, 60, true, 63, List.of(Seasons.WINTER), true, false),
    ANCIENT_FRUIT("Ancient Fruit", "Ancient Seeds", "2-7-7-7-5", false, 7, 550, false, 0, List.of(Seasons.SPRING, Seasons.SUMMER, Seasons.FALL), false, false),

    // FORAGE CROPS
    COMMON_MUSHROOM("Common Mushroom", "", "", true, 0, 40, true, 38, List.of(Seasons.SPECIAL), false, true),
    DAFFODIL("Daffodil", "", "", true, 0, 30, true, 0, List.of(Seasons.SPRING), false, true),
    DANDELION("Dandelion", "", "", true, 0, 40, true, 25, List.of(Seasons.SPRING), false, true),
    LEEK("Leek", "", "", true, 0, 60, true, 40, List.of(Seasons.SPRING), false, true),
    MOREL("Morel", "", "", true, 0, 150, true, 20, List.of(Seasons.SPRING), false, true),
    SALMONBERRY("Salmonberry", "", "", true, 0, 5, true, 25, List.of(Seasons.SPRING), false, true),
    SPRING_ONION("Spring Onion", "", "", true, 0, 8, true, 13, List.of(Seasons.SPRING), false, true),
    WILD_HORSERADISH("Wild Horseradish", "", "", true, 0, 50, true, 13, List.of(Seasons.SPRING), false, true),
    FIDDLEHEAD_FERN("Fiddlehead Fern", "", "", true, 0, 90, true, 25, List.of(Seasons.SUMMER), false, true),
    GRAPE_FORAGE("Grape (Forage)", "", "", true, 0, 80, true, 38, List.of(Seasons.SUMMER), false, true),
    RED_MUSHROOM("Red Mushroom", "", "", true, 0, 75, true, -50, List.of(Seasons.SUMMER), false, true),
    SPICE_BERRY("Spice Berry", "", "", true, 0, 80, true, 25, List.of(Seasons.SUMMER), false, true),
    SWEET_PEA("Sweet Pea", "", "", true, 0, 50, false, 0, List.of(Seasons.SUMMER), false, true),
    BLACKBERRY("Blackberry", "", "", true, 0, 25, true, 25, List.of(Seasons.FALL), false, true),
    CHANTERELLE("Chanterelle", "", "", true, 0, 160, true, 75, List.of(Seasons.FALL), false, true),
    HAZELNUT("Hazelnut", "", "", true, 0, 40, true, 38, List.of(Seasons.FALL), false, true),
    PURPLE_MUSHROOM("Purple Mushroom", "", "", true, 0, 90, true, 30, List.of(Seasons.FALL), false, true),
    WILD_PLUM("Wild Plum", "", "", true, 0, 80, true, 25, List.of(Seasons.FALL), false, true),
    CROCUS("Crocus", "", "", true, 0, 60, false, 0, List.of(Seasons.WINTER), false, true),
    CRYSTAL_FRUIT("Crystal Fruit", "", "", true, 0, 150, true, 63, List.of(Seasons.WINTER), false, true),
    HOLLY("Holly", "", "", true, 0, 80, true, -37, List.of(Seasons.WINTER), false, true),
    SNOW_YAM("Snow Yam", "", "", true, 0, 100, true, 30, List.of(Seasons.WINTER), false, true),
    WINTER_ROOT("Winter Root", "", "", true, 0, 70, true, 25, List.of(Seasons.WINTER), false, true);

    private final String name;
    private final String source;
    private final List<Integer> growthStages;
    private final boolean oneTime;
    private final int regrowthTime;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;
    private final List<Seasons> seasons;
    private final boolean canBecomeGiant;
    private final boolean isForage;

    CropType(String name, String source, String stagesStr, boolean oneTime, int regrowthTime,
             int baseSellPrice, boolean isEdible, int energy, List<Seasons> seasons,
             boolean canBecomeGiant, boolean isForage) {
        this.name = name;
        this.source = source;
        this.growthStages = stagesStr.isEmpty() ? List.of() :
            Arrays.stream(stagesStr.split("-")).map(Integer::parseInt).collect(Collectors.toList());
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
        this.canBecomeGiant = canBecomeGiant;
        this.isForage = isForage;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public List<Integer> getGrowthStages() {
        return growthStages;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public int getRegrowthTime() {
        return regrowthTime;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public boolean isEdible() {
        return isEdible;
    }

    public int getEnergy() {
        return energy;
    }

    public List<Seasons> getSeasons() {
        return seasons;
    }

    public boolean canBecomeGiant() {
        return canBecomeGiant;
    }

    public boolean isForage() {
        return isForage;
    }

    public boolean growsIn(Seasons season) {
        return seasons.contains(season);
    }
}
