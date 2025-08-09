package io.github.StardewValley.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeBook {
    public static List<CookingRecipe> getAllRecipes() {
        List<CookingRecipe> list = new ArrayList<>();

        Map<ItemType, Integer> ingredients = new HashMap<>();
        ingredients.put(CropType.CARROT, 3);
        ingredients.put(CropType.CORN, 2);
        list.add(new CookingRecipe("Carrot Soup", ingredients, FoodType.CARROT_SOUP));

        Map<ItemType, Integer> ingredients2 = new HashMap<>();
        ingredients2.put(CropType.CORN, 4);
        list.add(new CookingRecipe("Grilled Corn", ingredients2, FoodType.GRILLED_CORN));

        Map<ItemType, Integer> ingredients3 = new HashMap<>();
        ingredients2.put(CropType.CORN, 4);
        list.add(new CookingRecipe("Grilled Corn", ingredients3, FoodType.GRILLED_CORN));

        Map<ItemType, Integer> ingredients4 = new HashMap<>();
        ingredients2.put(CropType.CORN, 4);
        list.add(new CookingRecipe("Grilled Corn", ingredients4, FoodType.GRILLED_CORN));

        Map<ItemType, Integer> ingredients5 = new HashMap<>();
        ingredients2.put(CropType.CORN, 4);
        list.add(new CookingRecipe("Grilled Corn", ingredients5, FoodType.GRILLED_CORN));

        Map<ItemType, Integer> ingredients6 = new HashMap<>();
        ingredients2.put(CropType.CORN, 4);
        list.add(new CookingRecipe("Grilled Corn", ingredients6, FoodType.GRILLED_CORN));

        Map<ItemType, Integer> ingredients7 = new HashMap<>();
        ingredients2.put(CropType.CORN, 4);
        list.add(new CookingRecipe("Grilled Corn", ingredients7, FoodType.GRILLED_CORN));

        return list;
    }
}
