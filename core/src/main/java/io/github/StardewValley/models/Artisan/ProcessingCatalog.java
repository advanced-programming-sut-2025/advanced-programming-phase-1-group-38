package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.ItemType;
import io.github.StardewValley.models.CropType;
import io.github.StardewValley.models.Artisan.ArtisanProductType;
import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.*;

import static io.github.StardewValley.models.Artisan.ArtisanRecipe.fixed;

/** کاتالوگ سادهٔ چند رسپی برای تست/بازی */
public final class ProcessingCatalog {

    // واحد زمان: ثانیهٔ بازی
    private static final float MIN  = 60f;
    private static final float HOUR = 60f * MIN;
    private static final float DAY  = 24f * HOUR;

    private static final Map<MachineType, List<ArtisanRecipe>> RECIPES = new HashMap<>();

    private static void add(ArtisanRecipe r) {
        RECIPES.computeIfAbsent(r.machine, k -> new ArrayList<>()).add(r);
    }

    static {
        // Keg — آبمیوه هویج (به‌عنوان سبزی) — برای تست سریع:
        // اگر زمان واقعی میخوای: work = 4*DAY (مثل دفترچه)
        add(fixed(
                MachineType.KEG, CropType.CARROT, 1,
                ArtisanProductType.JUICE, 1,
                1.5f * MIN, 30f * MIN, "ORANGE"
        ));

        // Preserves Jar — ترشی/پیکلز هویج — تست سریع
        add(fixed(
                MachineType.PRESERVE_JAR, CropType.CARROT, 1,
                ArtisanProductType.PICKLES, 1,
                1.0f * MIN, 25f * MIN, "ORANGE"
        ));

        // Cheese Press — پنیر (زمان واقعی: 3*HOUR)
        add(fixed(
                MachineType.CHEESE_PRESS, MaterialType.Milk, 1,
                ArtisanProductType.CHEESE, 1,
                0.5f * MIN, 3f * HOUR, null
        ));

        // Mayo Machine — مایونز (زمان واقعی: 3*HOUR)
        add(fixed(
                MachineType.MAYO_MACHINE, MaterialType.Egg, 1,
                ArtisanProductType.MAYONNAISE, 1,
                0.5f * MIN, 3f * HOUR, null
        ));

        // Loom — پارچه از پشم (زمان واقعی: 4*HOUR)
        add(fixed(
                MachineType.LOOM, MaterialType.Wool, 1,
                ArtisanProductType.CLOTH, 1,
                0.5f * MIN, 4f * HOUR, null
        ));

        // Oil Maker — روغن ترافل (زمان واقعی: 6*HOUR)
        add(fixed(
                MachineType.OIL_MAKER, MaterialType.Truffle, 1,
                ArtisanProductType.TRUFFLE_OIL, 1,
                1.0f * MIN, 6f * HOUR, null
        ));

        // Charcoal Kiln — چوب → زغال (❗️خروجی از نوع MaterialType تا با اینونتوری استک شود)
        add(fixed(
                MachineType.CHARCOAL_KILN, MaterialType.Wood, 10,
                MaterialType.Coal, 1,
                0.5f * MIN, 1f * HOUR, "COAL"
        ));
    }

    /** یافتن رسپی سازگار با دستگاه و ورودی مشخص (بدون بررسی helper). */
    public static ArtisanRecipe find(MachineType m, ItemType input, int qty) {
        List<ArtisanRecipe> list = RECIPES.get(m);
        if (list == null) return null;
        for (ArtisanRecipe r : list) {
            if (r.input == input && qty >= r.inputQty) return r;
        }
        return null;
    }

    public static List<ArtisanRecipe> list(MachineType m) {
        return RECIPES.getOrDefault(m, Collections.emptyList());
    }
}