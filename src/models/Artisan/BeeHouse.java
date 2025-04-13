package models.Artisan;

import models.recipe.Ingredient;

import java.util.List;

public class BeeHouse {
    private final List<ArtisanRecipe> recipes = List.of(
            new ArtisanRecipe(
                    "Honey",
                    "It's a sweet syrup produced by bees.",
                    75,
                    "4 Days",
                    List.of(
                        new RecipeOption(
                            new Ingredient("",0),
                            350
                        )
                    )
            )
    );

    public List<ArtisanRecipe> getRecipes() {
        return recipes;
    }

    public ArtisanRecipe getRecipeByName(String name) {
        return recipes.stream()
                .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
