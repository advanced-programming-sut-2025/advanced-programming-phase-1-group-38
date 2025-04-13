package models.Animals;

import models.Position;
import models.enums.Types.AnimalType;

import java.util.ArrayList;

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
    }

    public AnimalLivingSpace getAnimalLivingSpace() {
        return this.animalLivingSpace;
    }

    public void feedHay() {
    }

    public void collectProduce() {
    }

    private void updateFriendshipLevel() {
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

}
