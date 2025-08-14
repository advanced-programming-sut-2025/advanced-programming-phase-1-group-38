package io.github.StardewValley.models.enums.Types;

import io.github.StardewValley.models.ItemType;

public enum MaterialType implements MaterialTypes, ItemType {

    // ─── Animal / Dairy / Pantry ──────────────────────────────────────────────
    Egg("Egg"),
    Milk("Milk"),
    Cheese("Cheese"),
    Sugar("Sugar"),
    Oil("Oil"),
    Wool("Wool", "Animal_product/Wool.png"),
    Truffle("Truffle", "Animal_product/Truffle.png"),
    Honey("Honey", "Artisan/Honey.png"),

    // ─── Crops ────────────────────────────────────────────────────────────────
    Wheat("Wheat"),
    WheatFlour("Wheat Flour"),
    Pumpkin("Pumpkin"),
    Tomato("Tomato"),
    Corn("Corn"),
    Potato("Potato"),
    Blueberry("Blueberry"),
    Melon("Melon"),
    Apricot("Apricot"),
    RedCabbage("Red Cabbage"),
    Radish("Radish"),
    Amaranth("Amaranth"),
    Kale("Kale"),
    Beet("Beet"),
    Parsnip("Parsnip"),
    Carrot("Carrot"),
    Eggplant("Eggplant"),
    Leek("Leek"),
    Sunflower("Sunflower"),
    SunflowerSeeds("Sunflower Seeds"),
    Hops("Hops"),

    // ─── Fish (برای رسپی‌ها/دودکُن) ─────────────────────────────────────────
    Sardine("Sardine"),
    Salmon("Salmon"),
    Flounder("Flounder"),
    MidnightCarp("Midnight Carp"),

    // ─── Foraging ────────────────────────────────────────────────────────────
    Dandelion("Dandelion"),
    Coffee("Coffee"),
    CoffeeBean("Coffee Bean"),

    // ─── Processed / Cooked basics (اگر نیاز شد در فروشگاه/رسپی) ───────────
    Rice("Rice"),
    Bread("Bread"),
    HashBrowns("Hashbrowns", "items/Hashbrowns.png"), // فایل واقعی یک‌کلمه‌ای است
    Omelet("Omelet"),

    // ─── Resources / Ores / Bars / Misc ──────────────────────────────────────
    CopperOre("Copper Ore"),
    IronOre("Iron Ore"),
    GoldOre("Gold Ore"),
    IridiumOre("Iridium Ore"),
    CopperBar("Copper Bar"),
    IronBar("Iron Bar"),
    GoldBar("Gold Bar"),
    IridiumBar("Iridium Bar"),
    Wood("Wood"),
    Stone("Stone"),
    Coal("Coal"),                 // خروجی Charcoal Kiln
    Fiber("Fiber"),
    Iron("Iron"),
    HardWood("Hard wood"),
    GoldCoin("Gold coin"),
    Diamond("Diamond"),

    // ─── Pierre’s / Shop leftovers ───────────────────────────────────────────
    Bouquet("Bouquet"),
    WeddingRing("Wedding Ring"),
    DehydratorRecipe("Dehydrator recipe"),
    GrassStarterRecipe("Grass Starter recipe"),
    Vinegar("Vinegar"),
    DeluxeRetainingSoil("Deluxe retaining Soil"),
    GrassStarter("Grass Starter"),
    SpeedGro("Speed-Gro"),
    BasicRetainingSoil("Basic retaining Soil"),
    QualityRetainingSoil("Quality-Retaining Soil"),

    // ─── Joja ────────────────────────────────────────────────────────────────
    JojaCola("Joja Cola"),

    // ─── Carpenter buildings (به‌عنوان آیتم خریدنی) ────────────────────────
    Barn("Barn"),
    BigBarn("Big Barn"),
    DeluxeBarn("Deluxe Barn"),
    Coop("Coop"),
    BigCoop("Big Coop"),
    DeluxeCoop("Deluxe Coop"),
    Well("Well"),
    ShippingBin("Shipping Bin"),

