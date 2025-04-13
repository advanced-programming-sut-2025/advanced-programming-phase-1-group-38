package models.Artisan;

import models.recipe.Ingredient;

import java.util.List;

public class CheesePress {
    private final List<ArtisanRecipe> recipes = List.of(
            new ArtisanRecipe(
                    "Cheese",
                    "It's your basic cheese.",
                    100,
                    "3 Hours",
                    List.of(
                            new RecipeOption(new Ingredient("Milk", 1), 230),
                            new RecipeOption(new Ingredient("Large Milk", 1), 345)
                    )
            ),
            new ArtisanRecipe(
                    "Goat Cheese",
                    "Soft cheese made from goat's milk.",
                    100,
                    "3 Hours",
                    List.of(
                            new RecipeOption(new Ingredient("Goat Milk", 1), 400),
                            new RecipeOption(new Ingredient("Large Goat Milk", 1), 600)
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
