//package io.github.StardewValley.models.enums.Types;
//
//import java.util.Arrays;
//import java.util.List;
//
//public enum AnimalType {
//    CHICKEN(Arrays.asList(AnimalProductType.CHICKEN_EGG, AnimalProductType.LARGE_CHICKEN_EGG), true, 800, 1),
//    DUCK(Arrays.asList(AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHER), true, 1200, 2),
//    RABBIT(Arrays.asList(AnimalProductType.RABBIT_WOOL, AnimalProductType.RABBIT_FOOT), true, 8000, 4),
//    DINOSAUR(Arrays.asList(AnimalProductType.DINOSAUR_EGG), true, 14000, 7),
//
//    COW(Arrays.asList(AnimalProductType.COW_MILK, AnimalProductType.LARGE_COW_MILK), false, 1500, 1),
//    GOAT(Arrays.asList(AnimalProductType.GOAT_MILK, AnimalProductType.LARGE_GOAT_MILK), false, 4000, 2),
//    SHEEP(Arrays.asList(AnimalProductType.WOOL), false, 8000, 3),
//    PIG(Arrays.asList(AnimalProductType.TRUFFLE), false, 16000, 1);
//
//    private final List<AnimalProductType> animalProducts;
//    private final boolean livesInCage;
//    private final int price;
//    private final int daysToProduce;
//
//    AnimalType(List<AnimalProductType> animalProducts, boolean livesInCage, int price, int daysToProduce) {
//        this.animalProducts = animalProducts;
//        this.livesInCage = livesInCage;
//        this.price = price;
//        this.daysToProduce = daysToProduce;
//    }
//
//    public List<AnimalProductType> getAnimalProducts() {
//        return animalProducts;
//    }
//
//    public boolean isLivesInCage() {
//        return livesInCage;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public int getDaysToProduce() {
//        return daysToProduce;
//    }
//
//    public List<FarmBuildingType> getLivingSpaceTypes() {
//        return livesInCage
//                ? List.of(FarmBuildingType.COOP, FarmBuildingType.BIG_COOP, FarmBuildingType.DELUXE_COOP)
//                : List.of(FarmBuildingType.BARN, FarmBuildingType.BIG_BARN, FarmBuildingType.DELUXE_BARN);
//    }
//
//    public String getName() {
//        return name();
//    }
//
//    public static AnimalType getAnimalTypeByName(String name) {
//        for (AnimalType type : AnimalType.values()) {
//            if (type.name().equalsIgnoreCase(name)) {
//                return type;
//            }
//        }
//        return null;
//    }
//}