    // ─── Stardrop Saloon items ───────────────────────────────────────────────
    Beer("Beer"),
    Salad("Salad"),
    Spaghetti("Spaghetti"),
    Pizza("Pizza"),
    CHERRY_BOMB("Cherry Bomb"),
    BOMB("Bomb"),
    MEGA_BOMB("Mega Bomb"),
    SPRINKLER("Sprinkler"),
    QUALITY_SPRINKLER("Quality Sprinkler"),
    IRIDIUM_SPRINKLER("Iridium Sprinkler"),
    CHARCOAL_KILN("Charcoal Kiln"),
    FURNACE("Furnace"),
    SCARECROW("Scarecrow"),
    DELUXE_SCARECROW("Deluxe Scarecrow"),
    BEE_HOUSE("Bee House"),
    CHEESE_PRESS("Cheese Press"),
    KEG("Keg"),
    LOOM("Loom"),
    MAYONNAISE_MACHINE("Mayonnaise Machine"),
    OIL_MAKER("Oil Maker"),
    PRESERVES_JAR("Preserves Jar"),
    DEHYDRATOR("Dehydrator"),
    GRASS_STARTER("Grass Starter"),
    FISH_SMOKER("Fish Smoker"),
    MYSTIC_TREE_SEED("Mystic Tree Seed"),

    // ─── Marnie’s Ranch ─────────────────────────────────────────────────────
    Hay("Hay"),
    MilkPail("Milk Pail"),
    Shears("Shears"),

    // ─── Fishing poles & recipes ─────────────────────────────────────────────
    FishSmokerRecipe("Fish Smoker recipe"),
    TroutSoup("Trout Soup"),
    BambooPole("Bamboo Pole"),
    TrainingRod("Training Rod"),
    FiberglassRod("Fiberglass Rod"),
    IridiumRod("Iridium Rod");

    // ─────────────────────────── Fields ───────────────────────────
    private final String name;
    /** اگر اسم فایل با نام آیتم متفاوت است، این را مقدار بده (می‌تواند شامل پوشه باشد). */
    private final String iconOverride;

    // ───────────────────── Constructors ─────────────────────
    MaterialType(String name) {
        this(name, null);
    }

    MaterialType(String name, String iconOverride) {
        this.name = name;
        this.iconOverride = iconOverride;
    }

    // ───────────────────── ItemType / MaterialTypes ─────────────────────
    @Override public boolean isTool() { return false; }           // اگر لازم شد می‌توانی برای بعضی‌ها true کنی
    @Override public String getName() { return this.name; }
    @Override public String id() { return this.name; }

    @Override
    public String iconPath() {
        // اگر برای این آیتم مسیر خاص داده شده، همان را برگردان
        if (iconOverride != null) {
            // اگر کاربر مسیر کامل داده باشد (مثلاً "Animal_product/Wool.png") ما فقط prefix را اضافه می‌کنیم اگر لازم بود
            if (iconOverride.startsWith("items/") || iconOverride.contains("/")) {
                return iconOverride.startsWith("items/") ? iconOverride : iconOverride; // مسیر دلخواه
            }
            return "items/" + iconOverride;
        }

        // ساخت خودکار مسیر: items/Title_Case_With_Underscores.png
        final String base = "items/";
        String s = this.name.trim().replaceAll("\\s+", " ");
        StringBuilder out = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // حروف اول هر کلمه را کپیتال کن
            if (i == 0 || s.charAt(i - 1) == ' ' || s.charAt(i - 1) == '-') {
                out.append(Character.toUpperCase(c));
            } else {
                out.append(c);
            }
        }
        String fileBase = out.toString()
                .replace(" ", "_")
                .replace("'", "%27");
        return base + fileBase + ".png";
    }

    @Override public int maxStack() { return 999; }
}