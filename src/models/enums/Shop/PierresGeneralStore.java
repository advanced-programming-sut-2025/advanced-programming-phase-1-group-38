package models.enums.Shop;

import models.enums.CraftingRecipes;
import models.enums.Types.BackpackType;
import models.enums.Types.CraftingMachineType;
import models.enums.Types.MaterialType;
import models.enums.Seasons;
import models.enums.Types.MaterialTypes;
import models.farming.CropSeedType;
import models.farming.TreeSaplingType;


public enum PierresGeneralStore implements ShopEntry {
    // Year-Round Stock
    Rice(null, "Rice", "A basic grain often served under vegetables.",
            200, Integer.MAX_VALUE, MaterialType.Rice),
    WheatFlour(null, "Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.",
            100, Integer.MAX_VALUE, MaterialType.WheatFlour),
    Bouquet(null, "Bouquet", "A gift that shows your romantic interest.",
            1000, 2, MaterialType.Bouquet),
    WeddingRing(null, "Wedding Ring", "It's used to ask for another farmer's hand in marriage.",
            10000, 2, MaterialType.WeddingRing),
    DehydratorRecipe(null, "Dehydrator (Recipe)", "A recipe to make Dehydrator",
            10000, 1, CraftingRecipes.DehydratorRecipe),
    GrassStarterRecipe(null, "Grass Starter (Recipe)", "A recipe to make Grass Starter",
            1000, 1, CraftingRecipes.GrassStarterRecipe),
    Sugar(null, "Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.",
            100, Integer.MAX_VALUE, MaterialType.Sugar),
    Oil(null, "Oil", "All purpose cooking oil.",
            200, Integer.MAX_VALUE, MaterialType.Oil),
    Vinegar(null, "Vinegar", "An aged fermented liquid used in many cooking recipes.",
            200, Integer.MAX_VALUE, MaterialType.Vinegar),
    DeluxeRetainingSoil(null, "Deluxe Retaining Soil", "This soil has a 100% chance of staying watered overnight.",
            150, Integer.MAX_VALUE, MaterialType.DeluxeRetainingSoil),
    GrassStarter(null, "Grass Starter", "Place this on your farm to start a new patch of grass.",
            100, Integer.MAX_VALUE, CraftingMachineType.GRASS_STARTER),
    SpeedGro(null, "Speed-Gro", "Makes the plants grow 1 day earlier.",
            100, Integer.MAX_VALUE, MaterialType.SpeedGro),
    AppleSapling(null, "Apple Sapling", "Bears fruit in fall. Needs 28 days to mature.",
            4000, Integer.MAX_VALUE, TreeSaplingType.APPLE_SAPLING),
    ApricotSapling(null, "Apricot Sapling", "Bears fruit in spring. Needs 28 days to mature.",
            2000, Integer.MAX_VALUE, TreeSaplingType.APRICOT_SAPLING),
    CherrySapling(null, "Cherry Sapling", "Bears fruit in spring. Needs 28 days to mature.",
            3400, Integer.MAX_VALUE, TreeSaplingType.CHERRY_SAPLING),
    OrangeSapling(null, "Orange Sapling", "Bears fruit in summer. Needs 28 days to mature.",
            4000, Integer.MAX_VALUE, TreeSaplingType.ORANGE_SAPLING),
    PeachSapling(null, "Peach Sapling", "Bears fruit in summer. Needs 28 days to mature.",
            6000, Integer.MAX_VALUE, TreeSaplingType.PEACH_SAPLING),
    PomegranateSapling(null, "Pomegranate Sapling", "Bears fruit in fall. Needs 28 days to mature.",
            6000, Integer.MAX_VALUE, TreeSaplingType.POMEGRANATE_SAPLING),
    BasicRetainingSoil(null, "Basic Retaining Soil", "Chance of staying watered overnight.",
            100, Integer.MAX_VALUE, MaterialType.BasicRetainingSoil),
    QualityRetainingSoil(null, "Quality Retaining Soil", "CraftingMachine chance of staying watered overnight.",
            150, Integer.MAX_VALUE, MaterialType.QualityRetainingSoil),

    // Backpacks
    LargePack(null, "Big Backpack", "Unlocks the 2nd row of inventory (12 more slots, total 24).",
            2000, 1, BackpackType.LARGE),
    DeluxePack(null, "Deluxe Backpack", "Unlocks the 3rd row of inventory (infinite slots).",
            10000, 1, BackpackType.DELUXE),

    // Seeds – Spring
    ParsnipSeeds(Seasons.SPRING, "Parsnip Seeds", "Plant in spring. Takes 4 days to mature.",
            30, 5, CropSeedType.PARSNIP_SEEDS),
    BeanStarter(Seasons.SPRING, "Bean Starter", "Plant in spring. Takes 10 days. Grows on a trellis.",
            90, 5, CropSeedType.BEAN_STARTER),
    CauliflowerSeeds(Seasons.SPRING, "Cauliflower Seeds", "Plant in spring. Takes 12 days.",
            120, 5, CropSeedType.CAULIFLOWER_SEEDS),
    PotatoSeeds(Seasons.SPRING, "Potato Seeds", "Plant in spring. Takes 6 days.",
            75, 5, CropSeedType.POTATO_SEEDS),
    TulipBulb(Seasons.SPRING, "Tulip Bulb", "Spring flower. Takes 6 days.",
            30, 5, CropSeedType.TULIP_BULB),
    KaleSeeds(Seasons.SPRING, "Kale Seeds", "Spring crop. Takes 6 days.",
            105, 5, CropSeedType.KALE_SEEDS),
    JazzSeeds(Seasons.SPRING, "Jazz Seeds", "Spring flower. Takes 7 days.",
            45, 5, CropSeedType.JAZZ_SEEDS),
    GarlicSeeds(Seasons.SPRING, "Garlic Seeds", "Plant in spring. Takes 4 days.",
            60, 5, CropSeedType.GARLIC_SEEDS),
    RiceShoot(Seasons.SPRING, "Rice Shoot", "Plant in spring. Takes 8 days. Grows faster near water.",
            60, 5, CropSeedType.RICE_SHOOT),

