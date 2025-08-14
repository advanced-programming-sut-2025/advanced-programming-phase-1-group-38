package io.github.StardewValley.models.Artisan;

import com.badlogic.gdx.graphics.Texture;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.models.ItemType;
import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.*;

import static io.github.StardewValley.models.Artisan.ArtisanProductType.*;
import static io.github.StardewValley.models.Artisan.ArtisanRecipe.InputTag;
import static io.github.StardewValley.models.Artisan.MachineType.*;

/**
 * دفترچهٔ رسپی‌ها.
 * زمان‌ها بر حسب «ثانیهٔ بازی» هستند: MIN/HOUR/DAY.
 * خروجی‌ها از نوع ItemType تا با MaterialType/ArtisanProductType هر دو سازگار باشند.
 */
public final class ArtisanRecipeBook {

    public static final float MIN  = 60f;
    public static final float HOUR = 60f * MIN;
    public static final float DAY  = 24f * HOUR;

    private static final List<ArtisanRecipe> ALL = new ArrayList<>();
    private static final Map<MachineType, List<ArtisanRecipe>> BY_MACHINE = new HashMap<>();

    // نگاشت: هر تگ → مجموعه‌ای از آیتم‌های واقعی (برای ANY_* و ...)
    private static final Map<InputTag, Set<ItemType>> TAG_ITEMS = new HashMap<>();

    public static void mapTag(InputTag tag, ItemType... items) {
        TAG_ITEMS.computeIfAbsent(tag, k -> new HashSet<>())
                .addAll(Arrays.asList(items));
    }
    public static void mapTag(InputTag tag, Collection<? extends ItemType> items) {
        TAG_ITEMS.computeIfAbsent(tag, k -> new HashSet<>()).addAll(items);
    }
    public static void unmapTag(InputTag tag, ItemType item) {
        var s = TAG_ITEMS.get(tag);
        if (s != null) s.remove(item);
    }

    // آیکن پیش‌فرض برای تگ‌ها (UI وقتی آیتم واقعی نداریم)
    private static final Map<InputTag, String> TAG_ICON = new HashMap<>();
    static {
        TAG_ICON.put(InputTag.ANY_FRUIT,        "items/Apple.png");
        TAG_ICON.put(InputTag.ANY_VEGETABLE,    "items/Carrot.png");
        TAG_ICON.put(InputTag.ANY_EGG,          "items/Egg.png");
        TAG_ICON.put(InputTag.ANY_MILK,         "items/Milk.png");

        TAG_ICON.put(InputTag.HOPS,             "items/hops.png");
        TAG_ICON.put(InputTag.WHEAT,            "items/wheat.png");
        TAG_ICON.put(InputTag.COFFEE_BEAN,      "items/coffee_bean.png");
        TAG_ICON.put(InputTag.RICE,             "items/rice.png");
        TAG_ICON.put(InputTag.HONEY,            "Artisan/Honey.png");

        TAG_ICON.put(InputTag.MILK,             "items/milk.png");
        TAG_ICON.put(InputTag.GOAT_MILK,        "items/goat_milk.png");
        TAG_ICON.put(InputTag.DUCK_EGG,         "items/duck_egg.png");
        TAG_ICON.put(InputTag.VOID_EGG,         "items/void_egg.png");
        TAG_ICON.put(InputTag.DINO_EGG,         "items/dinosaur_egg.png");

        TAG_ICON.put(InputTag.WOOL,             "items/wool.png");
        TAG_ICON.put(InputTag.CORN,             "items/corn.png");
        TAG_ICON.put(InputTag.SUNFLOWER_SEEDS,  "items/sunflower_seeds.png");
        TAG_ICON.put(InputTag.TRUFFLE,          "items/truffle.png");

        TAG_ICON.put(InputTag.WOOD,             "items/wood.png");
        TAG_ICON.put(InputTag.COPPER_ORE,       "items/copper_ore.png");
        TAG_ICON.put(InputTag.IRON_ORE,         "items/iron_ore.png");
        TAG_ICON.put(InputTag.GOLD_ORE,         "items/gold_ore.png");
        TAG_ICON.put(InputTag.IRIDIUM_ORE,      "items/iridium_ore.png");
    }

