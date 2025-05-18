package models.enums;

import models.enums.Types.MaterialType;
import models.enums.Types.MaterialTypes;

import java.util.HashMap;
import java.util.Map;

public enum CraftingRecipes implements MaterialTypes {
    CherryBombRecipe("Cherry Bomb Recipe", Map.of(
            MaterialType.CopperOre, 4,
            MaterialType.Coal, 1
    ), 50, MaterialType.CopperOre),

    BombRecipe("Bomb Recipe", Map.of(
            MaterialType.IronOre, 4,
            MaterialType.Coal, 1
    ), 50, MaterialType.IronOre),

    MegaBombRecipe("Mega Bomb Recipe", Map.of(
            MaterialType.GoldOre, 4,
            MaterialType.Coal, 1
    ), 50, MaterialType.GoldOre),

    SprinklerRecipe("Sprinkler Recipe", Map.of(
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0, MaterialType.CopperBar),

    QualitySprinklerRecipe("Quality Sprinkler Recipe", Map.of(
            MaterialType.IronBar, 1,
            MaterialType.GoldBar, 1
    ), 0, MaterialType.IronBar),

    IridiumSprinklerRecipe("Iridium Sprinkler Recipe", Map.of(
            MaterialType.GoldBar, 1,
            MaterialType.IridiumBar, 1
    ), 0, MaterialType.IridiumBar),

    CharcoalKilnRecipe("Charcoal Kiln Recipe", Map.of(
            MaterialType.Wood, 20,
            MaterialType.CopperBar, 2
    ), 0, MaterialType.CopperBar),

    FurnaceRecipe("Furnace Recipe", Map.of(
            MaterialType.CopperOre, 20,
            MaterialType.Stone, 25
    ), 0, MaterialType.CopperOre),

    ScarecrowRecipe("Scarecrow Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20
    ), 0, MaterialType.Wood),

    DeluxeScarecrowRecipe("Deluxe Scarecrow Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20,
            MaterialType.IridiumOre, 1
    ), 0, MaterialType.IridiumOre),

    BeeHouseRecipe("Bee House Recipe", Map.of(
            MaterialType.Wood, 40,
            MaterialType.Coal, 8,
            MaterialType.IronBar, 1
    ), 0, MaterialType.IronBar),

    CheesePressRecipe("Cheese Press Recipe", Map.of(
            MaterialType.Wood, 45,
            MaterialType.Stone, 45,
            MaterialType.CopperBar, 1
    ), 0, MaterialType.CopperBar),

    KegRecipe("Keg Recipe", Map.of(
            MaterialType.Wood, 30,
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0, MaterialType.IronBar),

    LoomRecipe("Loom Recipe", Map.of(
            MaterialType.Wood, 60,
            MaterialType.Fiber, 30
    ), 0, MaterialType.Fiber),

    MayonnaiseMachineRecipe("Mayonnaise Machine Recipe", Map.of(
            MaterialType.Wood, 15,
            MaterialType.Stone, 15,
            MaterialType.CopperBar, 1
    ), 0, MaterialType.CopperBar),

    OilMakerRecipe("Oil Maker Recipe", Map.of(
            MaterialType.Wood, 100,
            MaterialType.GoldBar, 1,
            MaterialType.IronBar, 1
    ), 0, MaterialType.GoldBar),

    PreservesJarRecipe("Preserves Jar Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Stone, 40,
            MaterialType.Coal, 8
    ), 0, MaterialType.Wood),

    DehydratorRecipe("Dehydrator Recipe", Map.of(
            MaterialType.Wood, 30,
            MaterialType.Stone, 20,
            MaterialType.Fiber, 30
    ), 0, MaterialType.Stone),

    FishSmokerRecipe("Fish Smoker Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.IronBar, 3,
            MaterialType.Coal, 10
    ), 0, MaterialType.IronBar),

    GrassStarterRecipe("Grass Starter Recipe", Map.of(
            MaterialType.Wood, 1,
            MaterialType.Fiber, 1
    ), 0, MaterialType.GrassStarter);


    // =====================
    // Fields and Constructor
    // =====================

    private final String displayName;
    private final Map<MaterialType, Integer> ingredients;
    private final int price;
    private final MaterialType product;

    CraftingRecipes(String displayName, Map<MaterialType, Integer> ingredients, int price, MaterialType product) {
        this.displayName = displayName;
        this.ingredients = new HashMap<>(ingredients);
        this.price = price;
        this.product = product;
    }

    // =====================
    // Getters
    // =====================

    public Map<MaterialType, Integer> getIngredients() {
        return this.ingredients;
    }

    public int getPrice() {
        return price;
    }

    public MaterialType getProduct() {
        return product;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.displayName;
    }
}