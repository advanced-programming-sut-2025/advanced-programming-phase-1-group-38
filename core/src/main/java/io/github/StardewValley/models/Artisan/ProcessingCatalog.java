package io.github.StardewValley.models.Artisan;


import io.github.StardewValley.models.*;
import io.github.StardewValley.models.Artisan.ArtisanProductType;
import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.*;

public final class ProcessingCatalog {
    private static final Map<MachineType, List<ArtisanRecipe>> RECIPES = new HashMap<>();

    private static void add(ArtisanRecipe r) {
        RECIPES.computeIfAbsent(r.machine, k -> new ArrayList<>()).add(r);
    }

    static {
        // Keg — آبمیوه از هویج (به‌عنوان سبزی) برای تست
        add(new ArtisanRecipe(MachineType.KEG, CropType.CARROT, 1,
                ArtisanProductType.JUICE, 1, 1.5f, 30f, "ORANGE"));

        // Preserves Jar — ترشی هویج
        add(new ArtisanRecipe(MachineType.PRESERVE_JAR, CropType.CARROT, 1,
                ArtisanProductType.PICKLES, 1, 1.0f, 25f, "ORANGE"));

        // Cheese Press — پنیـر
        add(new ArtisanRecipe(MachineType.CHEESE_PRESS, MaterialType.Milk, 1,
                ArtisanProductType.CHEESE, 1, 0.5f, 20f, null));

        // Mayo Machine — مایونز
        add(new ArtisanRecipe(MachineType.MAYO_MACHINE, MaterialType.Egg, 1,
                ArtisanProductType.MAYONNAISE, 1, 0.5f, 12f, null));

        // Loom — پارچه از پشم
        add(new ArtisanRecipe(MachineType.LOOM, MaterialType.Wool, 1,
                ArtisanProductType.CLOTH, 1, 0.5f, 20f, null));

        // Oil Maker — روغن ترافل
        add(new ArtisanRecipe(MachineType.OIL_MAKER, MaterialType.Truffle, 1,
                ArtisanProductType.TRUFFLE_OIL, 1, 1.0f, 45f, null));

// Charcoal Kiln — چوب → زغال
//        add(new ArtisanRecipe(
//                MachineType.CHARCOAL_KILN,
//                MaterialType.Wood, 10,
//                MaterialType.Coal, 1,   // ⟵ قبلاً اشتباهی ArtisanProductType.COAL بود
//                0.3f, 8f,
//                null
//        ));
    }

    public static ArtisanRecipe find(MachineType m, ItemType input, int qty) {
        List<ArtisanRecipe> list = RECIPES.get(m);
        if (list == null) return null;
        for (ArtisanRecipe r : list) {
            if (r.input == input && qty >= r.inputQty) return r;
        }
        return null;
    }

    public static List<ArtisanRecipe> list(MachineType m) {
        return RECIPES.getOrDefault(m, Collections.<ArtisanRecipe>emptyList());
    }
}