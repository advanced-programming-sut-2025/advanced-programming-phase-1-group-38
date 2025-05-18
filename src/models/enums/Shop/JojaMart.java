package models.enums.Shop;

import models.enums.Seasons;
import models.enums.Types.MaterialType;
import models.enums.Types.MaterialTypes;
import models.farming.CropSeedType;

public enum JojaMart implements ShopEntry{
    // Permanent Stock
    JojaCola(null, "Joja Cola", "The flagship product of Joja corporation.",
            75, Integer.MAX_VALUE, MaterialType.JojaCola),
    AncientSeed(null, "Ancient Seed", "Could these still grow?",
            500, 1,CropSeedType.ANCIENT_SEEDS),
    GrassStarter(null, "Grass Starter", "Place this on your farm to start a new patch of grass.",
            125, Integer.MAX_VALUE, MaterialType.GrassStarter),
    Sugar(null, "Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.",
            125, Integer.MAX_VALUE, MaterialType.Sugar),
    WheatFlour(null, "Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.",
            125, Integer.MAX_VALUE, MaterialType.WheatFlour),
    Rice(null, "Rice", "A basic grain often served under vegetables.",
            250, Integer.MAX_VALUE, MaterialType.Rice),

    // Spring Stock
    ParsnipSeeds(Seasons.SPRING, "Parsnip Seeds", "Plant these in the spring. Takes 4 days to mature.",
            25, 5, CropSeedType.PARSNIP_SEEDS),
    BeanStarter(Seasons.SPRING, "Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing.",
            75, 5, CropSeedType.BEAN_STARTER),
    CauliflowerSeeds(Seasons.SPRING, "Cauliflower Seeds", "Plant these in the spring. Takes 12 days.",
            100, 5, CropSeedType.CAULIFLOWER_SEEDS),
    PotatoSeeds(Seasons.SPRING, "Potato Seeds", "Plant these in the spring. Takes 6 days, may yield extra.",
            62, 5, CropSeedType.POTATO_SEEDS),
    StrawberrySeeds(Seasons.SPRING, "Strawberry Seeds", "Plant in spring. Takes 8 days, keeps producing.",
            100, 5, CropSeedType.STRAWBERRY_SEEDS),
    TulipBulb(Seasons.SPRING, "Tulip Bulb", "Plant in spring. Takes 6 days.",
            25, 5, CropSeedType.TULIP_BULB),
    KaleSeeds(Seasons.SPRING, "Kale Seeds", "Plant in spring. Takes 6 days. Harvest with scythe.",
            87, 5, CropSeedType.KALE_SEEDS),
    CoffeeBeansSpring(Seasons.SPRING, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
            200, 1,CropSeedType.COFFEE_BEAN),
    CarrotSeeds(Seasons.SPRING, "Carrot Seeds", "Plant in spring. Takes 3 days.",
            5, 10, CropSeedType.COFFEE_BEAN),
    RhubarbSeeds(Seasons.SPRING, "Rhubarb Seeds", "Plant in spring. Takes 13 days.",
            100, 5, CropSeedType.RHUBARB_SEEDS),
    JazzSeeds(Seasons.SPRING, "Jazz Seeds", "Plant in spring. Takes 7 days.",
            37, 5, CropSeedType.JAZZ_SEEDS),