    /** اگر نگاشت TAG_ITEMS پر شده باشد، از همان استفاده می‌کنیم؛
     * در غیر این صورت، از تطبیق نام (heuristic) به عنوان fallback. */
    public static boolean itemHasTag(ItemType it, InputTag tag) {
        if (it == null || tag == null) return false;

        // 1) نگاشت صریح
        Set<ItemType> set = TAG_ITEMS.get(tag);
        if (set != null && !set.isEmpty()) {
            return set.contains(it);
        }

        // 2) fallback بر اساس id (اگر نگاشت نداده‌ای)
        String id = it.id().toLowerCase(Locale.ROOT);
        switch (tag) {
            case ANY_FRUIT:      return id.contains("fruit");
            case ANY_VEGETABLE:  return id.contains("vegetable") || id.equals("corn") || id.equals("wheat");
            case ANY_MUSHROOM:   return id.contains("mushroom");
            case ANY_FISH:       return id.contains("fish");
            case ANY_ORE:        return id.endsWith("_ore");
            case ANY_EGG:        return id.endsWith("_egg") && !id.contains("duck") && !id.contains("void") && !id.contains("dinosaur");
            case ANY_MILK:       return id.endsWith("_milk");

            case HOPS:             return id.equals("hops");
            case WHEAT:            return id.equals("wheat");
            case COFFEE_BEAN:      return id.equals("coffee_bean");
            case RICE:             return id.equals("rice");
            case HONEY:            return id.equals("honey");

            case MILK:            return id.equals("milk");
            case GOAT_MILK:       return id.equals("goat_milk");
            case DUCK_EGG:        return id.equals("duck_egg");
            case VOID_EGG:        return id.equals("void_egg");
            case DINO_EGG:        return id.equals("dinosaur_egg");

            case WOOL:            return id.equals("wool");
            case CORN:            return id.equals("corn");
            case SUNFLOWER_SEEDS: return id.equals("sunflower_seeds");
            case TRUFFLE:         return id.equals("truffle");

            case WOOD:            return id.equals("wood");
            case COPPER_ORE:      return id.equals("copper_ore");
            case IRON_ORE:        return id.equals("iron_ore");
            case GOLD_ORE:        return id.equals("gold_ore");
            case IRIDIUM_ORE:     return id.equals("iridium_ore");
        }
        return false;
    }

    // API
    public static List<ArtisanRecipe> all() { return ALL; }

    public static List<ArtisanRecipe> forMachine(MachineType m) {
        return BY_MACHINE.getOrDefault(m, Collections.emptyList());
    }

    /** جستجوی رسپی سازگار با دستگاه و آیتم ورودی (بدون بررسی helper). */
    public static ArtisanRecipe match(MachineType m, ItemType input) {
        for (ArtisanRecipe r : forMachine(m)) {
            if (r.input != null && r.input == input) return r;
            if (r.inputTag != null && itemHasTag(input, r.inputTag)) return r;
        }
        return null;
    }

    /** آیکن ورودی برای UI (وقتی آیتم واقعی نداریم، از آیکن تگ استفاده می‌شود). */
    public static Texture inputIcon(ArtisanRecipe r, ItemType loadedInputOrNull) {
        GameAssetManager gm = GameAssetManager.getGameAssetManager();
        if (loadedInputOrNull != null) return gm.getTexture(loadedInputOrNull.iconPath());
        if (r.input != null)           return gm.getTexture(r.input.iconPath());
        if (r.inputTag != null) {
            String p = (r.uiInputIconPath != null) ? r.uiInputIconPath : TAG_ICON.get(r.inputTag);
            return p != null ? gm.getTexture(p) : null;
        }
        return null;
    }

    /** آیکن خروجی (اگر ArtisanProductType باشد، از واریانت استفاده کن). */
    public static Texture outputIcon(ArtisanRecipe r) {
        GameAssetManager gm = GameAssetManager.getGameAssetManager();
        if (r.output instanceof ArtisanProductType apt) {
            return gm.getTexture(apt.iconPath(r.variantKey));
        }
        return gm.getTexture(r.output.iconPath());
    }

