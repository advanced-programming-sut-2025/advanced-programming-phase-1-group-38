package models.Animals;

import models.FarmBuilding;
import models.Position;
import models.enums.Types.FarmBuildingType;

import java.util.ArrayList;

public class AnimalLivingSpace extends FarmBuilding {
    private ArrayList<Animal> animals;
    private boolean isCage;

    public AnimalLivingSpace(FarmBuildingType farmBuildingType, Position positionOfUpperLeftCorner) {
        super(farmBuildingType, positionOfUpperLeftCorner);
    }

    void addAnimal(Animal animal) {
    }

    void removeAnimal(Animal animal) {
    }
}
