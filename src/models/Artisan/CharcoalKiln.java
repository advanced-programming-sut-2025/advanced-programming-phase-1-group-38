package models.Artisan;

import models.recipe.Ingredient;

import java.util.List;

public class CharcoalKiln {
    private final List<ArtisanRecipe> recipes = List.of(
            new ArtisanRecipe(
                    "Coal",
                    "Turns 10 pieces of wood into one piece of coal.",
                    0,
                    "1 Hour",
                    List.of(
                            new RecipeOption(
                                    new Ingredient("Wood", 10),
                                    50
                            )
                    )
            )
    );

    public List<ArtisanRecipe> getRecipes() {
        return recipes;
    }

    public ArtisanRecipe getRecipeByName(String name) {
        return recipes.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