    // Summer Stock
    TomatoSeeds(Seasons.SUMMER, "Tomato Seeds", "Plant in summer. Takes 11 days, keeps producing.",
            62, 5, CropSeedType.TOMATO_SEEDS),
    PepperSeeds(Seasons.SUMMER, "Pepper Seeds", "Plant in summer. Takes 5 days, keeps producing.",
            50, 5, CropSeedType.PEPPER_SEEDS),
    WheatSeedsSummer(Seasons.SUMMER, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
            12, 10,CropSeedType.WHEAT_SEEDS),
    SummerSquashSeeds(Seasons.SUMMER, "Summer Squash Seeds", "Plant in summer. Takes 6 days, keeps producing.",
            10, 10, CropSeedType.SUMMER_SQUASH_SEEDS),
    RadishSeeds(Seasons.SUMMER, "Radish Seeds", "Plant in summer. Takes 6 days.",
            50, 5, CropSeedType.RADISH_SEEDS),
    MelonSeeds(Seasons.SUMMER, "Melon Seeds", "Plant in summer. Takes 12 days.",
            100, 5, CropSeedType.MELON_SEEDS),
    HopsStarter(Seasons.SUMMER, "Hops Starter", "Summer. Takes 11 days, keeps producing. Trellis.",
            75, 5, CropSeedType.HOPS_STARTER),
    PoppySeeds(Seasons.SUMMER, "Poppy Seeds", "Summer. Bright red flower in 7 days.",
            125, 5, CropSeedType.POPPY_SEEDS),
    SpangleSeeds(Seasons.SUMMER, "Spangle Seeds", "Summer. Tropical flower in 8 days.",
            62, 5, CropSeedType.SPANGLE_SEEDS),
    StarfruitSeeds(Seasons.SUMMER, "Starfruit Seeds", "Summer. Takes 13 days.",
            400, 5, CropSeedType.STARFRUIT_SEEDS),
    CoffeeBeansSummer(Seasons.SUMMER, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
            200, 1, CropSeedType.COFFEE_BEAN),
    SunflowerSeedsSummer(Seasons.SUMMER, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
            125, 5,CropSeedType.SUNFLOWER_SEEDS),

    // Fall Stock
    CornSeeds(Seasons.FALL, "Corn Seeds", "Summer/Fall. Takes 14 days, keeps producing.",
            187, 5, CropSeedType.CORN_SEEDS),
    EggplantSeeds(Seasons.FALL, "Eggplant Seeds", "Fall. Takes 5 days, keeps producing.",
            25, 5, CropSeedType.EGGPLANT_SEEDS),
    PumpkinSeeds(Seasons.FALL, "Pumpkin Seeds", "Fall. Takes 13 days.",
            125, 5, CropSeedType.PUMPKIN_SEEDS),
    BroccoliSeeds(Seasons.FALL, "Broccoli Seeds", "Fall. Takes 8 days, keeps producing.",
            15, 5, CropSeedType.BROCCOLI_SEEDS),
    AmaranthSeeds(Seasons.FALL, "Amaranth Seeds", "Fall. Takes 7 days. Scythe harvest.",
            87, 5, CropSeedType.AMARANTH_SEEDS),
    GrapeStarter(Seasons.FALL, "Grape Starter", "Fall. Takes 10 days, keeps producing. Trellis.",
            75, 5, CropSeedType.GRAPE_STARTER),
    BeetSeeds(Seasons.FALL, "Beet Seeds", "Fall. Takes 6 days.",
            20, 5, CropSeedType.BEET_SEEDS),
    YamSeeds(Seasons.FALL, "Yam Seeds", "Fall. Takes 10 days.",
            75, 5, CropSeedType.YAM_SEEDS),
    BokChoySeeds(Seasons.FALL, "Bok Choy Seeds", "Fall. Takes 4 days.",
            62, 5, CropSeedType.BOK_CHOY_SEEDS),
    CranberrySeeds(Seasons.FALL, "Cranberry Seeds", "Fall. Takes 7 days, keeps producing.",
            300, 5, CropSeedType.CRANBERRY_SEEDS),
    SunflowerSeedsFall(Seasons.FALL, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
            125, 5, CropSeedType.SUNFLOWER_SEEDS),
    FairySeeds(Seasons.FALL, "Fairy Seeds", "Fall. Mysterious flower in 12 days.",
            250, 5,CropSeedType.FAIRY_SEEDS),
    RareSeed(Seasons.FALL, "Rare Seed", "Fall. Takes all season to grow.",
            1000, 1, CropSeedType.RARE_SEED),
    WheatSeedsFall(Seasons.FALL, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
            12, 5, CropSeedType.WHEAT_SEEDS),

    // Winter Stock
    PowdermelonSeeds(Seasons.WINTER, "Powdermelon Seeds", "Winter. Takes 7 days to grow.",
            20, 10, CropSeedType.POWDERMELON_SEEDS),;

    private final Seasons season;
    private final String displayName;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final MaterialTypes itemType;

    JojaMart(Seasons season, String displayName, String description, int price, int dailyLimit, MaterialTypes itemType) {
        this.season = season;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.itemType = itemType;
    }

    public Seasons getSeason() { return season; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }
    @Override
    public MaterialTypes getItemType() { return itemType; }

    @Override
    public String toString() {
        return this.name() + "\nPrice: " + 
        this.price + "\nDescription: " + this.description + 
        "\n----------------------\n";
    }
}
