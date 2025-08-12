package io.github.StardewValley.models;


import io.github.StardewValley.models.farming.SeedType;

public enum CropType implements ItemType {

    CORN(
        "Corn",
        new String[]{
            "crops/corn/0.png",
            "crops/corn/1.png",
            "crops/corn/2.png",
            "crops/corn/3.png",
            "crops/corn/4.png"
        },
        new int[]{2, 3, 3, 3},
        "CORN_SEED", "crops/corn/cornTexture.png", 35, 0
    ),

    CARROT(
        "Carrot",
        new String[]{
            "crops/carrot/0.png",
            "crops/carrot/1.png",
            "crops/carrot/2.png",
            "crops/carrot/3.png",
        },
        new int[]{1, 1, 1},
        "CARROT_SEED", "crops/carrot/carrotTexture.png", 50, 0
    ),

    PUMPKIN(
        "Pumpkin",
        new String[]{
            "crops/pumpkin/0.png",
            "crops/pumpkin/1.png",
            "crops/pumpkin/2.png",
            "crops/pumpkin/3.png",
        },
        new int[]{2, 3, 4},
        "PUMPKIN_SEED", "crops/pumpkin/pumpkinTexture.png", 320, 0
    ),

    WHEAT(
        "Wheat",
        new String[]{
            "crops/wheat/0.png",
            "crops/wheat/1.png",
            "crops/wheat/2.png",
            "crops/wheat/3.png",
        },
        new int[]{1, 1, 1},
        "WHEAT", "crops/wheat/wheatTexture.png", 25, 0
    );

    private final String id;
    private final String[] stageSprites;
    private final int[] stageDays;
    private final String seedTypeId;
    private SeedType seedType;
    private final String cropTexture;
    private final int sellPrice;
    private final int maxStack;

    CropType(String id, String[] stageSprites, int[] stageDays, String seedTypeId, String cropTexture, int sellPrice, int maxStack) {
        this.id = id;
        this.stageSprites = stageSprites;
        this.stageDays = stageDays;
        this.seedTypeId = seedTypeId;
        this.cropTexture = cropTexture;
        this.sellPrice = sellPrice;
        this.maxStack = maxStack;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String iconPath() {
        return stageSprites[stageSprites.length - 1];
    }

    @Override
    public int maxStack() {
        return maxStack;
    }

    public int sellPrice() {
        return sellPrice;
    }

    public int[] stageDays() {
        return stageDays;
    }

    public String[] stageSprites() {
        return stageSprites;
    }

    public int stageCount() {
        return stageDays.length;
    }



    public SeedType seedType() {
        return seedType;
    }

    public String cropTexture() {
        return cropTexture;
    }

    public int totalGrowthDays() {
        int sum = 0;
        for (int d : stageDays) sum += d;
        return sum;
    }

    public String getSpriteForDay(int daysGrown) {
        int daySum = 0;
        for (int i = 0; i < stageDays.length; i++) {
            daySum += stageDays[i];
            if (daysGrown < daySum) return stageSprites[i];
        }
        return stageSprites[stageSprites.length - 1];
    }
}
