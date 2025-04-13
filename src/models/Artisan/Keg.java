package models.Artisan;

import models.recipe.Ingredient;

import java.util.List;

public class Keg {
    private final List<ArtisanRecipe> recipes = List.of(
            new ArtisanRecipe(
                    "Beer",
                    "Drink in moderation.",
                    50,
                    "1 Day",
                    List.of(
                            new RecipeOption(new Ingredient("Wheat", 1), 200)
                    )
            ),
            new ArtisanRecipe(
                    "Vinegar",
                    "An aged fermented liquid used in many cooking recipes.",
                    13,
                    "10 Hours",
                    List.of(
                            new RecipeOption(new Ingredient("Rice", 1), 100)
                    )
            ),
            new ArtisanRecipe(
                    "Coffee",
                    "It smells delicious. This is sure to give you a boost.",
                    75,
                    "2 Hours",
                    List.of(
                            new RecipeOption(new Ingredient("Coffee Bean", 5), 150)
                    )
            ),
            new ArtisanRecipe(
                    "Juice",
                    "A sweet, nutritious beverage.",
                    "2 × Base Ingredient Energy",
                    "4 Days",
                    List.of(
                            new RecipeOption(new Ingredient("Any Vegetable", 1), "2.25 × Ingredient Base Price")
                    )
            ),
            new ArtisanRecipe(
                    "Mead",
                    "A fermented beverage made from honey. Drink in moderation.",
                    100,
                    "10 Hours",
                    List.of(
                            new RecipeOption(new Ingredient("Honey", 1), 300)
                    )
            ),
            new ArtisanRecipe(
                    "Pale Ale",
                    "Drink in moderation.",
                    50,
                    "3 Days",
                    List.of(
                            new RecipeOption(new Ingredient("Hops", 1), 300)
                    )
            ),
            new ArtisanRecipe(
                    "Wine",
                    "Drink in moderation.",
                    "1.75 × Base Fruit Energy",
                    "7 Days",
                    List.of(
                            new RecipeOption(new Ingredient("Any Fruit", 1), "3 × Fruit Base Price")
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
