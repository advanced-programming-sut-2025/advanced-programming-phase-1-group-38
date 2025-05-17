package models.recipe;

import models.enums.Types.CookingRecipeType;
import models.enums.Types.CraftingRecipeType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeRepository {
    private final List<CookingRecipe> cookingRecipes;

    private final List<CraftingRecipe> craftingRecipes;

    public RecipeRepository() {
        this.cookingRecipes = Arrays.stream(CookingRecipeType.values())
                .map(CookingRecipeType::getRecipe)
                .collect(Collectors.toList());

        this.craftingRecipes = Arrays.stream(CraftingRecipeType.values())
                .map(CraftingRecipeType::getRecipe)
                .collect(Collectors.toList());
    }

    public List<CookingRecipe> findAllCooking() {
        return cookingRecipes;
    }

    public CookingRecipe findCookingByName(String name) {
        return cookingRecipes.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<CraftingRecipe> findAllCrafting() {
        return craftingRecipes;
    }

    public CraftingRecipe findCraftingByName(String name) {
        return craftingRecipes.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Object findAnyRecipeByName(String name) {
        CookingRecipe c = findCookingByName(name);
        if (c != null) return c;
        CraftingRecipe cr = findCraftingByName(name);
        if (cr != null) return cr;
        return null;
    }
}
