//package io.github.StardewValley.models.Artisan;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.Time;
//import io.github.StardewValley.models.enums.Types.AnimalProductType;
//
//import java.util.List;
//
//public class CheesePress implements ArtisanMachine {
//    private ArtisanProcessingSlot slot = null;
//
//    private final List<ArtisanRecipe> recipes = List.of(
//        new ArtisanRecipe(
//            ArtisanProductType.CHEESE.getName(),
//            ArtisanProductType.CHEESE.getDescription(),
//            "3 Hours",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), AnimalProductType.COW_MILK, null, 1, 230),
//                    230,
//                    ArtisanProductType.CHEESE
//                ),
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), AnimalProductType.LARGE_COW_MILK, null, 1, 345),
//                    345,
//                    ArtisanProductType.LARGE_CHEESE
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.GOAT_CHEESE.getName(),
//            ArtisanProductType.GOAT_CHEESE.getDescription(),
//            "3 Hours",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), AnimalProductType.GOAT_MILK, null, 1, 400),
//                    400,
//                    ArtisanProductType.GOAT_CHEESE
//                ),
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), AnimalProductType.LARGE_GOAT_MILK, null, 1, 600),
//                    600,
//                    ArtisanProductType.LARGE_GOAT_CHEESE
//                )
//            ),
//            null
//        )
//    );
//
//    public List<ArtisanRecipe> getRecipes() {
//        return recipes;
//    }
//
//    @Override
//    public ArtisanMachineType getMachineType() {
//        return ArtisanMachineType.CHEESE_PRESS;
//    }
//
//    @Override
//    public boolean startProcessing(List<Item> inputItems, Time time) {
//        if (slot != null) return false;
//
//        ArtisanRecipe recipe = findMatchingRecipe(inputItems);
//        if (recipe == null) return false;
//
//        slot = new ArtisanProcessingSlot(recipe, inputItems, time);
//        return true;
//    }
//
//    @Override
//    public ArtisanProduct collectProduct(Time time) {
//        if (slot != null && slot.isReady(time)) {
//            ArtisanProduct product = slot.collectProduct();
//            slot = null;
//            return product;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isBusy() {
//        return slot != null;
//    }
//
//    @Override
//    public boolean isReady(Time time) {
//        return slot != null && slot.isReady(time);
//    }
//
//    @Override
//    public int getTimeRemaining(Time time) {
//        return slot == null ? -1 : slot.getHoursRemaining(time);
//    }
//
//    @Override
//    public ArtisanRecipe findMatchingRecipe(List<Item> inputItems) {
//        for (ArtisanRecipe recipe : recipes) {
//            if (recipe.getMatchingOption(inputItems) != null) {
//                return recipe;
//            }
//        }
//        return null;
//    }
//}