    // Seeds – Summer
    MelonSeeds(Seasons.SUMMER, "Melon Seeds", "Plant in summer. Takes 12 days.",
            120, 5, CropSeedType.MELON_SEEDS),
    TomatoSeeds(Seasons.SUMMER, "Tomato Seeds", "Plant in summer. Takes 11 days.",
            75, 5, CropSeedType.TOMATO_SEEDS),
    BlueberrySeeds(Seasons.SUMMER, "Blueberry Seeds", "Plant in summer. Takes 13 days.",
            120, 5, CropSeedType.BLUEBERRY_SEEDS),
    PepperSeeds(Seasons.SUMMER, "Pepper Seeds", "Plant in summer. Takes 5 days.",
            60, 5, CropSeedType.PEPPER_SEEDS),
    WheatSeeds_Summer(Seasons.SUMMER, "Wheat Seeds", "Summer crop. Takes 4 days.",
            15, 5, CropSeedType.WHEAT_SEEDS),
    RadishSeeds(Seasons.SUMMER, "Radish Seeds", "Plant in summer. Takes 6 days.",
            60, 5, CropSeedType.RADISH_SEEDS),
    PoppySeeds(Seasons.SUMMER, "Poppy Seeds", "Summer flower. Takes 7 days.",
            150, 5, CropSeedType.POPPY_SEEDS),
    SpangleSeeds(Seasons.SUMMER, "Spangle Seeds", "Summer flower. Takes 8 days.",
            75, 5, CropSeedType.SPANGLE_SEEDS),
    HopsStarter(Seasons.SUMMER, "Hops Starter", "Summer crop. Takes 11 days. Grows on a trellis.",
            90, 5, CropSeedType.HOPS_STARTER),
    CornSeeds_Summer(Seasons.SUMMER, "Corn Seeds", "Summer crop. Takes 14 days.",
            225, 5, CropSeedType.CORN_SEEDS),
    SunflowerSeeds_Summer(Seasons.SUMMER, "Sunflower Seeds", "Summer flower. Takes 8 days.",
            300, 5, CropSeedType.SUNFLOWER_SEEDS),
    RedCabbageSeeds(Seasons.SUMMER, "Red Cabbage Seeds", "Summer crop. Takes 9 days.",
            150, 5, CropSeedType.RED_CABBAGE_SEEDS),

    // Seeds – Fall
    EggplantSeeds(Seasons.FALL, "Eggplant Seeds", "Fall crop. Takes 5 days.",
            30, 5, CropSeedType.EGGPLANT_SEEDS),
    CornSeeds_Fall(Seasons.FALL, "Corn Seeds", "Fall crop. Takes 14 days.",
            225, 5, CropSeedType.CORN_SEEDS),
    PumpkinSeeds(Seasons.FALL, "Pumpkin Seeds", "Plant in fall. Takes 13 days.",
            150, 5, CropSeedType.PUMPKIN_SEEDS),
    BokChoySeeds(Seasons.FALL, "Bok Choy Seeds", "Fall crop. Takes 4 days.",
            75, 5, CropSeedType.BOK_CHOY_SEEDS),
    YamSeeds(Seasons.FALL, "Yam Seeds", "Fall crop. Takes 10 days.",
            90, 5, CropSeedType.YAM_SEEDS),
    CranberrySeeds(Seasons.FALL, "Cranberry Seeds", "Fall crop. Takes 7 days.",
            360, 5, CropSeedType.CRANBERRY_SEEDS),
    SunflowerSeeds_Fall(Seasons.FALL, "Sunflower Seeds", "Fall flower. Takes 8 days.",
            300, 5, CropSeedType.SUNFLOWER_SEEDS),
    FairySeeds(Seasons.FALL, "Fairy Seeds", "Fall flower. Takes 12 days.",
            300, 5, CropSeedType.FAIRY_SEEDS),
    AmaranthSeeds(Seasons.FALL, "Amaranth Seeds", "Fall crop. Takes 7 days.",
            105, 5, CropSeedType.AMARANTH_SEEDS),
    GrapeStarter(Seasons.FALL, "Grape Starter", "Fall crop. Takes 10 days. Grows on a trellis.",
            90, 5, CropSeedType.GRAPE_STARTER),
    WheatSeeds_Fall(Seasons.FALL, "Wheat Seeds", "Fall crop. Takes 4 days.",
            15, 5, CropSeedType.WHEAT_SEEDS),
    ArtichokeSeeds(Seasons.FALL, "Artichoke Seeds", "Fall crop. Takes 8 days.",
            45, 5, CropSeedType.ARTICHOKE_SEEDS);


    private final Seasons season;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final MaterialTypes itemType;

    PierresGeneralStore(Seasons season, String name, String description, int price, int dailyLimit, MaterialTypes itemType) {
            this.season = season;
            this.name = name;
            this.description = description;
            this.price = price;
            this.dailyLimit = dailyLimit;
            this.itemType = itemType;
    }

    public Seasons getSeason() { return season; }
    @Override public String getDisplayName() { return name; }
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
