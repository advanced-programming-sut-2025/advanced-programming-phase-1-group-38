package io.github.StardewValley.models;

import java.util.Map;

public class CookingRecipe {
    private final String name;
    private final Map<ItemType, Integer> ingredients;
    private final ItemType result;

    public CookingRecipe(String name, Map<ItemType, Integer> ingredients, ItemType result) {
        this.name = name;
        this.ingredients = ingredients;
        this.result = result;
    }


    public String getName() {
        return name;
    }

    public Map<ItemType, Integer> getIngredients() {
        return ingredients;
    }

    public ItemType getResult() {
        return result;
    }
}
