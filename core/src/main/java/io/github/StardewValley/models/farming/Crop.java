package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.CropType;

public class Crop {
    private final CropType cropType;
    private int daysGrown;
    private boolean harvested;

    public Crop(CropType cropType) {
        this.cropType = cropType;
        this.daysGrown = 0;
        this.harvested = false;
    }

    public void growOneDay() {
        if (!isFullyGrown()) {
            daysGrown++;
        }
    }

    public boolean isFullyGrown() {
        return daysGrown >= cropType.totalGrowthDays();
    }

    public String getCurrentSpritePath() {
        return cropType.getSpriteForDay(daysGrown);
    }

    public int getDaysGrown() {
        return daysGrown;
    }

    public CropType getCropType() {
        return cropType;
    }

    public boolean isHarvested() {
        return harvested;
    }

    public void harvest() {
        if (isFullyGrown()) {
            harvested = true;
        }
    }

    public void updateDaily() {
        growOneDay();  // Advances growth by one day if not fully grown
    }
}
