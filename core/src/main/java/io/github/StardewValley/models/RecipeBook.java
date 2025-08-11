// core/src/main/java/io/github/StardewValley/models/RecipeBook.java
package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Types.MaterialType;
import java.util.*;

public class RecipeBook {

    /** همهٔ رسپی‌هایی که فعلاً با آیتم‌های موجود پروژه سازگارن. */
    public static List<CookingRecipe> getAllRecipes() {
        List<CookingRecipe> list = new ArrayList<>();

        // Helper برای مرتب‌بودن مواد در UI
        java.util.function.BiFunction<Object[], Integer[], Map<ItemType,Integer>> M = (items, qtys) -> {
            Map<ItemType,Integer> m = new LinkedHashMap<>();
            for (int i=0;i<items.length;i++) m.put((ItemType) items[i], qtys[i]);
            return m;
        };

        // پایه‌ها
        list.add(new CookingRecipe(
                "Fried Egg",
                M.apply(new Object[]{ MaterialType.Egg }, new Integer[]{ 1 }),
                FoodType.FRIED_EGG
        ));

        list.add(new CookingRecipe(
                "Omelet",
                M.apply(new Object[]{ MaterialType.Egg, MaterialType.Milk }, new Integer[]{ 1, 1 }),
                FoodType.OMELET
        ));

        list.add(new CookingRecipe(
                "Pancakes",
                M.apply(new Object[]{ MaterialType.WheatFlour, MaterialType.Egg }, new Integer[]{ 1, 1 }),
                FoodType.PANCAKES
        ));

        list.add(new CookingRecipe(
                "Bread",
                M.apply(new Object[]{ MaterialType.WheatFlour }, new Integer[]{ 1 }),
                FoodType.BREAD
        ));

        list.add(new CookingRecipe(
                "Tortilla",
                M.apply(new Object[]{ CropType.CORN }, new Integer[]{ 1 }),
                FoodType.TORTILLA
        ));

        // پیتزا/اسپاگتی
        list.add(new CookingRecipe(
                "Spaghetti",
                M.apply(new Object[]{ MaterialType.WheatFlour, MaterialType.Tomato },
                        new Integer[]{ 1, 1 }),
                FoodType.SPAGHETTI
        ));

        list.add(new CookingRecipe(
                "Pizza",
                M.apply(new Object[]{ MaterialType.WheatFlour, MaterialType.Tomato, MaterialType.Cheese },
                        new Integer[]{ 1, 1, 1 }),
                FoodType.PIZZA
        ));

        // سالاد
        list.add(new CookingRecipe(
                "Salad",
                M.apply(new Object[]{ MaterialType.Leek, MaterialType.Dandelion },
                        new Integer[]{ 1, 1 }),
                FoodType.SALAD
        ));

        // ماهی
        list.add(new CookingRecipe(
                "Sashimi",
                M.apply(new Object[]{ MaterialType.Salmon }, new Integer[]{ 1 }),
                FoodType.SASHIMI
        ));

        list.add(new CookingRecipe(
                "Baked Fish",
                M.apply(new Object[]{ MaterialType.Sardine, MaterialType.Salmon, MaterialType.WheatFlour },
                        new Integer[]{ 1, 1, 1 }),
                FoodType.BAKED_FISH
        ));

        list.add(new CookingRecipe(
                "Maki Roll",
                M.apply(new Object[]{ MaterialType.Salmon, MaterialType.Rice, MaterialType.Fiber },
                        new Integer[]{ 1, 1, 1 }),
                FoodType.MAKI_ROLL
        ));

        // دسر/لبنیات
        list.add(new CookingRecipe(
                "Ice Cream",
                M.apply(new Object[]{ MaterialType.Milk, MaterialType.Sugar },
                        new Integer[]{ 1, 1 }),
                FoodType.ICE_CREAM
        ));

        list.add(new CookingRecipe(
                "Rice Pudding",
                M.apply(new Object[]{ MaterialType.Milk, MaterialType.Sugar, MaterialType.Rice },
                        new Integer[]{ 1, 1, 1 }),
                FoodType.RICE_PUDDING
        ));

        list.add(new CookingRecipe(
                "Cookie",
                M.apply(new Object[]{ MaterialType.WheatFlour, MaterialType.Sugar, MaterialType.Egg },
                        new Integer[]{ 1, 1, 1 }),
                FoodType.COOKIE
        ));

        // کدو و بادمجان (موادش رو در MaterialType داری)
        list.add(new CookingRecipe(
                "Pumpkin Pie",
                M.apply(new Object[]{ MaterialType.Pumpkin, MaterialType.WheatFlour, MaterialType.Milk, MaterialType.Sugar },
                        new Integer[]{ 1, 1, 1, 1 }),
                FoodType.PUMPKIN_PIE
        ));

        list.add(new CookingRecipe(
                "Pumpkin Soup",
                M.apply(new Object[]{ MaterialType.Pumpkin, MaterialType.Milk },
                        new Integer[]{ 1, 1 }),
                FoodType.PUMPKIN_SOUP
        ));

        list.add(new CookingRecipe(
                "Eggplant Parmesan",
                M.apply(new Object[]{ MaterialType.Eggplant, MaterialType.Tomato, MaterialType.Cheese },
                        new Integer[]{ 1, 1, 1 }),
                FoodType.EGGPLANT_PARMESAN
        ));

        // سیب‌زمینی سرخ‌کرده (Hashbrowns)
        list.add(new CookingRecipe(
                "Hashbrowns",
                M.apply(new Object[]{ MaterialType.Potato, MaterialType.Oil },
                        new Integer[]{ 1, 1 }),
                FoodType.HASHBROWNS
        ));

        // چند آیتم ساده با محصولات مزرعه فعلی‌ت (CARROT و CORN)
        list.add(new CookingRecipe(
                "Carrot Soup",
                M.apply(new Object[]{ CropType.CARROT, CropType.CORN }, new Integer[]{ 3, 2 }),
                // اگر FoodType خاصی برای سوپ هویج نداری، یکی از غذاهای آماده رو جایگزین کن
                FoodType.SALAD // یا FoodType.BREAD … بنا به سلیقه
        ));

        list.add(new CookingRecipe(
                "Grilled Corn",
                M.apply(new Object[]{ CropType.CORN }, new Integer[]{ 3 }),
                FoodType.TORTILLA // نزدیک‌ترین خروجی (اگر آیکن اختصاصی نداری)
        ));

        return list;
    }
}