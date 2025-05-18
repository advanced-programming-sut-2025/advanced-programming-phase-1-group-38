package models.Artisan;

import models.Item;
import models.Time;
import models.enums.Types.ItemType;
import models.farming.CropType;
import models.farming.FruitType;
import models.farming.TreeProductType;

import java.util.List;

public class Dehydrator implements ArtisanMachine {
    private ArtisanProcessingSlot slot;

    private final List<ArtisanRecipe> recipes = List.of(
        new ArtisanRecipe(
            ArtisanProductType.DRIED_MUSHROOMS.getName(),
            ArtisanProductType.DRIED_MUSHROOMS.getDescription(),
            "next morning",
            List.of(
                new RecipeOption(
                    new ArtisanIngredient(List.of(), CropType.COMMON_MUSHROOM, null, 5, CropType.COMMON_MUSHROOM.getBaseSellPrice()),
                    80,
                    ArtisanProductType.DRIED_MUSHROOMS
                ),
                new RecipeOption(
                    new ArtisanIngredient(List.of(), FruitType.COMMON_MUSHROOM, null, 5, FruitType.COMMON_MUSHROOM.getBaseSellPrice()),
                    80,
                    ArtisanProductType.DRIED_MUSHROOMS
                ),
                new RecipeOption(
                    new ArtisanIngredient(List.of(), CropType.RED_MUSHROOM, null, 5, CropType.RED_MUSHROOM.getBaseSellPrice()),
                    80,
                    ArtisanProductType.DRIED_MUSHROOMS
                ),
                new RecipeOption(
                    new ArtisanIngredient(List.of(), CropType.PURPLE_MUSHROOM, null, 5, CropType.PURPLE_MUSHROOM.getBaseSellPrice()),
                    80,
                    ArtisanProductType.DRIED_MUSHROOMS
                )
            ),
            null
        ),
        new ArtisanRecipe(
            ArtisanProductType.DRIED_FRUIT.getName(),
            ArtisanProductType.DRIED_FRUIT.getDescription(),
            "next morning",
            List.of(
                new RecipeOption(
                    new ArtisanIngredient(List.of(ItemType.FRUIT), null, null, 5, -1),
                    -1,
                    ArtisanProductType.DRIED_FRUIT
                )
            ),
            null
        ),
        new ArtisanRecipe(
            ArtisanProductType.RAISINS.getName(),
            ArtisanProductType.RAISINS.getDescription(),
            "next morning",
            List.of(
                new RecipeOption(
                    new ArtisanIngredient(List.of(), CropType.GRAPE, null, 5, CropType.GRAPE.getBaseSellPrice()),
                    600,
                    ArtisanProductType.RAISINS
                )
            ),
            null
        )
    );

    public List<ArtisanRecipe> getRecipes() {
        return recipes;
    }

    @Override
    public boolean startProcessing(List<Item> inputItems, Time time) {
        if (slot != null) return false;

        ArtisanRecipe recipe = findMatchingRecipe(inputItems);
        if (recipe == null) return false;

        slot = new ArtisanProcessingSlot(recipe, inputItems, time);
        return true;
    }

    @Override
    public ArtisanProduct collectProduct(Time time) {
        if (slot != null && slot.isReady(time)) {
            ArtisanProduct product = slot.collectProduct();
            slot = null;
            return product;
        }
        return null;
    }

    @Override
    public ArtisanMachineType getMachineType() {
        return ArtisanMachineType.DEHYDRATOR;
    }

    @Override public boolean isBusy() { return slot != null; }

    @Override public boolean isReady(Time time) { return slot != null && slot.isReady(time); }

    @Override
    public int getTimeRemaining(Time time) {
        return slot == null ? -1 : slot.getHoursRemaining(time);
    }

    @Override
    public ArtisanRecipe findMatchingRecipe(List<Item> inputItems) {
        for (ArtisanRecipe recipe : recipes) {
            if (recipe.getMatchingOption(inputItems) != null) {
                return recipe;
            }
        }
        return null;
    }
}
