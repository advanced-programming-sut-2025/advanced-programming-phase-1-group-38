//package io.github.StardewValley.models.enums.Shop;
//
//import io.github.StardewValley.models.enums.Types.AnimalType;
//import io.github.StardewValley.models.enums.Types.MaterialType;
//import io.github.StardewValley.models.enums.Types.MaterialTypes;
//
//public enum MarniesRanch implements ShopEntry{
//
//    // Shop
//    Hay(MaterialType.Hay, null, "Hay",
//            "Dried grass used as animal food.",
//            50, Integer.MAX_VALUE, null),
//    MilkPail(MaterialType.MilkPail, null, "Milk Pail",
//            "Gather milk from your animals.",
//            1000, 1, null),
//    Shears(MaterialType.Shears, null, "Shears",
//            "Use this to collect wool from sheep",
//            1000, 1, null),
//
//    // PurchasedAnimal
//    Chicken(null ,AnimalType.CHICKEN, "Chicken",
//            "Well cared-for chickens lay eggs every day. Lives in the coop.", 800, 2, "Coop"),
//    Cow(null ,AnimalType.COW, "Cow",
//            "Can be milked daily. A milk pail is required to harvest the milk. Lives in the barn.",
//            1500, 2, "Barn"),
//    Goat(null ,AnimalType.GOAT, "Goat",
//            "Happy provide goat milk every other day. A milk pail is required to harvest the milk. Lives in the barn.",
//            4000, 2, "Big Barn"),
//    Duck(null ,AnimalType.DUCK, "Duck",
//            "Happy lay duck eggs every other day. Lives in the coop.",
//            1200, 2, "Big Coop"),
//    Sheep(null ,AnimalType.SHEEP, "Sheep",
//            "Can be shorn for wool. A pair of shears is required to harvest the wool. Lives in the barn.",
//            8000, 2, "Deluxe Barn"),
//    Rabbit(null ,AnimalType.RABBIT, "Rabbit",
//            "These are wooly rabbits! They shed precious wool every few days. Lives in the coop.",
//            8000, 2, "Deluxe Coop"),
//    Dinosaur(null ,AnimalType.DINOSAUR, "Dinosaur",
//            "The Dinosaur is a farm animal that lives in a Big Coop",
//            14000, 2, "Big Coop"),
//    Pig(null ,AnimalType.PIG, "Pig",
//            "These pigs are trained to find truffles! Lives in the barn.",
//            16000, 2, "Deluxe Barn");
//
//
//
//    private final MaterialTypes itemType;
//    private final AnimalType animalType;
//    private final String name;
//    private final String description;
//    private final int price;
//    private final int dailyLimit;
//    private final String buildingRequired;
//
//    MarniesRanch(MaterialTypes itemType, AnimalType animalType, String name, String description, int price, int dailyLimit, String buildingRequired) {
//        this.itemType = itemType;
//        this.animalType = animalType;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.dailyLimit = dailyLimit;
//        this.buildingRequired = buildingRequired;
//    }
//
//    @Override
//    public MaterialTypes getItemType() { return itemType; }
//    public AnimalType getAnimalType() { return animalType; }
//    @Override public String getDisplayName() { return name; }
//    @Override public String getDescription() { return description; }
//    @Override public int getPrice() { return price; }
//    @Override public int getDailyLimit() { return dailyLimit; }
//
//    public String getBuildingRequired() { return buildingRequired; }
//
//    @Override
//    public String toString() {
//        return this.name() + "\nPrice: " +
//        this.price + "\nDescription: " + this.description +
//        "\n----------------------\n";
//    }
//
//    public static MarniesRanch forAnimal(AnimalType animal) {
//        for (MarniesRanch entry : values()) {
//            if (animal.equals(entry.getAnimalType())) {
//                return entry;
//            }
//        }
//        throw new IllegalArgumentException("No shop entry for animal: " + animal);
//    }
//}
