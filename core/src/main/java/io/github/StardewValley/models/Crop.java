package io.github.StardewValley.models;

public class Crop {
    private final CropType cropType;
    private int daysGrown;
    private boolean harvested;

    // Watering / wither
    private boolean wateredToday = false;
    private int  dryStreakDays = 0;
    private boolean dead = false;
    private boolean autoWaterForever = false;

    // Optional: override per crop via setter or CropType
    private String deadSpritePath = "crops/dead.png";

    public Crop(CropType cropType) {
        this.cropType = cropType;
        this.daysGrown = 0;
        this.harvested = false;
    }

    /** Player action: water the crop for today. */
    public void water() {
        if (!harvested && !dead) wateredToday = true;
    }

    /** Daily tick (Tile.updateDaily() already calls this). */
    // in Crop.java
    // Crop.java

    public void updateDaily() { updateDaily(false); }

    public void updateDaily(boolean autoWater) {
        if (harvested || dead) { wateredToday = false; return; }

        // greenhouse OR caller-forced autoWater
        boolean effectiveAuto = autoWaterForever || autoWater;
        if (effectiveAuto) {
            if (!isFullyGrown()) daysGrown++;
            dryStreakDays = 0;
            wateredToday = false;
            return;
        }

        if (wateredToday) {
            if (!isFullyGrown()) daysGrown++;
            dryStreakDays = 0;
        } else {
            dryStreakDays++;
            if (dryStreakDays >= 2) dead = true;
        }

        // IMPORTANT: reset for the next day
        wateredToday = false;
    }

    public void setAutoWaterForever(boolean v) { this.autoWaterForever = v; }
    public boolean isAutoWaterForever() { return autoWaterForever; }

        // --- Queries ---
    public boolean isFullyGrown() { return daysGrown >= cropType.totalGrowthDays(); }
    public boolean isDead()       { return dead; }
    public boolean isHarvested()  { return harvested; }
    public CropType getCropType() { return cropType; }
    public int getDaysGrown()     { return daysGrown; }
    public boolean wasWateredToday() { return wateredToday; }

    public String getCurrentSpritePath() {
        return dead ? deadSpritePath : cropType.getSpriteForDay(daysGrown);
    }

    /** Mark removed. Controller decides rewards (none if dead or not grown). */
    public void harvest() {
        harvested = true;  // ← allows clearing dead crops with scythe
    }

    // Optional customization hooks
    public void setDeadSpritePath(String path) { this.deadSpritePath = path; }

    // If you want to keep this, make it private and call from updateDaily().
    private void growOneDay() {
        if (!isFullyGrown()) daysGrown++;
    }
}
