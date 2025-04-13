package models.enums.Types;

import models.recipe.CraftingRecipe;
import models.recipe.Ingredient;

import java.util.List;

public enum CraftingRecipeType {
    CHERRY_BOMB(new CraftingRecipe(
            "Cherry Bomb",
            "هرچیز در شعاع ۳ تایی را نابود می‌کند",
            List.of(new Ingredient("Copper Ore", 4), new Ingredient("Coal", 1)),
            "Mining Level 1", 50
    )),

    BOMB(new CraftingRecipe(
            "Bomb",
            "هرچیز در شعاع ۵ تایی را نابود می‌کند",
            List.of(new Ingredient("Iron Ore", 4), new Ingredient("Coal", 1)),
            "Mining Level 2", 50
    )),

    MEGA_BOMB(new CraftingRecipe(
            "Mega Bomb",
            "هرچیز در شعاع ۷ تایی را نابود می‌کند",
            List.of(new Ingredient("Gold Ore", 4), new Ingredient("Coal", 1)),
            "Mining Level 3", 50
    )),

    SPRINKLER(new CraftingRecipe(
            "Sprinkler",
            "۴ تایل مجاور آب می‌دهد",
            List.of(new Ingredient("Copper Bar", 1), new Ingredient("Iron Bar", 1)),
            "Farming Level 1", 0
    )),

    QUALITY_SPRINKLER(new CraftingRecipe(
            "Quality Sprinkler",
            "به ۸ تایل مجاور آب می‌دهد",
            List.of(new Ingredient("Iron Bar", 1), new Ingredient("Gold Bar", 1)),
            "Farming Level 2", 0
    )),

    IRIDIUM_SPRINKLER(new CraftingRecipe(
            "Iridium Sprinkler",
            "به ۲۴ تایل مجاور آب می‌دهد",
            List.of(new Ingredient("Gold Bar", 1), new Ingredient("Iridium Bar", 1)),
            "Farming Level 3", 0
    )),

    CHARCOAL_KILN(new CraftingRecipe(
            "Charcoal Kiln",
            "۱۰ چوب را تبدیل به ۱ ذغال می‌کند",
            List.of(new Ingredient("Wood", 20), new Ingredient("Copper Bar", 2)),
            "Foraging Level 1", 0
    )),

    FURNACE(new CraftingRecipe(
            "Furnace",
            "کانی‌ها و ذغال را تبدیل به شمش می‌کند",
            List.of(new Ingredient("Copper Ore", 20), new Ingredient("Stone", 25)),
            "-", 0
    )),

    SCARECROW(new CraftingRecipe(
            "Scarecrow",
            "از حمله کلاغ‌ها تا شعاع ۸ تایی جلوگیری می‌کند",
            List.of(new Ingredient("Wood", 50), new Ingredient("Coal", 1), new Ingredient("Fiber", 20)),
            "-", 0
    )),

    DELUXE_SCARECROW(new CraftingRecipe(
            "Deluxe Scarecrow",
            "از حمله کلاغ‌ها تا شعاع ۱۲ تایی جلوگیری می‌کند",
            List.of(new Ingredient("Wood", 50), new Ingredient("Coal", 1), new Ingredient("Fiber", 20), new Ingredient("Iridium Ore", 1)),
            "Farming Level 2", 0
    )),

    BEE_HOUSE(new CraftingRecipe(
            "Bee House",
            "اگر در مزرعه گذاشته شود عسل تولید می‌کند",
            List.of(new Ingredient("Wood", 40), new Ingredient("Coal", 8), new Ingredient("Iron Bar", 1)),
            "Farming Level 1", 0
    )),

    CHEESE_PRESS(new CraftingRecipe(
            "Cheese Press",
            "شیر را به پنیر تبدیل می‌کند",
            List.of(new Ingredient("Wood", 45), new Ingredient("Stone", 45), new Ingredient("Copper Bar", 1)),
            "Farming Level 2", 0
    )),

    KEG(new CraftingRecipe(
            "Keg",
            "میوه و سبزیجات را به نوشیدنی تبدیل می‌کند",
            List.of(new Ingredient("Wood", 30), new Ingredient("Copper Bar", 1), new Ingredient("Iron Bar", 1)),
            "Farming Level 3", 0
    )),

    LOOM(new CraftingRecipe(
            "Loom",
            "پشم را به پارچه تبدیل می‌کند",
            List.of(new Ingredient("Wood", 60), new Ingredient("Fiber", 30)),
            "Farming Level 3", 0
    )),

    MAYONNAISE_MACHINE(new CraftingRecipe(
            "Mayonnaise Machine",
            "تخم مرغ را به سس مایونز تبدیل می‌کند",
            List.of(new Ingredient("Wood", 15), new Ingredient("Stone", 15), new Ingredient("Copper Bar", 1)),
            "-", 0
    )),

    OIL_MAKER(new CraftingRecipe(
            "Oil Maker",
            "را به روغن تبدیل می‌کند",
            List.of(new Ingredient("Wood", 100), new Ingredient("Gold Bar", 1), new Ingredient("Iron Bar", 1)),
            "Farming Level 3", 0
    )),

    PRESERVES_JAR(new CraftingRecipe(
            "Preserves Jar",
            "سبزیجات را به ترشی و میوه‌ها را به مربا تبدیل می‌کند",
            List.of(new Ingredient("Wood", 50), new Ingredient("Stone", 40), new Ingredient("Coal", 8)),
            "Farming Level 2", 0
    )),

    DEHYDRATOR(new CraftingRecipe(
            "Dehydrator",
            "میوه یا قارچ را خشک می‌کند",
            List.of(new Ingredient("Wood", 30), new Ingredient("Stone", 20), new Ingredient("Fiber", 30)),
            "Pierre's General Store", 0
    )),

    FISH_SMOKER(new CraftingRecipe(
            "Fish Smoker",
            "ماهی را با یک ذغال با حفظ کیفیتش تبدیل به ماهی دودی می‌کند",
            List.of(new Ingredient("Wood", 50), new Ingredient("Iron Bar", 3), new Ingredient("Coal", 10)),
            "Fish Shop", 0
    )),

    MYSTIC_TREE_SEED(new CraftingRecipe(
            "Mystic Tree Seed",
            "می‌تواند کاشته شود تا mystic tree رشد کند",
            List.of(
                    new Ingredient("Acorn", 5),
                    new Ingredient("Maple Seed", 5),
                    new Ingredient("Pine Cone", 5),
                    new Ingredient("Mahogany Seed", 5)
            ),
            "Foraging Level 4", 100
    ));

    private final CraftingRecipe recipe;

    CraftingRecipeType(CraftingRecipe recipe) {
        this.recipe = recipe;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }
}
