package models.farming;

import models.enums.Seasons;

public enum ForageSeedTypes {
    JAZZ_SEEDS("Jazz Seeds", Seasons.SPRING),
    CARROT_SEEDS("Carrot Seeds", Seasons.SPRING),
    CAULIFLOWER_SEEDS("Cauliflower Seeds", Seasons.SPRING),
    COFFEE_BEAN("Coffee Bean", Seasons.SPRING),
    GARLIC_SEEDS("Garlic Seeds", Seasons.SPRING),
    BEAN_STARTER("Bean Starter", Seasons.SPRING),
    KALE_SEEDS("Kale Seeds", Seasons.SPRING),
    PARSNIP_SEEDS("Parsnip Seeds", Seasons.SPRING),
    POTATO_SEEDS("Potato Seeds", Seasons.SPRING),
    RHUBARB_SEEDS("Rhubarb Seeds", Seasons.SPRING),
    STRAWBERRY_SEEDS("Strawberry Seeds", Seasons.SPRING),
    TULIP_BULB("Tulip Bulb", Seasons.SPRING),
    RICE_SHOOT("Rice Shoot", Seasons.SPRING),
    BLUEBERRY_SEEDS("Blueberry Seeds", Seasons.SUMMER),
    CORN_SEEDS("Corn Seeds", Seasons.SUMMER),
    HOPS_STARTER("Hops Starter", Seasons.SUMMER),
    PEPPER_SEEDS("Pepper Seeds", Seasons.SUMMER),
    MELON_SEEDS("Melon Seeds", Seasons.SUMMER),
    POPPY_SEEDS("Poppy Seeds", Seasons.SUMMER),
    RADISH_SEEDS("Radish Seeds", Seasons.SUMMER),
    RED_CABBAGE_SEEDS("Red Cabbage Seeds", Seasons.SUMMER),
    STARFRUIT_SEEDS("Starfruit Seeds", Seasons.SUMMER),
    SPANGLE_SEEDS("Spangle Seeds", Seasons.SUMMER),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", Seasons.SUMMER),
    SUNFLOWER_SEEDS("Sunflower Seeds", Seasons.SUMMER),
    TOMATO_SEEDS("Tomato Seeds", Seasons.SUMMER),
    WHEAT_SEEDS("Wheat Seeds", Seasons.SUMMER),
    AMARANTH_SEEDS("Amaranth Seeds", Seasons.FALL),
    ARTICHOKE_SEEDS("Artichoke Seeds", Seasons.FALL),
    BEET_SEEDS("Beet Seeds", Seasons.FALL),
    BOK_CHOY_SEEDS("Bok Choy Seeds", Seasons.FALL),
    BROCCOLI_SEEDS("Broccoli Seeds", Seasons.FALL),
    CRANBERRY_SEEDS("Cranberry Seeds", Seasons.FALL),
    EGGPLANT_SEEDS("Eggplant Seeds", Seasons.FALL),
    FAIRY_SEEDS("Fairy Seeds", Seasons.FALL),
    GRAPE_STARTER("Grape Starter", Seasons.FALL),
    PUMPKIN_SEEDS("Pumpkin Seeds", Seasons.FALL),
    YAM_SEEDS("Yam Seeds", Seasons.FALL),
    RARE_SEED("Rare Seed", Seasons.FALL),
    POWDERMELON_SEEDS("Powdermelon Seeds", Seasons.WINTER),
    ANCIENT_SEEDS("Ancient Seeds", Seasons.SPECIAL),
    MIXED_SEEDS("Mixed Seeds", Seasons.SPECIAL);

    private final String name;
    private final Seasons season;

    ForageSeedTypes(String name, Seasons season) {
        this.name = name;
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public Seasons getSeason() {
        return season;
    }
}
