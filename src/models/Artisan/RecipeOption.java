package models.Artisan;

import models.recipe.Ingredient;

public class RecipeOption {
    private final ArtisanIngredient ingredient;
    private final int sellPrice;
    private final ArtisanProductType resultProductType;

    public RecipeOption(ArtisanIngredient ingredient, int sellPrice, ArtisanProductType resultProductType) {
        this.ingredient = ingredient;
        this.sellPrice = sellPrice;
        this.resultProductType = resultProductType;
    }

    public ArtisanIngredient getIngredient() {
        return ingredient;
    }
    public ArtisanProductType getResultProductType() {
        return resultProductType;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
