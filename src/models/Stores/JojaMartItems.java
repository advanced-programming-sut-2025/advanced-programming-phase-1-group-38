package models.Stores;

import models.enums.Seasons;
import models.farming.CropSeedType;
import models.enums.Seasons;

public enum JojaMartItems {
    PARSNIP_SEEDS(CropSeedType.PARSNIP_SEEDS, "Plant these in the spring. Takes 4 days to mature.", 25, 5, Seasons.SPRING),
    BEAN_STARTER(null, "Plant these in the spring. Takes 10 days to mature, but keeps producing after that. Grows on a trellis.", 75, 5, Seasons.SPRING),
    CAULIFLOWER_SEEDS(CropSeedType.CAULIFLOWER_SEEDS, "Plant these in the spring. Takes 12 days to produce a large cauliflower.", 100, 5, Seasons.SPRING),
    POTATO_SEEDS(CropSeedType.POTATO_SEEDS, "Plant these in the spring. Takes 6 days to mature, and has a chance of yielding multiple potatoes at harvest.", 62, 5, Seasons.SPRING),
    STRAWBERRY_SEEDS(CropSeedType.STRAWBERRY_SEEDS, "Plant these in spring. Takes 8 days to mature, and keeps producing strawberries after that.", 100, 5, Seasons.SPRING),
    TULIP_BULB(CropSeedType.TULIP_BULB, "Plant in spring. Takes 6 days to produce a colorful flower. Assorted colors.", 25, 5, Seasons.SPRING),
    KALE_SEEDS(CropSeedType.KALE_SEEDS, "Plant these in the spring. Takes 6 days to mature. Harvest with the scythe.", 87, 5, Seasons.SPRING),
    CARROT_SEEDS(CropSeedType.CARROT_SEEDS, "Plant in the spring. Takes 3 days to grow.", 5, 10, Seasons.SPRING),
    RHUBARB_SEEDS(CropSeedType.RHUBARB_SEEDS, "Plant these in the spring. Takes 13 days to mature.", 100, 5, Seasons.SPRING),
    JAZZ_SEEDS(CropSeedType.JAZZ_SEEDS, "Plant in spring. Takes 7 days to produce a blue puffball flower.", 37, 5, Seasons.SPRING),

    TOMATO_SEEDS(CropSeedType.TOMATO_SEEDS, "Plant these in the summer. Takes 11 days to mature, and continues to produce after first harvest.", 62, 5, Seasons.SUMMER),
    PEPPER_SEEDS(CropSeedType.PEPPER_SEEDS, "Plant these in the summer. Takes 5 days to mature, and continues to produce after first harvest.", 50, 5, Seasons.SUMMER),
    WHEAT_SEEDS(CropSeedType.WHEAT_SEEDS, "Plant these in the summer or autumn. Takes 4 days to mature. Harvest with the scythe.", 12, 10, Seasons.SUMMER),
    SUMMER_SQUASH_SEEDS(CropSeedType.SUMMER_SQUASH_SEEDS, "Plant in the summer. Takes 6 days to grow, and continues to produce after first harvest.", 10, 10, Seasons.SUMMER),
    RADISH_SEEDS(CropSeedType.RADISH_SEEDS, "Plant these in the summer. Takes 6 days to mature.", 50, 5, Seasons.SUMMER),
    MELON_SEEDS(CropSeedType.MELON_SEEDS, "Plant these in the summer. Takes 12 days to mature.", 100, 5, Seasons.SUMMER),
    HOPS_STARTER(CropSeedType.HOPS_STARTER, "Plant these in the summer. Takes 11 days to grow, but keeps producing after that. Grows on a trellis.", 75, 5, Seasons.SUMMER),
    POPPY_SEEDS(CropSeedType.POPPY_SEEDS, "Plant in summer. Produces a bright red flower in 7 days.", 125, 5, Seasons.SUMMER),
    SPANGLE_SEEDS(CropSeedType.SPANGLE_SEEDS, "Plant in summer. Takes 8 days to produce a vibrant tropical flower. Assorted colors.", 62, 5, Seasons.SUMMER),
    STARFRUIT_SEEDS(CropSeedType.STARFRUIT_SEEDS, "Plant these in the summer. Takes 13 days to mature.", 400, 5, Seasons.SUMMER),
    SUNFLOWER_SEEDS(CropSeedType.SUNFLOWER_SEEDS, "Plant in summer or autumn. Takes 8 days to produce a large sunflower. Yields more seeds at harvest.", 125, 5, Seasons.SUMMER),

    CORN_SEEDS(CropSeedType.CORN_SEEDS, "Plant these in the summer or autumn. Takes 14 days to mature, and continues to produce after first harvest.", 187, 5, Seasons.FALL),
    EGGPLANT_SEEDS(CropSeedType.EGGPLANT_SEEDS, "Plant these in the autumn. Takes 5 days to mature, and continues to produce after first harvest.", 25, 5, Seasons.FALL),
    PUMPKIN_SEEDS(null, "Plant these in the autumn. Takes 13 days to mature.", 125, 5, Seasons.FALL),
    BROCCOLI_SEEDS(CropSeedType.BROCCOLI_SEEDS, "Plant in the autumn. Takes 8 days to mature, and continues to produce after first harvest.", 15, 5, Seasons.FALL),
    AMARANTH_SEEDS(CropSeedType.AMARANTH_SEEDS, "Plant these in the autumn. Takes 7 days to grow. Harvest with the scythe.", 87, 5, Seasons.FALL),
    GRAPE_STARTER(CropSeedType.GRAPE_STARTER, "Plant these in the autumn. Takes 10 days to grow, but keeps producing after that. Grows on a trellis.", 75, 5, Seasons.FALL),
    BEET_SEEDS(CropSeedType.BEET_SEEDS, "Plant these in the autumn. Takes 6 days to mature.", 20, 5, Seasons.FALL),
    YAM_SEEDS(CropSeedType.YAM_SEEDS, "Plant these in the autumn. Takes 10 days to mature.", 75, 5, Seasons.FALL),
    BOK_CHOY_SEEDS(CropSeedType.BOK_CHOY_SEEDS, "Plant these in the autumn. Takes 4 days to mature.", 62, 5, Seasons.FALL),
    CRANBERRY_SEEDS(CropSeedType.CRANBERRY_SEEDS, "Plant these in the autumn. Takes 7 days to mature, and continues to produce after first harvest.", 300, 5, Seasons.FALL),
    FAIRY_SEEDS(CropSeedType.FAIRY_SEEDS, "Plant in autumn. Takes 12 days to produce a mysterious flower. Assorted Colors.", 250, 5, Seasons.FALL),
    RARE_SEED(null, "Sow in autumn. Takes all season to grow.", 1000, 1, Seasons.FALL),

    POWDER_MELON_SEEDS(null, "This special melon grows in the winter. Takes 7 days to grow.", 20, 10, Seasons.WINTER)
    ;

    public final CropSeedType cropType;
    public final String description;
    public final int price;
    public final int dailyLimit;
    public final Seasons season;

    JojaMartItems(CropSeedType cropType, String description, int price, int dailyLimit, Seasons season) {
        this.cropType = cropType;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.season = season;
    }

    public String getName() {
        return name().toLowerCase().replaceAll("_", " ");
    }
}