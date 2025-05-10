package models;

import models.enums.Types.FarmBuildingType;

public class FarmBuilding {
    private FarmBuildingType farmBuildingType;
    private Position positionOfUpperLeftCorner;
    private int width;
    private int length;
    private String description;
    private int woodNeed;
    private int stoneNeed;
    private int cost;

    public FarmBuilding(FarmBuildingType farmBuildingType, Position positionOfUpperLeftCorner) {
        this.farmBuildingType = farmBuildingType;
        this.positionOfUpperLeftCorner = positionOfUpperLeftCorner;
        this.width = farmBuildingType.getWidth();
        this.length = farmBuildingType.getLength();
        this.description = farmBuildingType.getDescription();
    }

}
