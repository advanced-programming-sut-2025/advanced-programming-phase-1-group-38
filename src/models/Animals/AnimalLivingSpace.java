package models.Animals;

import models.FarmBuilding;
import models.Position;
import models.enums.Types.FarmBuildingType;

import java.util.ArrayList;

public class AnimalLivingSpace extends FarmBuilding {
    private final ArrayList<Animal> animals;
    private final boolean isCage;

    public AnimalLivingSpace(FarmBuildingType farmBuildingType, Position positionOfUpperLeftCorner) {
        super(farmBuildingType, positionOfUpperLeftCorner);
        this.animals = new ArrayList<>();
        this.isCage = farmBuildingType.name().contains("COOP");
    }

    public boolean addAnimal(Animal animal) {
        if (isFull()) return false;
        animals.add(animal);
        return true;
    }

    public boolean removeAnimalByName(String name) {
        return animals.removeIf(a -> a.getName().equalsIgnoreCase(name));
    }

    public Animal getAnimalByName(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) return animal;
        }
        return null;
    }

    public boolean hasAnimalWithName(String name) {
        return getAnimalByName(name) != null;
    }

    public boolean isFull() {
        return animals.size() >= getCapacity();
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public boolean isCage() {
        return isCage;
    }

    public boolean isBarn() {
        return !isCage;
    }

    public void resetDailyStates() {
        for (Animal a : animals) {
            a.resetDailyFlags();
        }
    }

    public ArrayList<Animal> getAnimalsWithProduce() {
        ArrayList<Animal> result = new ArrayList<>();
        for (Animal a : animals) {
            if (a.canProduce()) {
                result.add(a);
            }
        }
        return result;
    }
}