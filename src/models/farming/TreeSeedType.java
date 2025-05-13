package models.farming;

import models.GameRandom;

import java.util.Arrays;
import java.util.List;

public enum TreeSeedType implements TreeSourceType, SeedType {
    ACORN("Acorn", TreeType.OAK),
    MAPLE_SEED("Maple Seed", TreeType.MAPLE),
    PINE_CONE("Pine Cone", TreeType.PINE),
    MAHOGANY_SEED("Mahogany Seed", TreeType.MAHOGANY),
    MUSHROOM_SEED("Mushroom Seed", TreeType.MUSHROOM);

    private final String name;
    private final TreeType treeType;

    TreeSeedType(String name, TreeType treeType) {
        this.name = name;
        this.treeType = treeType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCrop() {
        return false;
    }

    public static TreeSeedType getRandomSeed() {
        List<TreeSeedType> seeds = Arrays.asList(values());
        return GameRandom.pickRandom(seeds);
    }

    public static Seed getRandomSeedItem() {
        return new Seed(getRandomSeed());
    }

    @Override
    public TreeType getTreeType() {
        return treeType;
    }

    @Override
    public String toString() {
        return name;
    }
}

