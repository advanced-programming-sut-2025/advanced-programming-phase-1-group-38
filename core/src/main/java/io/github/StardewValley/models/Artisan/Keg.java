//package io.github.StardewValley.models.Artisan;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.Time;
//import io.github.StardewValley.models.CropType;
//
//import java.util.List;
//
//
//public class Keg implements ArtisanMachine {
//    private ArtisanProcessingSlot slot;
//
//    private final List<ArtisanRecipe> recipes = List.of(
//        new ArtisanRecipe(
//            ArtisanProductType.BEER.getName(),
//            ArtisanProductType.BEER.getDescription(),
//            "1 Day",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), CropType.WHEAT, null, 1, CropType.WHEAT.getBaseSellPrice()),
//                    200,
//                    ArtisanProductType.BEER
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.VINEGAR.getName(),
//            ArtisanProductType.VINEGAR.getDescription(),
//            "10 Hours",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), CropType.UNMILLED_RICE, null, 1, CropType.UNMILLED_RICE.getBaseSellPrice()),
//                    100,
//                    ArtisanProductType.VINEGAR
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.COFFEE.getName(),
//            ArtisanProductType.COFFEE.getDescription(),
//            "2 Hours",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), CropType.COFFEE_BEAN, null, 5, CropType.COFFEE_BEAN.getBaseSellPrice()),
//                    150,
//                    ArtisanProductType.COFFEE
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.JUICE.getName(),
//            ArtisanProductType.JUICE.getDescription(),
//            "4 Days",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(ItemType.CROP), null, null, 1, -1),
//                    -1,
//                    ArtisanProductType.JUICE
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.MEAD.getName(),
//            ArtisanProductType.MEAD.getDescription(),
//            "10 Hours",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), ArtisanProductType.HONEY, null, 1, ArtisanProductType.HONEY.getSellPrice()),
//                    300,
//                    ArtisanProductType.MEAD
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.PALE_ALE.getName(),
//            ArtisanProductType.PALE_ALE.getDescription(),
//            "3 Days",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(), CropType.HOPS, null, 1, CropType.HOPS.getBaseSellPrice()),
//                    300,
//                    ArtisanProductType.PALE_ALE
//                )
//            ),
//            null
//        ),
//        new ArtisanRecipe(
//            ArtisanProductType.WINE.getName(),
//            ArtisanProductType.WINE.getDescription(),
//            "7 Days",
//            List.of(
//                new RecipeOption(
//                    new ArtisanIngredient(List.of(ItemType.FRUIT), null, null, 1, -1),
//                    -1,
//                    ArtisanProductType.WINE
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
//    public ArtisanRecipe getRecipeByName(String name) {
//        return recipes.stream()
//            .filter(r -> r.getName().equalsIgnoreCase(name))
//            .findFirst()
//            .orElse(null);
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
//    public ArtisanMachineType getMachineType() {
//        return ArtisanMachineType.KEG;
//    }
//
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
