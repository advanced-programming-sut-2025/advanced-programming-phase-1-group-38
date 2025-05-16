package models.Animals;

import models.Item;
import models.enums.Types.AnimalProductType;
import models.enums.ProductQuality;
import models.enums.Types.ItemType;
import models.enums.Types.ToolType;

public class AnimalProduct extends Item {
    private final AnimalProductType type;
    private final ProductQuality quality;
    private final int basePrice;
    private final Animal producerAnimal;

    public AnimalProduct(AnimalProductType type, Animal producerAnimal, ProductQuality quality) {
        super(ItemType.ANIMAL_PRODUCT);
        this.type = type;
        this.producerAnimal = producerAnimal;
        this.quality = quality;
        this.basePrice = type.getBasePrice();
        calculatePrice();
    }

    private void calculatePrice() {
        double rawPrice = basePrice * (0.3 + producerAnimal.getFriendshipLevel() / 1000.0);
        int finalPrice = (int) (rawPrice * quality.getPriceCoefficient());
        this.setPrice(finalPrice);
    }

    public AnimalProductType getProductType() {
        return type;
    }

    public ProductQuality getQuality() {
        return quality;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Animal getProducerAnimal() {
        return producerAnimal;
    }

    public boolean requiresTool() {
        return type.requiresTool();
    }

    public ToolType requiredToolName() {
        return type.getRequiredToolName();
    }

    @Override
    public String toString() {
        return type.name() + " (" + quality.name() + ") - " + getPrice() + "g";
    }
}
