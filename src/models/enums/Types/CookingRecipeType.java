package models.enums.Types;

import models.recipe.CookingRecipe;
import models.recipe.Ingredient;

import java.util.List;

public enum CookingRecipeType {
    FRIED_EGG(new CookingRecipe(
        "Fried Egg",
        "",
        List.of(new Ingredient("Egg", 1)),
        "Starter",
        35,
        50,
        0,
        ""
    )),

    BAKED_FISH(new CookingRecipe(
        "Baked Fish",
        "",
        List.of(
            new Ingredient("Sardine", 1),
            new Ingredient("Salmon", 1),
            new Ingredient("Wheat", 1)
        ),
        "Starter",
        100,
        75,
        0,
        ""
    )),

    SALAD(new CookingRecipe(
        "Salad",
        "",
        List.of(
            new Ingredient("Leek", 1),
            new Ingredient("Dandelion", 1)
        ),
        "Starter",
        110,
        113,
        0,
        ""
    )),

    OMELET(new CookingRecipe(
        "Omelet",
        "",
        List.of(
            new Ingredient("Egg", 1),
            new Ingredient("Milk", 1)
        ),
        "Stardrop Saloon",
        125,
        100,
        0,
        ""
    )),

    PUMPKIN_PIE(new CookingRecipe(
        "Pumpkin Pie",
        "",
        List.of(
            new Ingredient("Pumpkin", 1),
            new Ingredient("Wheat Flour", 1),
            new Ingredient("Milk", 1),
            new Ingredient("Sugar", 1)
        ),
        "Stardrop Saloon",
        385,
        225,
        0,
        ""
    )),

    SPAGHETTI(new CookingRecipe(
        "Spaghetti",
        "",
        List.of(
            new Ingredient("Wheat Flour", 1),
            new Ingredient("Tomato", 1)
        ),
        "Stardrop Saloon",
        120,
        75,
        0,
        ""
    )),

    PIZZA(new CookingRecipe(
        "Pizza",
        "",
        List.of(
            new Ingredient("Wheat Flour", 1),
            new Ingredient("Tomato", 1),
            new Ingredient("Cheese", 1)
        ),
        "Stardrop Saloon",
        300,
        150,
        0,
        ""
    )),

    TORTILLA(new CookingRecipe(
        "Tortilla",
        "",
        List.of(new Ingredient("Corn", 1)),
        "Stardrop Saloon",
        50,
        50,
        0,
        ""
    )),

    MAKI_ROLL(new CookingRecipe(
        "Maki Roll",
        "",
        List.of(
            new Ingredient("Any Fish", 1),
            new Ingredient("Rice", 1),
            new Ingredient("Fiber", 1)
        ),
        "Stardrop Saloon",
        220,
        100,
        0,
        ""
    )),

    TRIPLE_SHOT_ESPRESSO(new CookingRecipe(
        "Triple Shot Espresso",
        "",
        List.of(new Ingredient("Coffee", 3)),
        "Stardrop Saloon",
        450,
        200,
        5,
        "Max Energy + 100"
    )),

    COOKIE(new CookingRecipe(
        "Cookie",
        "",
        List.of(
            new Ingredient("Wheat Flour", 1),
            new Ingredient("Sugar", 1),
            new Ingredient("Egg", 1)
        ),
        "Stardrop Saloon",
        140,
        90,
        0,
        ""
    )),

    HASH_BROWNS(new CookingRecipe(
        "Hash Browns",
        "",
        List.of(
            new Ingredient("Potato", 1),
            new Ingredient("Oil", 1)
        ),
        "Stardrop Saloon",
        120,
        90,
        5,
        "Farming"
    )),

    PANCAKES(new CookingRecipe(
        "Pancakes",
        "",
        List.of(
            new Ingredient("Wheat Flour", 1),
            new Ingredient("Egg", 1)
        ),
        "Stardrop Saloon",
        80,
        90,
        11,
        "Foraging"
    )),

    FRUIT_SALAD(new CookingRecipe(
        "Fruit Salad",
        "",
        List.of(
            new Ingredient("Blueberry", 1),
            new Ingredient("Melon", 1),
            new Ingredient("Apricot", 1)
        ),
        "Stardrop Saloon",
        450,
        263,
        0,
        ""
    ));

    private final CookingRecipe recipe;

    CookingRecipeType(CookingRecipe recipe) {
        this.recipe = recipe;
    }

    public CookingRecipe getRecipe() {
        return recipe;
    }
}
