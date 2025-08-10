package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.CraftingRecipes;
import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Builds the crafting list directly from CraftingRecipes enum using your asset naming scheme. */
public class SimpleRecipeBook {
    private static final String ICON_BASE = "items/"; // پوشه‌ای که همه PNGها رو گذاشتی

    /** All recipes (you can add locking logic later if needed). */
    public static List<SimpleCraftingRecipe> getBasicRecipes() {
        List<SimpleCraftingRecipe> list = new ArrayList<>();

        for (CraftingRecipes cr : CraftingRecipes.values()) {
            // 1) name shown to players: drop trailing " Recipe"
            String display = dropRecipeSuffix(cr.getName()); // e.g., "Bee House"

            // 2) convert ingredients MaterialType→ItemType map
            Map<ItemType, Integer> ing = new LinkedHashMap<>();
            for (Map.Entry<MaterialType, Integer> e : cr.getIngredients().entrySet()) {
                ing.put((ItemType) e.getKey(), e.getValue()); // MaterialType implements ItemType (see below)
            }

            // 3) icon from the display name using your file list convention
            String iconPath = ICON_BASE + toIconFile(display); // e.g., "items/Bee_House.png"

            // 4) output is currently a MaterialType in your enum; later you can switch to Placeable
            ItemType output = (ItemType) cr.getProduct();

            list.add(new SimpleCraftingRecipe(display, ing, output, 1, iconPath));
        }
        return list;
    }

    private static String dropRecipeSuffix(String s) {
        String t = s == null ? "" : s.trim();
        if (t.endsWith(" Recipe")) t = t.substring(0, t.length() - " Recipe".length());
        return t;
    }

    /** Convert a display name like "Bee House" or "Hyper Speed-Gro" to your file names. */
    private static String toIconFile(String display) {
        // normalize whitespace
        String s = display.trim().replaceAll("\\s+", " ");

        // title-case words (handle hyphenated parts too)
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == 0 || s.charAt(i - 1) == ' ' || s.charAt(i - 1) == '-' ) {
                out.append(Character.toUpperCase(c));
            } else {
                out.append(c);
            }
        }
        // spaces→'_' , apostrophe→%27 (Tub o' Flowers), keep '-'
        String fileBase = out.toString()
                .replace(" ", "_")
                .replace("'", "%27");

        return fileBase + ".png";
    }
}