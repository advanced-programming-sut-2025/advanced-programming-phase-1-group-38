package models.recipe;

import java.util.List;

public class CraftingRecipe extends Recipe {
    public CraftingRecipe(String name, String description, List<Ingredient> ingredients, String source, int sellPrice) {
        super(name, description, ingredients, source, sellPrice);
    }

    @Override
    public String getRecipeType() {
        return "Crafting";
    }
}
