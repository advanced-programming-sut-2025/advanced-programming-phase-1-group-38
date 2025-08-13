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

    // Crop.java – fields
    private boolean regrowing = false;
    private int regrowTargetDays = 0; // how many days are needed during regrow


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

        boolean effectiveAuto = autoWaterForever || autoWater;
        if (effectiveAuto || wateredToday) {
            if (!isFullyGrown()) daysGrown++;
            dryStreakDays = 0;
        } else {
            dryStreakDays++;
            if (dryStreakDays >= 2) dead = true;
        }
        wateredToday = false;

        // optional: once a regrow cycle reaches its target, mark as normal mature again
        if (regrowing && isFullyGrown()) regrowing = false;
    }


    public void setAutoWaterForever(boolean v) { this.autoWaterForever = v; }
    public boolean isAutoWaterForever() { return autoWaterForever; }

        // --- Queries ---
        public boolean isFullyGrown() {
            int target = regrowing ? regrowTargetDays : cropType.totalGrowthDays();
            return daysGrown >= target;
        }

    public boolean isDead()       { return dead; }
    public boolean isHarvested()  { return harvested; }
    public CropType getCropType() { return cropType; }
    public int getDaysGrown()     { return daysGrown; }
    public boolean wasWateredToday() { return wateredToday; }

    public String getCurrentSpritePath() {
        if (dead) return deadSpritePath;

        if (regrowing) {
            int total = Math.max(1, cropType.totalGrowthDays());
            int target = Math.max(1, regrowTargetDays);
            int scaledDay = Math.round((daysGrown / (float) target) * total);
            return cropType.getSpriteForDay(scaledDay);
        }

        return cropType.getSpriteForDay(daysGrown);
    }

    /** Mark removed. Controller decides rewards (none if dead or not grown). */
    public void harvest() {
        harvested = true;  // ← allows clearing dead crops with scythe
    }

    // Crop.java  (add this method; keep your existing code)
    public boolean harvestAndMaybeRegrow() {
        if (dead) { harvested = true; return true; }
        if (!isFullyGrown()) return false;

        int regrow = cropType.regrowDays();
        if (regrow > 0) {
            // start a new regrow cycle FROM STAGE 0 (show 0.png)
            regrowing = true;
            regrowTargetDays = Math.max(1, regrow);
            daysGrown = 0;            // ← key change: reset to 0 so 0.png shows
            harvested = false;
            dryStreakDays = 0;
            wateredToday = false;
            return false;             // keep the plant on the tile
        }

        harvested = true;             // one-time crop → remove from tile
        return true;
    }


    // Optional customization hooks
    public void setDeadSpritePath(String path) { this.deadSpritePath = path; }

    // If you want to keep this, make it private and call from updateDaily().
    private void growOneDay() {
        if (!isFullyGrown()) daysGrown++;
    }
}
