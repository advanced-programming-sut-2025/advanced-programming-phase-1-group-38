package models.Animals;

import models.Position;
import models.enums.Types.AnimalProductType;
import models.enums.Types.AnimalType;

import java.util.ArrayList;
import java.util.Random;

public class Animal {
    private String name;
    private AnimalType animalType;
    protected Position position;
    private int animalCost;
    public int friendshipLevel;
    private ArrayList<AnimalProduct> producedProducts;
    private boolean isHungry;
    private AnimalLivingSpace animalLivingSpace;

    public Animal(String name, AnimalType animalType) {
        this.name = name;
        this.animalType = animalType;
        this.friendshipLevel = 0;
        this.producedProducts = new ArrayList<>();
        this.isHungry = true;
    }

    public AnimalLivingSpace getAnimalLivingSpace() {
        return this.animalLivingSpace;
    }

    public void feedHay() {
        if (this.isHungry) {
            this.isHungry = false;
            increaseFriendship(8);
        }
    }

    public void collectProduce() {
        if (canProduce()) {
            AnimalProduct product = createProduct();
            this.producedProducts.add(product);
            increaseFriendship(5);
        }
    }

    public String getName() {
        return name;
    }

    public boolean canProduce() {
        return friendshipLevel > 200;
    }

    private AnimalProduct createProduct() {
        Random random = new Random();
        AnimalProductType productType = getRandomProduct();

        return new AnimalProduct(productType, this);
    }

    private AnimalProductType getRandomProduct() {
        ArrayList<AnimalProductType> products = new ArrayList<>(this.animalType.getAnimalProducts());
        Random random = new Random();
        return products.get(random.nextInt(products.size()));
    }

    private void increaseFriendship(int amount) {
        this.friendshipLevel = Math.min(friendshipLevel + amount, 1000);
    }

    private void decreaseFriendship(int amount) {
        this.friendshipLevel = Math.max(friendshipLevel - amount, 0);
    }

    public void resetDailyFlags() {
        isHungry = true;
    }

    public void pet() {
        increaseFriendship(15);
    }

    public ArrayList<AnimalProduct> getProducedProducts() {
        return this.producedProducts;
    }

    public int getFriendshipLevel() {
        return this.friendshipLevel;
    }

    public AnimalType getAnimalType() {
        return this.animalType;
    }

    public int getAnimalCost() {
        return this.animalType.getAnimalPrice();
    }
}