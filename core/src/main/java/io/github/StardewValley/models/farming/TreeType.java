package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.ItemType;
import io.github.StardewValley.models.farming.SeedType;

public enum TreeType implements ItemType {

//    OAK(
//        "Oak",
//        new String[]{ "trees/oak/0.png","trees/oak/1.png","trees/oak/2.png","trees/oak/3.png" },
//        new int[]{2,3,4},
//        null,
//        SyrupType.OAK_RESIN,
//        8,
//        "OAK_SAPLING",
//        "trees/oak/icon.png",
//        0,
//        "oak"            // ← animKey (matches tree/*/oak/)
//    ),
//
//    MAPLE(
//        "Maple",
//        new String[]{ "trees/maple/0.png","trees/maple/1.png","trees/maple/2.png","trees/maple/3.png" },
//        new int[]{2,3,4},
//        null,
//        SyrupType.MAPLE_SYRUP,
//        7,
//        "MAPLE_SAPLING",
//        "trees/maple/icon.png",
//        0,
//        "maple"
//    ),
//
//    ORANGE_TREE(
//        "Orange Tree",
//        new String[]{ "trees/orange/0.png","trees/orange/1.png","trees/orange/2.png","trees/orange/3.png" },
//        new int[]{3,3,4},
//        FruitType.ORANGE,
//        null,
//        5,
//        "ORANGE_SAPLING",
//        "trees/orange/icon.png",
//        0,
//        "orange"
//    );

    APPLE(
        "Apple Tree",
        new String[]{
            "trees/grow/apple/0.png", "trees/grow/apple/1.png", "trees/grow/apple/2.png", "trees/grow/apple/3.png"
        },
        new int[]{1,1,1},
        FruitType.APPLE,    // or null if you don't have it
        null,
        6,
        "APPLE_SAPLING",
        "apple.png",
        0,
        "apple"             // <── animation key (matches tree/chop/apple/* etc.)
    );

    // ---- data ----
    private final String id;
    private final String[] stageSprites;
    private final int[] stageDays;
    private final FruitType fruit;
    private final SyrupType syrup;
    private final int woodYield;
    private final String saplingId;
    private final String icon;
    private final int maxStack;
    private final String animKey;   // ← NEW

    private SeedType sapling;

    TreeType(
        String id,
        String[] stageSprites,
        int[] stageDays,
        FruitType fruit,
        SyrupType syrup,
        int woodYield,
        String saplingId,
        String icon,
        int maxStack,
        String animKey                 // ← NEW
    ) {
        this.id = id;
        this.stageSprites = stageSprites;
        this.stageDays = stageDays;
        this.fruit = fruit;
        this.syrup = syrup;
        this.woodYield = woodYield;
        this.saplingId = saplingId;
        this.icon = icon;
        this.maxStack = maxStack;
        this.animKey = animKey;      // ← NEW
    }

    // ---- ItemType ----
    @Override public String id() { return id; }
    @Override public String iconPath() { return icon != null ? icon : stageSprites[stageSprites.length - 1]; }
    @Override public int maxStack() { return maxStack; }

    // ---- growth API ----
    public String[] stageSprites() { return stageSprites; }
    public int[] stageDays()       { return stageDays; }
    public int stageCount()        { return stageDays.length; }

    public String getSpriteForDay(int daysGrown) {
        int daySum = 0;
        for (int i = 0; i < stageDays.length; i++) {
            daySum += stageDays[i];
            if (daysGrown < daySum) return stageSprites[i];
        }
        return stageSprites[stageSprites.length - 1];
    }

    public int totalGrowthDays() { int s = 0; for (int d : stageDays) s += d; return s; }

    // ---- products ----
    public FruitType fruitType()   { return fruit; }
    public SyrupType syrupType()   { return syrup; }
    public boolean producesFruit() { return fruit != null; }
    public boolean producesSyrup() { return syrup != null; }
    public int woodYield()         { return woodYield; }

    // ---- sapling linkage ----
    public String saplingId()      { return saplingId; }
    public SeedType saplingType()  { return sapling; }
    public void bindSapling(SeedType s) { this.sapling = s; }

    // ---- animations ----
    public String animKey()        { return animKey; }   // ← NEW
}
