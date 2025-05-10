package models.Artisan;

import models.recipe.Ingredient;

import java.util.List;

public class Dehydrator {
    private final List<ArtisanRecipe> recipes = List.of(
            new ArtisanRecipe(
                    "Dried Mushrooms",
                    "A package of gourmet mushrooms.",
                    50,
                    "Ready the next morning",
                    List.of(
                            new RecipeOption(
                                    new Ingredient("Any Mushroom", 5),
                                    "7.5 × Mushroom Base Price + 25g"
                            )
                    )
            ),
            new ArtisanRecipe(
                    "Dried Fruit",
                    "Chewy pieces of dried fruit.",
                    "75",
                    "Ready the next morning",
                    List.of(
                            new RecipeOption(
                                    new Ingredient("5 of Any Fruit (except Grapes)", 5),
                                    "7.5 × Fruit Base Price + 25g"
                            )
                    )
            ),
            new ArtisanRecipe(
                    "Raisins",
                    "It's said to be the Junimos' favorite food.",
                    125,
                    "Ready the next morning",
                    List.of(
                            new RecipeOption(
                                    new Ingredient("Grapes", 5),
                                    600
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
