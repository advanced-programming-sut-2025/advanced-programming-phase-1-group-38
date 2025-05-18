package models.Artisan;

import models.Item;
import models.Time;
import models.enums.Types.ItemType;

import java.util.List;

public class CharcoalKiln implements ArtisanMachine {
    private ArtisanProcessingSlot slot;

    private final List<ArtisanRecipe> recipes = List.of(
        new ArtisanRecipe(
            ArtisanProductType.COAL.getName(),
            ArtisanProductType.COAL.getDescription(),
            "1 Hour",
            List.of(
                new RecipeOption(
                    new ArtisanIngredient(
                        List.of(ItemType.WOOD),
                        null,
                        null,
                        10,
                        -1
                    ),
                    50,
                    ArtisanProductType.COAL
                )
            ),
            null
        )
    );

    public List<ArtisanRecipe> getRecipes() {
        return recipes;
    }

    public ArtisanRecipe getRecipeByName(String name) {
        return recipes.stream()
            .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    @Override
    public ArtisanMachineType getMachineType() {
        return ArtisanMachineType.CHARCOAL_KILN;
    }


    @Override
    public boolean isBusy() {
        return slot != null;
    }

    @Override
    public boolean isReady(Time time) {
        return slot != null && slot.isReady(time);
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
        if (isReady(time)) {
            ArtisanProduct product = slot.collectProduct();
            slot = null;
            return product;
        }
        return null;
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

    @Override
    public int getTimeRemaining(Time time) {
        return slot == null ? -1 : slot.getHoursRemaining(time);
    }
}