    // ===== تعریف رسپی‌ها =====
    static {
        // ---- Preserves Jar ----
        add(ArtisanRecipe.tagged(PRESERVE_JAR, InputTag.ANY_FRUIT,      1, JELLY,   1, 0.5f*MIN, 3*DAY,  "FRUIT", TAG_ICON.get(InputTag.ANY_FRUIT), "Any Fruit"));
        add(ArtisanRecipe.tagged(PRESERVE_JAR, InputTag.ANY_VEGETABLE,  1, PICKLES, 1, 0.5f*MIN, 6*HOUR, "VEG",   TAG_ICON.get(InputTag.ANY_VEGETABLE), "Any Vegetable"));

        // ---- Keg ----
        add(ArtisanRecipe.tagged(KEG, InputTag.ANY_FRUIT,     1, WINE,     1, MIN,   7*DAY,  "WINE",  TAG_ICON.get(InputTag.ANY_FRUIT), "Any Fruit"));
        add(ArtisanRecipe.tagged(KEG, InputTag.ANY_VEGETABLE, 1, JUICE,    1, MIN,   4*DAY,  "JUICE", TAG_ICON.get(InputTag.ANY_VEGETABLE), "Any Vegetable"));
        add(ArtisanRecipe.tagged(KEG, InputTag.HOPS,          1, PALE_ALE, 1, MIN,   3*DAY,  "PALE_ALE", TAG_ICON.get(InputTag.HOPS), "Hops"));
        add(ArtisanRecipe.tagged(KEG, InputTag.WHEAT,         1, BEER,     1, MIN,   1*DAY,  "BEER",  TAG_ICON.get(InputTag.WHEAT), "Wheat"));
        add(ArtisanRecipe.tagged(KEG, InputTag.COFFEE_BEAN,   5, COFFEE,   1, MIN,   2*HOUR, "COFFEE",TAG_ICON.get(InputTag.COFFEE_BEAN), "Coffee Beans (5)"));
        add(ArtisanRecipe.tagged(KEG, InputTag.RICE,          1, VINEGAR,  1, MIN,  10*HOUR, "VINEGAR",TAG_ICON.get(InputTag.RICE), "Rice"));
        add(ArtisanRecipe.tagged(KEG, InputTag.HONEY,         1, MEAD,     1, MIN,  10*HOUR, "MEAD",   TAG_ICON.get(InputTag.HONEY), "Honey"));

        // ---- Cheese Press ----
        add(ArtisanRecipe.tagged(CHEESE_PRESS, InputTag.MILK,      1, CHEESE,      1, MIN, 3*HOUR, "CHEESE",      TAG_ICON.get(InputTag.MILK), "Milk"));
        add(ArtisanRecipe.tagged(CHEESE_PRESS, InputTag.GOAT_MILK, 1, GOAT_CHEESE, 1, MIN, 3*HOUR, "GOAT_CHEESE", TAG_ICON.get(InputTag.GOAT_MILK), "Goat Milk"));

        // ---- Mayonnaise Machine ----
        add(ArtisanRecipe.tagged(MAYO_MACHINE, InputTag.ANY_EGG,  1, MAYONNAISE,     1, MIN, 3*HOUR, "MAYO",       TAG_ICON.get(InputTag.ANY_EGG), "Egg"));
        add(ArtisanRecipe.tagged(MAYO_MACHINE, InputTag.DUCK_EGG, 1, DUCK_MAYONNAISE,1, MIN, 3*HOUR, "DUCK_MAYO",  TAG_ICON.get(InputTag.DUCK_EGG), "Duck Egg"));
        add(ArtisanRecipe.tagged(MAYO_MACHINE, InputTag.VOID_EGG, 1, VOID_MAYONNAISE,1, MIN, 3*HOUR, "VOID_MAYO",  TAG_ICON.get(InputTag.VOID_EGG), "Void Egg"));
        add(ArtisanRecipe.tagged(MAYO_MACHINE, InputTag.DINO_EGG, 1, DINOSAUR_MAYO,  1, MIN, 3*HOUR, "DINO_MAYO",  TAG_ICON.get(InputTag.DINO_EGG), "Dinosaur Egg"));

        // ---- Loom ----
        add(ArtisanRecipe.tagged(LOOM, InputTag.WOOL, 1, CLOTH, 1, MIN, 4*HOUR, "CLOTH", TAG_ICON.get(InputTag.WOOL), "Wool"));

        // ---- Oil Maker ----
        add(ArtisanRecipe.tagged(OIL_MAKER, InputTag.CORN,             1, OIL,         1, MIN, 1*HOUR, "OIL",           TAG_ICON.get(InputTag.CORN), "Corn"));
        add(ArtisanRecipe.tagged(OIL_MAKER, InputTag.SUNFLOWER_SEEDS,  1, OIL,         1, MIN, 1*HOUR, "OIL",           TAG_ICON.get(InputTag.SUNFLOWER_SEEDS), "Sunflower Seeds"));
        add(ArtisanRecipe.tagged(OIL_MAKER, InputTag.TRUFFLE,          1, TRUFFLE_OIL, 1, MIN, 6*HOUR, "TRUFFLE_OIL",   TAG_ICON.get(InputTag.TRUFFLE), "Truffle"));

        // ---- Charcoal Kiln ---- (10 Wood -> 1 Coal)  ← خروجی MaterialType تا با اینونتوری هم‌نوع باشد
        add(ArtisanRecipe.tagged(CHARCOAL_KILN, InputTag.WOOD, 10,
                MaterialType.Coal, 1, MIN, 1*HOUR, "COAL", TAG_ICON.get(InputTag.WOOD), "Wood x10"));

        // ---- Furnace ---- (Ore x5 + 1 Coal -> Bar) — خروجی Bar اگر در MaterialType داری، از همون استفاده کن
        add(ArtisanRecipe.tagged2(FURNACE, InputTag.COPPER_ORE,  5,
                MaterialType.Coal, 1,
                /* اگر داری: */ MaterialType.CopperBar /*وگرنه ArtisanProductType.COPPER_BAR*/, 1,
                MIN, 4*HOUR, "COPPER_BAR", TAG_ICON.get(InputTag.COPPER_ORE), "Copper Ore x5"));

        add(ArtisanRecipe.tagged2(FURNACE, InputTag.IRON_ORE,    5,
                MaterialType.Coal, 1,
                /* اگر داری: */ MaterialType.IronBar /*وگرنه ArtisanProductType.IRON_BAR*/, 1,
                MIN, 4*HOUR, "IRON_BAR",   TAG_ICON.get(InputTag.IRON_ORE),   "Iron Ore x5"));

        add(ArtisanRecipe.tagged2(FURNACE, InputTag.GOLD_ORE,    5,
                MaterialType.Coal, 1,
                /* اگر داری: */ MaterialType.GoldBar /*وگرنه ArtisanProductType.GOLD_BAR*/, 1,
                MIN, 4*HOUR, "GOLD_BAR",   TAG_ICON.get(InputTag.GOLD_ORE),   "Gold Ore x5"));

        add(ArtisanRecipe.tagged2(FURNACE, InputTag.IRIDIUM_ORE, 5,
                MaterialType.Coal, 1,
                /* اگر داری: */ MaterialType.IridiumBar /*وگرنه ArtisanProductType.IRIDIUM_BAR*/, 1,
                MIN, 4*HOUR, "IRIDIUM_BAR", TAG_ICON.get(InputTag.IRIDIUM_ORE), "Iridium Ore x5"));

        // ---- Bee House ---- (بدون ورودی؛ خروجی دوره‌ای)
        add(ArtisanRecipe.fixed(BEE_HOUSE, null, 0, HONEY, 1, 0, 4*DAY, "HONEY"));

        index();
    }

    private static void add(ArtisanRecipe r) { ALL.add(r); }

    private static void index() {
        BY_MACHINE.clear();
        for (ArtisanRecipe r : ALL) {
            BY_MACHINE.computeIfAbsent(r.machine, k -> new ArrayList<>()).add(r);
        }
        for (List<ArtisanRecipe> list : BY_MACHINE.values()) {
            list.sort(Comparator.comparing(a -> a.output.id()));
        }
    }
}