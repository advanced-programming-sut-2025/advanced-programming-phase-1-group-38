//package io.github.StardewValley.models.Artisan;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.Time;
//import io.github.StardewValley.models.farming.Crop;
//import io.github.StardewValley.models.farming.Fruit;
//
//import java.util.List;
//
//public class ArtisanProcessingSlot {
//    private final ArtisanRecipe recipe;
//    private final List<Item> inputItems;
//    private final RecipeOption usedOption;
//    private final int startHour;
//    private final int requiredHours;
//    private final int startDay;
//
//    public ArtisanProcessingSlot(ArtisanRecipe recipe, List<Item> inputItems, Time time) {
//        this.recipe = recipe;
//        this.inputItems = inputItems;
//        this.usedOption = recipe.getMatchingOption(inputItems);
//        this.startHour = (int) time.getHour();
//        this.startDay = time.getDayOfYear();
//        this.requiredHours = parseDurationToHours(recipe.getProcessingTime());
//    }
//
//    public boolean isReady(Time currentTime) {
//        int currentHour = (int) currentTime.getHour();
//        int currentDay = currentTime.getDayOfYear();
//
//        if (requiredHours == -999) {
//            return currentDay > startDay;
//        }
//
//        return currentHour - startHour >= requiredHours;
//    }
//
//    private int getEnergy(Item item) {
//        if (item instanceof Crop crop) return crop.getEnergy();
//        if (item instanceof Fruit fruit) return fruit.getEnergy();
//        return 0;
//    }
//
//    public ArtisanProduct collectProduct() {
//        ArtisanProductType type = (usedOption != null)
//            ? usedOption.getResultProductType()
//            : recipe.getProductType();
//
//        int finalPrice = (usedOption != null) ? usedOption.getSellPrice() : type.getSellPrice();
//        int finalEnergy = type.getEnergy();
//
//        if (finalPrice == -1) {
//            Item main = getMainIngredientFor(type);
//            if (main != null) {
//                switch (type) {
//                    case JUICE -> finalPrice = (int) (main.getPrice() * 2.25);
//                    case WINE -> finalPrice = main.getPrice() * 3;
//                    default -> finalPrice = inputItems.stream().mapToInt(Item::getPrice).sum();
//                }
//            }
//        }
//
//        if (finalEnergy == -1) {
//            Item main = getMainIngredientFor(type);
//            if (main != null) {
//                switch (type) {
//                    case JUICE -> finalEnergy = getEnergy(main) * 2;
//                    case WINE -> finalEnergy = (int) (getEnergy(main) * 1.75);
//                    default -> finalEnergy = inputItems.stream()
//                        .mapToInt(this::getEnergy)
//                        .sum();
//                }
//            }
//        }
//
//        return new ArtisanProduct(type, finalPrice, finalEnergy);
//    }
//
//    public int getHoursRemaining(Time currentTime) {
//        int currentHour = (int) currentTime.getHour();
//        int currentDay = currentTime.getDayOfYear();
//
//        if (requiredHours == -999) {
//            return (currentDay > startDay) ? 0 : (24 - (currentHour - startHour) % 24);
//        }
//
//        return Math.max(0, (startHour + requiredHours) - currentHour);
//    }
//
//    public ArtisanRecipe getRecipe() {
//        return recipe;
//    }
//
//    private int parseDurationToHours(String input) {
//        input = input.trim().toLowerCase();
//        if (input.contains("next morning")) {
//            return -999;
//        } else if (input.contains("day")) {
//            return Integer.parseInt(input.split(" ")[0]) * 24;
//        } else if (input.contains("hour")) {
//            return Integer.parseInt(input.split(" ")[0]);
//        }
//        return 24;
//    }
//
//
//    private Item getMainIngredientFor(ArtisanProductType type) {
//        return switch (type) {
//            case JUICE -> inputItems.stream()
//                .filter(i -> i.getType() == ItemType.CROP)
//                .findFirst().orElse(null);
//
//            case WINE -> inputItems.stream()
//                .filter(i -> i.getType() == ItemType.FRUIT)
//                .findFirst().orElse(null);
//
//            default -> inputItems.isEmpty() ? null : inputItems.get(0);
//        };
//    }
//}
