//package io.github.StardewValley.models.farming;
//
//
//import io.github.StardewValley.models.CropType;
//import io.github.StardewValley.models.enums.Seasons;
//import io.github.StardewValley.models.enums.Types.MaterialTypes;
//
//public enum CropSeedType implements SeedType, MaterialTypes {
//    JAZZ_SEEDS("Jazz Seeds", CropType.BLUE_JAZZ, Seasons.SPRING),
//    CARROT_SEEDS("Carrot Seeds", CropType.CARROT, Seasons.SPRING),
//    CAULIFLOWER_SEEDS("Cauliflower Seeds", CropType.CAULIFLOWER, Seasons.SPRING),
//    COFFEE_BEAN("Coffee Bean", CropType.COFFEE_BEAN, Seasons.SPRING),
//    GARLIC_SEEDS("Garlic Seeds", CropType.GARLIC, Seasons.SPRING),
//    BEAN_STARTER("Bean Starter", CropType.GREEN_BEAN, Seasons.SPRING),
//    KALE_SEEDS("Kale Seeds", CropType.KALE, Seasons.SPRING),
//    PARSNIP_SEEDS("Parsnip Seeds", CropType.PARSNIP, Seasons.SPRING),
//    POTATO_SEEDS("Potato Seeds", CropType.POTATO, Seasons.SPRING),
//    RHUBARB_SEEDS("Rhubarb Seeds", CropType.RHUBARB, Seasons.SPRING),
//    STRAWBERRY_SEEDS("Strawberry Seeds", CropType.STRAWBERRY, Seasons.SPRING),
//    TULIP_BULB("Tulip Bulb", CropType.TULIP, Seasons.SPRING),
//    RICE_SHOOT("Rice Shoot", CropType.UNMILLED_RICE, Seasons.SPRING),
//
//    BLUEBERRY_SEEDS("Blueberry Seeds", CropType.BLUEBERRY, Seasons.SUMMER),
//    CORN_SEEDS("Corn Seeds", CropType.CORN, Seasons.SUMMER),
//    HOPS_STARTER("Hops Starter", CropType.HOPS, Seasons.SUMMER),
//    PEPPER_SEEDS("Pepper Seeds", CropType.HOT_PEPPER, Seasons.SUMMER),
//    MELON_SEEDS("Melon Seeds", CropType.MELON, Seasons.SUMMER),
//    POPPY_SEEDS("Poppy Seeds", CropType.POPPY, Seasons.SUMMER),
//    RADISH_SEEDS("Radish Seeds", CropType.RADISH, Seasons.SUMMER),
//    RED_CABBAGE_SEEDS("Red Cabbage Seeds", CropType.RED_CABBAGE, Seasons.SUMMER),
//    STARFRUIT_SEEDS("Starfruit Seeds", CropType.STARFRUIT, Seasons.SUMMER),
//    SPANGLE_SEEDS("Spangle Seeds", CropType.SUMMER_SPANGLE, Seasons.SUMMER),
//    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", CropType.SUMMER_SQUASH, Seasons.SUMMER),
//    SUNFLOWER_SEEDS("Sunflower Seeds", CropType.SUNFLOWER, Seasons.SUMMER),
//    TOMATO_SEEDS("Tomato Seeds", CropType.TOMATO, Seasons.SUMMER),
//    WHEAT_SEEDS("Wheat Seeds", CropType.WHEAT, Seasons.SUMMER),
//
//    AMARANTH_SEEDS("Amaranth Seeds", CropType.AMARANTH, Seasons.FALL),
//    ARTICHOKE_SEEDS("Artichoke Seeds", CropType.ARTICHOKE, Seasons.FALL),
//    BEET_SEEDS("Beet Seeds", CropType.BEET, Seasons.FALL),
//    BOK_CHOY_SEEDS("Bok Choy Seeds", CropType.BOK_CHOY, Seasons.FALL),
//    BROCCOLI_SEEDS("Broccoli Seeds", CropType.BROCCOLI, Seasons.FALL),
//    CRANBERRY_SEEDS("Cranberry Seeds", CropType.CRANBERRIES, Seasons.FALL),
//    EGGPLANT_SEEDS("Eggplant Seeds", CropType.EGGPLANT, Seasons.FALL),
//    FAIRY_SEEDS("Fairy Seeds", CropType.FAIRY_ROSE, Seasons.FALL),
//    GRAPE_STARTER("Grape Starter", CropType.GRAPE, Seasons.FALL),
//    PUMPKIN_SEEDS("Pumpkin Seeds", CropType.PUMPKIN, Seasons.FALL),
//    YAM_SEEDS("Yam Seeds", CropType.YAM, Seasons.FALL),
//    RARE_SEED("Rare Seed", CropType.SWEET_GEM_BERRY, Seasons.FALL),
//
//    POWDERMELON_SEEDS("Powdermelon Seeds", CropType.POWDERMELON, Seasons.WINTER),
//
//    ANCIENT_SEEDS("Ancient Seeds", CropType.ANCIENT_FRUIT, Seasons.SPECIAL),
//    MIXED_SEEDS("Mixed Seeds", null, Seasons.SPECIAL);
//
//    private final String name;
//    private final CropType cropType;
//    private final Seasons season;
//
//    CropSeedType(String name, CropType cropType, Seasons season) {
//        this.name = name;
//        this.cropType = cropType;
//        this.season = season;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public boolean isCrop() {
//        return true;
//    }
//
//    public CropType getCropType() {
//        return cropType;
//    }
//
//    public Seasons getSeason() {
//        return season;
//    }
//
//    @Override
//    public boolean isTool() {
//        return false;
//    }
//
//    @Override
//    public String toString() {
//        return getName();
//    }
//}
