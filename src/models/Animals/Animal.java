package models.Animals;

import models.Position;
import models.enums.Types.AnimalType;
import models.enums.ProductQuality;
import models.enums.Types.AnimalProductType;
import models.enums.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    private final String name;
    private final AnimalType animalType;
    private int friendshipLevel;
    private boolean isFedToday;
    private boolean isPetToday;
    private boolean productReady;
    private final List<AnimalProduct> producedProducts;
    private boolean isOutside;
    private boolean wentOutside;
    private Position position;

    public Animal(String name, AnimalType animalType) {
        this.name = name;
        this.animalType = animalType;
        this.friendshipLevel = 0;
        this.isFedToday = false;
        this.isPetToday = false;
        this.productReady = false;
        this.producedProducts = new ArrayList<>();
        this.isOutside = false;
        this.wentOutside = false;
        this.position = null;
    }

    public String getName() { return name; }

    public AnimalType getAnimalType() { return animalType; }

    public int getFriendshipLevel() { return friendshipLevel; }

    public boolean isFedToday() { return isFedToday; }

    public boolean isPetToday() { return isPetToday; }

    public boolean isProductReady() { return productReady; }

    public boolean isOutside() { return isOutside; }

    public boolean hasWentOutside() { return wentOutside; }

    public Position getPosition() { return position; }

    public void setWentOutside(boolean wentOutside) {
        this.wentOutside = wentOutside;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setOutside(boolean outside) {
        this.isOutside = outside;
        if (outside) this.wentOutside = true;
    }

    public void feed() {
        if (!isFedToday) {
            isFedToday = true;
            productReady = true;
            updateFriendshipLevel(2);
        }
    }

    public void feedOutside() {
        if (!isFedToday) {
            isFedToday = true;
            productReady = true;
            updateFriendshipLevel(8);
        }
    }

    public void pet() {
        if (!isPetToday) {
            isPetToday = true;
            updateFriendshipLevel(15);
        }
    }

    public void shepherdAnimal(Position newPosition, Weather currentWeather) {
        if (currentWeather == Weather.SNOW || currentWeather == Weather.RAINY || currentWeather == Weather.STORM) {
            System.out.println("The weather is " + currentWeather.name().toLowerCase() + ". You can't bring the animal outside.");
            return;
        }
        setOutside(true);
        setPosition(newPosition);
        setWentOutside(true);
        feedOutside();
        updateFriendshipLevel(8);
    }

    public AnimalProduct collectProduct() {
        if (!productReady) return null;
        if (animalType == AnimalType.PIG && !isOutside) return null;

        List<AnimalProductType> productList = animalType.getAnimalProducts();
        if (productList.isEmpty()) return null;

        AnimalProductType productType;
        Random rand = new Random();

        if (productList.size() > 1 && friendshipLevel >= 100) {
            double r = rand.nextDouble();
            double chance = (friendshipLevel + 150 * r) / 1500.0;
            productType = (chance >= 1.5) ? productList.get(1) : productList.get(0);
        } else {
            productType = productList.get(0);
        }

        double r2 = rand.nextDouble();
        double qualityValue = (friendshipLevel / 1000.0) * (0.5 + 0.5 * r2);
        ProductQuality quality;
        if (qualityValue >= 0.9) {
            quality = ProductQuality.IRIDIUM;
        } else if (qualityValue >= 0.75) {
            quality = ProductQuality.GOLD;
        } else if (qualityValue >= 0.5) {
            quality = ProductQuality.SILVER;
        } else {
            quality = ProductQuality.NORMAL;
        }

        AnimalProduct product = new AnimalProduct(productType, this, quality);
        producedProducts.add(product);
        productReady = false;
        return product;
    }

    public void resetDailyStatus() {
        if (!isFedToday) friendshipLevel = Math.max(0, friendshipLevel - 20);
        if (!isPetToday) friendshipLevel = Math.max(0, friendshipLevel - 10);
        if (isOutside) friendshipLevel = Math.max(0, friendshipLevel - 20);

        isFedToday = false;
        isPetToday = false;
        wentOutside = false;
    }

    private void updateFriendshipLevel(int delta) {
        friendshipLevel = Math.min(1000, friendshipLevel + delta);
    }

    public int getSellPrice() {
        return (int) (animalType.getAnimalPrice() * (0.3 + friendshipLevel / 1000.0));
    }

    public void setFriendshipLevel(int friendshipLevel) {
        if (friendshipLevel < 0) this.friendshipLevel = 0;
        else if (friendshipLevel > 1000) this.friendshipLevel = 1000;
        else this.friendshipLevel = friendshipLevel;
    }
}