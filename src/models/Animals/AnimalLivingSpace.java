package models.Animals;

import models.FarmBuilding;
import models.Position;
import models.enums.Types.FarmBuildingType;

import java.util.ArrayList;
import java.util.List;

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
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        return null;
    }

    public boolean feedAnimal(String name) {
        Animal animal = getAnimalByName(name);
        if (animal != null && !animal.isFedToday()) {
            animal.feed();
            return true;
        }
        return false;
    }

    public boolean petAnimal(String name) {
        Animal animal = getAnimalByName(name);
        if (animal != null && !animal.isPetToday()) {
            animal.pet();
            return true;
        }
        return false;
    }

    public AnimalProduct collectFromAnimal(String name) {
        Animal animal = getAnimalByName(name);
        if (animal != null && animal.isProductReady()) {
            return animal.collectProduct();
        }
        return null;
    }

    public void resetDailyStatusForAllAnimals() {
        for (Animal animal : animals) {
            animal.resetDailyStatus();
        }
    }

    public List<String> getProduces() {
        List<String> result = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.isProductReady()) {
                result.add(animal.getName() + ": " + animal.getAnimalType().getAnimalProducts());
            }
        }
        return result;
    }

    public boolean isFull() {
        return animals.size() >= this.getFarmBuildingType().getCapacity();
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public boolean isCage() {
        return isCage;
    }
}
