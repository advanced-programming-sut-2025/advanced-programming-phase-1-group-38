//package io.github.StardewValley.models.Artisan;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.Time;
//
//import java.util.List;
//
//public class BeeHouse implements ArtisanMachine {
//    private ArtisanProcessingSlot slot;
//
//    private final List<ArtisanRecipe> recipes = List.of(
//        new ArtisanRecipe(
//            ArtisanProductType.HONEY.getName(),
//            ArtisanProductType.HONEY.getDescription(),
//            "4 Days",
//            List.of(),
//            ArtisanProductType.HONEY
//        )
//    );
//
//    public List<ArtisanRecipe> getRecipes() {
//        return recipes;
//    }
//
//    public ArtisanRecipe getRecipeByName(String name) {
//        return recipes.stream()
//            .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
//            .findFirst()
//            .orElse(null);
//    }
//
//    @Override
//    public ArtisanMachineType getMachineType() {
//        return ArtisanMachineType.BEE_HOUSE;
//    }
//
//
//    @Override
//    public boolean startProcessing(List<Item> inputItems, Time time) {
//        if (slot != null) return false;
//        slot = new ArtisanProcessingSlot(recipes.get(0), List.of(), time);
//        return true;
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
//    public ArtisanProduct collectProduct(Time time) {
//        if (isReady(time)) {
//            ArtisanProduct product = slot.collectProduct();
//            slot = null;
//            startProcessing(List.of(), time);
//            return product;
//        }
//        return null;
//    }
//
//    @Override
//    public ArtisanRecipe findMatchingRecipe(List<Item> inputItems) {
//        return recipes.get(0);
//    }
//
//    @Override
//    public int getTimeRemaining(Time time) {
//        return slot == null ? -1 : slot.getHoursRemaining(time);
//    }
//
//    public String getStatus(Time time) {
//        if (slot == null) return "Idle";
//        if (slot.isReady(time)) return "Honey ready!";
//        return "Processing... " + slot.getHoursRemaining(time) + "h remaining";
//    }
//}
