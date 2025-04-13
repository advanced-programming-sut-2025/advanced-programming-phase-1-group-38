package models.Artisan;

import models.recipe.Ingredient;

public class RecipeOption {
    private final Ingredient ingredient;
    private final int sellPrice;

    public RecipeOption(Ingredient ingredient, int sellPrice) {
        this.ingredient = ingredient;
        this.sellPrice = sellPrice;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
