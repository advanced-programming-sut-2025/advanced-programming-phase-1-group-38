package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.ItemType;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ArtisanProductType implements ItemType {

    HONEY             ("Honey","Honey.png",999, 350,  75,  true),
    CHEESE            ("Cheese","Cheese.png",999, 230, 100,  true),
    LARGE_CHEESE      ("Large Cheese","Cheese.png",999, 345, 100,  true),
    GOAT_CHEESE       ("Goat Cheese","Goat_Cheese.png",999, 400, 100,  true),
    LARGE_GOAT_CHEESE ("Large Goat Cheese","Goat_Cheese.png",999, 600, 100,  true),
    COFFEE            ("Coffee","Coffee.png",999, 150,  75,  true),
    BEER              ("Beer","Beer.png",999, 200,  50,  true),
    MEAD              ("Mead","Mead.png",999, 300, 100,  true),
    VINEGAR           ("Vinegar","Vinegar.png",999, 100,  13,  true),

    TRUFFLE_OIL       ("Truffle Oil","Truffle_Oil.png",999, 1065,  0,  false),
    CLOTH             ("Cloth","Cloth.png",999, 470,   0,  false),
    CAVIAR            ("Caviar","Caviar.png",999, 500,  75,  true),

    MAPLE_SYRUP       ("Maple Syrup","Maple_Syrup.png",999, 200,  45,  true),
    OAK_RESIN         ("Oak Resin","Oak_Resin.png",999, 150,   0,  false),
    PINE_TAR          ("Pine Tar","Pine_Tar.png",999, 100,   0,  false),

    RAISINS           ("Raisins","Raisins.png",999, 600, 125,  true),

    DRIED_MUSHROOMS   ("Dried Mushrooms","Dried_Mushrooms.png",999, 250,  50,  true),
    DRIED_FRUIT       ("Dried Fruit","Dried_Fruit.png",999, 250,  75,  true),

    // ── محصولات داینامیک (آیکن‌های واریانت / قیمت و انرژی متغیر) ───────────────
    WINE ("Wine", "Wine.png", 999, -1, -1, true, variants(
            "RED","Red_Wine.png","WHITE","White_Wine.png","PINK","Pink_Wine.png",
            "PURPLE","Purple_Wine.png","YELLOW","Yellow_Wine.png","ORANGE","Orange_Wine.png",
            "BLUE","Blue_Wine.png","LIGHT_BLUE","Light_Blue_Wine.png","DARK_BLUE","Dark_Blue_Wine.png",
            "DARK_PINK","Dark_Pink_Wine.png","DARK_PURPLE","Dark_Purple_Wine.png","BROWN","Brown_Wine.png"
    )),

    JUICE ("Juice", "Juice.png", 999, -1, -1, true, variants(
            "RED","Red_Juice.png","WHITE","White_Juice.png","YELLOW","Yellow_Juice.png",
            "ORANGE","Orange_Juice.png","GREEN","Green_Juice.png","PINK","Pink_Juice.png",
            "PURPLE","Purple_Juice.png","DARK_PURPLE","Dark_Purple_Juice.png"
    )),

    JELLY ("Jelly", "Jelly.png", 999, -1, -1, true, variants(
            "RED","Red_Jelly.png","WHITE","White_Jelly.png","YELLOW","Yellow_Jelly.png",
            "ORANGE","Orange_Jelly.png","GREEN","Green_Jelly.png","PINK","Pink_Jelly.png",
            "PURPLE","Purple_Jelly.png","BLUE","Blue_Jelly.png","LIGHT_BLUE","Light_Blue_Jelly.png",
            "DARK_BLUE","Dark_Blue_Jelly.png","DARK_PINK","Dark_Pink_Jelly.png",
            "DARK_PURPLE","Dark_Purple_Jelly.png","BROWN","Brown_Jelly.png"
    )),

    PICKLES ("Pickles", "Pickles.png", 999, -1, -1, true, variants(
            "WHITE","White_Pickles.png","YELLOW","Yellow_Pickles.png","ORANGE","Orange_Pickles.png",
            "GREEN","Green_Pickles.png","PINK","Pink_Pickles.png","PURPLE","Purple_Pickles.png",
            "RED","Red_Pickles.png","BROWN","Brown_Pickles.png","DARK_PINK","Dark_Pink_Pickles.png",
            "DARK_PURPLE","Dark_Purple_Pickles.png"
    )),

    // مایونزها
    MAYONNAISE      ("Mayonnaise",           "Mayonnaise.png",          999, 190, 45, true),
    DUCK_MAYONNAISE ("Duck Mayonnaise",      "Duck_Mayonnaise.png",     999, 375, 45, true),
    VOID_MAYONNAISE ("Void Mayonnaise",      "Void_Mayonnaise.png",     999, 275, 45, true),
    DINOSAUR_MAYO   ("Dinosaur Mayonnaise",  "Dinosaur_Mayonnaise.png", 999, 800, 45, true),

    // نوشیدنی خاص
    GREEN_TEA       ("Green Tea",            "Green_Tea.png",           999, 100, 30, true),
    PINA_COLADA     ("Piña Colada",          "Pi%C3%B1a_Colada.png",    999, 300, 75, true);

    private static final String BASE_PATH = "Artisan/";

    private final String id;
    private final String iconFile;
    private final int    maxStack;
    private final int    sellPrice;
    private final int    energy;
    private final boolean edible;
    private final Map<String, String> variantIcons; // nullable

    ArtisanProductType(String id, String iconFile, int maxStack, int sellPrice, int energy, boolean edible) {
        this(id, iconFile, maxStack, sellPrice, energy, edible, null);
    }

    ArtisanProductType(String id, String iconFile, int maxStack, int sellPrice, int energy,
                       boolean edible, Map<String, String> variantIcons) {
        this.id = id;
        this.iconFile = iconFile;
        this.maxStack = maxStack;
        this.sellPrice = sellPrice;
        this.energy = energy;
        this.edible = edible;
        this.variantIcons = variantIcons;
    }

    // ItemType
    @Override public String id()       { return id; }
    @Override public String iconPath() { return BASE_PATH + iconFile; }
    @Override public int    maxStack() { return maxStack; }
    @Override public boolean stackable(){ return maxStack != 1; }

    // آیکن واریانت
    public String iconPath(String variantKey) {
        if (variantIcons == null || variantKey == null) return iconPath();
        String f = variantIcons.get(variantKey);
        return BASE_PATH + (f != null ? f : iconFile);
    }

    // اقتصاد/مصرف
    public int  getSellPrice() { return sellPrice; }
    public int  getEnergy()    { return energy; }
    public boolean isEdible()  { return edible; }
    public boolean hasDynamicPricing() { return sellPrice == -1; }
    public boolean hasDynamicEnergy()  { return energy    == -1; }

    public static ArtisanProductType fromName(String name) {
        for (ArtisanProductType t : values()) {
            if (t.id.equalsIgnoreCase(name)) return t;
        }
        return null;
    }

    // ===== Java 8 helper (جایگزین Map.of) =====
    private static Map<String,String> variants(String... kv) {
        LinkedHashMap<String,String> m = new LinkedHashMap<>();
        if (kv == null) return m;
        if ((kv.length & 1) != 0) {
            throw new IllegalArgumentException("variants() requires even number of strings: key,value,...");
        }
        for (int i = 0; i < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1]);
        }
        return m;
    }
}
