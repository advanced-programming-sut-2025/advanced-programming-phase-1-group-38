//package io.github.StardewValley.models.Artisan;
//
//import io.github.StardewValley.models.Item;
//
//import java.util.List;
//
//public class ArtisanRecipe {
//    private final String name;
//    private final String description;
//    private final String processingTime;
//    private final List<RecipeOption> options;
//    private final ArtisanProductType productType;
//
//    public ArtisanRecipe(String name, String description, String processingTime,
//                         List<RecipeOption> options, ArtisanProductType productType) {
//        this.name = name;
//        this.description = description;
//        this.processingTime = processingTime;
//        this.options = options;
//        this.productType = productType;
//    }
//
//    public String getName() { return name; }
//    public String getDescription() { return description; }
//    public String getProcessingTime() { return processingTime; }
//    public List<RecipeOption> getOptions() { return options; }
//    public ArtisanProductType getProductType() {
//        return productType;
//    }
//
//    public RecipeOption getMatchingOption(List<Item> items) {
//        for (RecipeOption option : options) {
//            if (option.getIngredient().matches(items)) {
//                return option;
//            }
//        }
//        return null;
//    }
//
//}
