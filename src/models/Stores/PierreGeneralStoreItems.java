package models.Stores;

import models.farming.CropSeedType;
import models.enums.Seasons;

public enum PierreGeneralStoreItems {

    PARSNIP_SEEDS(CropSeedType.PARSNIP_SEEDS, "Plant these in the spring. Takes 4 days to mature.", 20, 30, 5, Seasons.SPRING),
    BEAN_STARTER(null, "Plant these in the spring. Takes 10 days to mature, but keeps producing after that. Grows on a trellis.", 60, 90, 5, Seasons.SPRING),
    CAULIFLOWER_SEEDS(CropSeedType.CAULIFLOWER_SEEDS, "Plant these in the spring. Takes 12 days to produce a large cauliflower.", 80, 120, 5, Seasons.SPRING),
    POTATO_SEEDS(CropSeedType.POTATO_SEEDS, "Plant these in the spring. Takes 6 days to mature, and has a chance of yielding multiple potatoes at harvest.", 50, 75, 5, Seasons.SPRING),
    TULIP_BULB(CropSeedType.TULIP_BULB, "Plant in spring. Takes 6 days to produce a colorful flower. Assorted colors.", 20, 30, 5, Seasons.SPRING),
    KALE_SEEDS(CropSeedType.KALE_SEEDS, "Plant these in the spring. Takes 6 days to mature. Harvest with the scythe.", 70, 105, 5, Seasons.SPRING),
    JAZZ_SEEDS(CropSeedType.JAZZ_SEEDS, "Plant in spring. Takes 7 days to produce a blue puffball flower.", 30, 45, 5, Seasons.SPRING),
    GARLIC_SEEDS(CropSeedType.GARLIC_SEEDS, "Plant these in the spring. Takes 4 days to mature.", 40, 60, 5, Seasons.SPRING),
    RICE_SHOOT(CropSeedType.RICE_SHOOT, "Plant these in the spring. Takes 8 days to mature. Grows faster if planted near a body of water. Harvest with the scythe.", 40, 60, 5, Seasons.SPRING),

    MELON_SEEDS(CropSeedType.MELON_SEEDS, "Plant these in the summer. Takes 12 days to mature.", 80, 120, 5, Seasons.SUMMER),
    TOMATO_SEEDS(CropSeedType.TOMATO_SEEDS, "Plant these in the summer. Takes 11 days to mature, and continues to produce after first harvest.", 50, 75, 5, Seasons.SUMMER),
    BLUEBERRY_SEEDS(CropSeedType.BLUEBERRY_SEEDS, "Plant these in the summer. Takes 13 days to mature, and continues to produce after first harvest.", 80, 120, 5, Seasons.SUMMER),
    PEPPER_SEEDS(CropSeedType.PEPPER_SEEDS, "Plant these in the summer. Takes 5 days to mature, and continues to produce after first harvest.", 40, 60, 5, Seasons.SUMMER),
    WHEAT_SEEDS(CropSeedType.WHEAT_SEEDS, "Plant these in the summer or AUTUMN. Takes 4 days to mature. Harvest with the scythe.", 10, 15, 5, Seasons.SUMMER),
    RADISH_SEEDS(CropSeedType.RADISH_SEEDS, "Plant these in the summer. Takes 6 days to mature.", 40, 60, 5, Seasons.SUMMER),
    POPPY_SEEDS(CropSeedType.POPPY_SEEDS, "Plant in summer. Produces a bright red flower in 7 days.", 100, 150, 5, Seasons.SUMMER),
    SPANGLE_SEEDS(CropSeedType.SPANGLE_SEEDS, "Plant in summer. Takes 8 days to produce a vibrant tropical flower. Assorted colors.", 50, 75, 5, Seasons.SUMMER),
    HOPS_STARTER(CropSeedType.HOPS_STARTER, "Plant these in the summer. Takes 11 days to grow, but keeps producing after that. Grows on a trellis.", 60, 90, 5, Seasons.SUMMER),
    CORN_SEEDS(CropSeedType.CORN_SEEDS, "Plant these in the summer or AUTUMN. Takes 14 days to mature, and continues to produce after first harvest.", 150, 225, 5, Seasons.SUMMER),
    SUNFLOWER_SEEDS(CropSeedType.SUNFLOWER_SEEDS, "Plant in summer or AUTUMN. Takes 8 days to produce a large sunflower. Yields more seeds at harvest.", 200, 300, 5, Seasons.SUMMER),
    RED_CABBAGE_SEEDS(CropSeedType.RED_CABBAGE_SEEDS, "Plant these in the summer. Takes 9 days to mature.", 100, 150, 5, Seasons.SUMMER),

    EGGPLANT_SEEDS(CropSeedType.EGGPLANT_SEEDS, "Plant these in the AUTUMN. Takes 5 days to mature, and continues to produce after first harvest.", 20, 30, 5, Seasons.FALL),
    PUMPKIN_SEEDS(null, "Plant these in the AUTUMN. Takes 13 days to mature.", 100, 150, 5, Seasons.FALL),
    BOK_CHOY_SEEDS(CropSeedType.BOK_CHOY_SEEDS, "Plant these in the AUTUMN. Takes 4 days to mature.", 50, 75, 5, Seasons.FALL),
    YAM_SEEDS(CropSeedType.YAM_SEEDS, "Plant these in the AUTUMN. Takes 10 days to mature.", 60, 90, 5, Seasons.FALL),
    CRANBERRY_SEEDS(CropSeedType.CRANBERRY_SEEDS, "Plant these in the AUTUMN. Takes 7 days to mature, and continues to produce after first harvest.", 240, 360, 5, Seasons.FALL),
    FAIRY_SEEDS(CropSeedType.FAIRY_SEEDS, "Plant in AUTUMN. Takes 12 days to produce a mysterious flower. Assorted Colors.", 200, 300, 5, Seasons.FALL),
    AMARANTH_SEEDS(CropSeedType.AMARANTH_SEEDS, "Plant these in the AUTUMN. Takes 7 days to grow. Harvest with the scythe.", 70, 105, 5, Seasons.FALL),
    GRAPE_STARTER(CropSeedType.GRAPE_STARTER, "Plant these in the AUTUMN. Takes 10 days to grow, but keeps producing after that. Grows on a trellis.", 60, 90, 5, Seasons.FALL),
    ARTICHOKE_SEEDS(CropSeedType.ARTICHOKE_SEEDS, "Plant these in the AUTUMN. Takes 8 days to mature.", 30, 45, 5, Seasons.FALL);

    public final Object item;
    public final String description;
    public final int price;
    public final int sellPrice;
    public final int dailyLimit;
    public final Seasons season;

    PierreGeneralStoreItems(Object item, String description, int price, int sellPrice, int dailyLimit, Seasons season) {
        this.item = item;
        this.description = description;
        this.price = price;
        this.sellPrice = sellPrice;
        this.dailyLimit = dailyLimit;
        this.season = season;
    }

    public String getName() {
        return name().toLowerCase().replaceAll("_", " ");
    }
}