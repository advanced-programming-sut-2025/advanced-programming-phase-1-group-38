package models;

import models.enums.Types.FarmBuildingType;

public class FarmBuilding {
    private final FarmBuildingType farmBuildingType;
    private final Position positionOfUpperLeftCorner;

    public FarmBuilding(FarmBuildingType farmBuildingType, Position positionOfUpperLeftCorner) {
        this.farmBuildingType = farmBuildingType;
        this.positionOfUpperLeftCorner = positionOfUpperLeftCorner;
    }

    public FarmBuildingType getFarmBuildingType() {
        return farmBuildingType;
    }

    public Position getPositionOfUpperLeftCorner() {
        return positionOfUpperLeftCorner;
    }

    public int getWidth() {
        return farmBuildingType.getWidth();
    }

    public int getLength() {
        return farmBuildingType.getLength();
    }

    public int getCapacity() {
        return farmBuildingType.getCapacity();
    }

    public String getDescription() {
        return farmBuildingType.getDescription();
    }

    public int getWoodNeed() {
        return farmBuildingType.getWoodNeed();
    }

    public int getStoneNeed() {
        return farmBuildingType.getStoneNeed();
    }

    public int getCost() {
        return farmBuildingType.getCost();
    }
}