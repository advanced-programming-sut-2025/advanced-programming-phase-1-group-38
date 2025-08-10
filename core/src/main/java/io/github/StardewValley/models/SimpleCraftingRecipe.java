package io.github.StardewValley.models;

import java.util.LinkedHashMap;
import java.util.Map;

/** A lightweight recipe model that works with your current Inventory. */
public class SimpleCraftingRecipe {
    private final String name;                      // e.g., "Bee House"
    private final Map<ItemType, Integer> ingredients; // inputs as ItemType
    private final ItemType output;                  // output as ItemType (can be MaterialType or Placeable later)
    private final int outputQty;
    private final String iconPath;                  // e.g., "items/Bee_House.png"

    public SimpleCraftingRecipe(String name,
                                Map<ItemType, Integer> ingredients,
                                ItemType output,
                                int outputQty,
                                String iconPath) {
        this.name = name;
        this.ingredients = new LinkedHashMap<>(ingredients);
        this.output = output;
        this.outputQty = outputQty;
        this.iconPath = iconPath;
    }

    public String getName() { return name; }
    public Map<ItemType, Integer> getIngredients() { return ingredients; }
    public ItemType getOutput() { return output; }
    public int getOutputQty() { return outputQty; }
    public String getIconPath() { return iconPath; }
}