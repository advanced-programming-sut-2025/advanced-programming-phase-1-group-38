package io.github.StardewValley.models.enums;

import io.github.StardewValley.models.ItemType;
import io.github.StardewValley.models.enums.Types.MaterialType;
import io.github.StardewValley.models.enums.Types.MaterialTypes;
import io.github.StardewValley.models.recipe.CraftingRecipe;
import io.github.StardewValley.models.Artisan.MachineType;

import java.util.HashMap;
import java.util.Map;

public enum CraftingRecipes implements MaterialTypes {
    // ───── Explosives (فعلاً اگر ItemType جدا ندارید، موقت بگذارید روی مواد تا بعداً بسازید) ─────
    CherryBombRecipe("Cherry Bomb Recipe", Map.of(
            MaterialType.CopperOre, 4,
            MaterialType.Coal, 1
    ), 50, MaterialType.CHERRY_BOMB /* TODO: Replace with Bomb ItemType */),

    BombRecipe("Bomb Recipe", Map.of(
            MaterialType.IronOre, 4,
            MaterialType.Coal, 1
    ), 50, MaterialType.BOMB /* TODO: Replace with Bomb ItemType */),

    MegaBombRecipe("Mega Bomb Recipe", Map.of(
            MaterialType.GoldOre, 4,
            MaterialType.Coal, 1
    ), 50, MaterialType.MEGA_BOMB /* TODO: Replace with Bomb ItemType */),

    // ───── Sprinklers (اگر ItemType جدا ندارید، بعداً اضافه کنید) ─────
    SprinklerRecipe("Sprinkler Recipe", Map.of(
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0, MaterialType.SPRINKLER /* TODO: Replace with Sprinkler ItemType */),

    QualitySprinklerRecipe("Quality Sprinkler Recipe", Map.of(
            MaterialType.IronBar, 1,
            MaterialType.GoldBar, 1
    ), 0, MaterialType.QUALITY_SPRINKLER /* TODO: Replace with Quality Sprinkler ItemType */),

    IridiumSprinklerRecipe("Iridium Sprinkler Recipe", Map.of(
            MaterialType.GoldBar, 1,
            MaterialType.IridiumBar, 1
    ), 0, MaterialType.IridiumBar /* TODO: Replace with Iridium Sprinkler ItemType */),

    // ───── Machines (محصول → MachineType) ─────
    CharcoalKilnRecipe("Charcoal Kiln Recipe", Map.of(
            MaterialType.Wood, 20,
            MaterialType.CopperBar, 2
    ), 0, MachineType.CHARCOAL_KILN),

    // Furnace اگر ItemType جدا دارید اینجا جایگزین کنید؛ در MachineType فعلاً نیست
    FurnaceRecipe("Furnace Recipe", Map.of(
            MaterialType.CopperOre, 20,
            MaterialType.Stone, 25
    ), 0, MaterialType.FURNACE /* TODO: Replace with Furnace ItemType */),

    // Scarecrows: اگر ItemType/Enum جدا دارید جایگزین کنید
    ScarecrowRecipe("Scarecrow Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20
    ), 0, MaterialType.SCARECROW /* TODO: Replace with Scarecrow ItemType */),

    DeluxeScarecrowRecipe("Deluxe Scarecrow Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20,
            MaterialType.IridiumOre, 1
    ), 0, MaterialType.DELUXE_SCARECROW /* TODO: Replace with Deluxe Scarecrow ItemType */),

    BeeHouseRecipe("Bee House Recipe", Map.of(
            MaterialType.Wood, 40,
            MaterialType.Coal, 8,
            MaterialType.IronBar, 1
    ), 0, MachineType.BEE_HOUSE),

    CheesePressRecipe("Cheese Press Recipe", Map.of(
            MaterialType.Wood, 45,
            MaterialType.Stone, 45,
            MaterialType.CopperBar, 1
    ), 0, MachineType.CHEESE_PRESS),

    KegRecipe("Keg Recipe", Map.of(
            MaterialType.Wood, 30,
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0, MachineType.KEG),

    LoomRecipe("Loom Recipe", Map.of(
            MaterialType.Wood, 60,
            MaterialType.Fiber, 30
    ), 0, MachineType.LOOM),

    MayonnaiseMachineRecipe("Mayonnaise Machine Recipe", Map.of(
            MaterialType.Wood, 15,
            MaterialType.Stone, 15,
            MaterialType.CopperBar, 1
    ), 0, MachineType.MAYO_MACHINE),

    OilMakerRecipe("Oil Maker Recipe", Map.of(
            MaterialType.Wood, 100,
            MaterialType.GoldBar, 1,
            MaterialType.IronBar, 1
    ), 0, MachineType.OIL_MAKER),

    PreservesJarRecipe("Preserves Jar Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Stone, 40,
            MaterialType.Coal, 8
    ), 0, MachineType.PRESERVE_JAR),

    DehydratorRecipe("Dehydrator Recipe", Map.of(
            MaterialType.Wood, 30,
            MaterialType.Stone, 20,
            MaterialType.Fiber, 30
    ), 0, MachineType.DEHYDRATOR),

    // Fish Smoker: اگر ItemType جدا دارید جایگزین کنید
    FishSmokerRecipe("Fish Smoker Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.IronBar, 3,
            MaterialType.Coal, 10
    ), 0, MaterialType.FISH_SMOKER /* TODO: Replace with Fish Smoker ItemType */),

    // تنها رسپی‌ای که واقعاً محصول MaterialType دارد
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
    private final ItemType product; // ← تغییر اصلی

    CraftingRecipes(String displayName, Map<MaterialType, Integer> ingredients, int price, ItemType product) {
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

    public CraftingRecipe getRecipe() {
        return null; // TODO: اگر سازنده‌ی CraftingRecipe را داری، اینجا بساز و برگردان
    }

    public ItemType getProduct() {   // ← خروجی حالا ItemType است
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