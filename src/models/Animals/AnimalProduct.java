package models.Animals;

import models.Item;
import models.enums.Types.ItemType;
import models.enums.ProductQuality;
import models.enums.Types.AnimalProductType;

public class AnimalProduct extends Item {
    private AnimalProductType type;
    private int basePrice;
    private ProductQuality quality;
    private Animal producerAnimal;

    public AnimalProduct(AnimalProductType type, Animal producerAnimal) {
        super(ItemType.ANIMAL_PRODUCT);
        this.type = type;
        this.producerAnimal = producerAnimal;
        this.basePrice = type.getBasePrice();
        this.quality = ProductQuality.NORMAL;
        calculatePrice();
    }

    public AnimalProduct(AnimalProductType type, Animal producerAnimal, int basePrice, ProductQuality quality) {
        super(ItemType.ANIMAL_PRODUCT);
        this.type = type;
        this.producerAnimal = producerAnimal;
        this.basePrice = basePrice;
        this.quality = quality;
        calculatePrice();
    }

    public void calculatePrice() {
        double qualityMultiplier = this.quality.getPriceCoefficient();
        double price = this.basePrice * ((double) this.producerAnimal.getFriendshipLevel() / 1000 + 0.3);
        this.setPrice((int) (price * qualityMultiplier));
    }

    public AnimalProductType getProductType() {
        return this.type;
    }

    public ProductQuality getQuality() {
        return this.quality;
    }

    public int getBasePrice() {
        return this.basePrice;
    }

    public Animal getProducerAnimal() {
        return this.producerAnimal;
    }
}