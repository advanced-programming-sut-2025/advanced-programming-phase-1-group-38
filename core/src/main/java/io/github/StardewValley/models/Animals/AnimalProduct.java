//package io.github.StardewValley.models.Animals;
//
//import io.github.StardewValley.models.Item;
//import io.github.StardewValley.models.enums.ProductQuality;
//import io.github.StardewValley.models.enums.Types.AnimalProductType;
//
//import java.util.Objects;
//
//public class AnimalProduct extends Item {
//    private final AnimalProductType type;
//    private final ProductQuality quality;
//    private final int basePrice;
//    private final Animal producerAnimal;
//
//    public AnimalProduct(AnimalProductType type, Animal producerAnimal, ProductQuality quality) {
//        super(ItemType.ANIMAL_PRODUCT);
//        this.type = type;
//        this.producerAnimal = producerAnimal;
//        this.quality = quality;
//        this.basePrice = type.getBasePrice();
//        calculatePrice();
//    }
//
//    private void calculatePrice() {
//        double rawPrice = basePrice * (0.3 + producerAnimal.getFriendshipLevel() / 1000.0);
//        int finalPrice = (int) (rawPrice * quality.getPriceCoefficient());
//        this.setPrice(finalPrice);
//    }
//
//    public AnimalProductType getProductType() {
//        return type;
//    }
//
//    public ProductQuality getQuality() {
//        return quality;
//    }
//
//    public int getBasePrice() {
//        return basePrice;
//    }
//
//    public Animal getProducerAnimal() {
//        return producerAnimal;
//    }
//
//    public boolean requiresTool() {
//        return type.requiresTool();
//    }
//
//    public ToolType requiredToolName() {
//        return type.getRequiredToolName();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof AnimalProduct other)) return false;
//        return this.type == other.type && this.quality == other.quality;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(type, quality);
//    }
//
//    @Override
//    public String toString() {
//        return type.name() + " (" + quality.name() + ") - " + getPrice() + "g";
//    }
//}
