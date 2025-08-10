package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.enums.Types.MaterialTypes;

public enum TreeSaplingType implements TreeSourceType, MaterialTypes {
    APRICOT_SAPLING("Apricot Sapling", TreeType.APRICOT),
    CHERRY_SAPLING("Cherry Sapling", TreeType.CHERRY),
    BANANA_SAPLING("Banana Sapling", TreeType.BANANA),
    MANGO_SAPLING("Mango Sapling", TreeType.MANGO),
    ORANGE_SAPLING("Orange Sapling", TreeType.ORANGE),
    PEACH_SAPLING("Peach Sapling", TreeType.PEACH),
    APPLE_SAPLING("Apple Sapling", TreeType.APPLE),
    POMEGRANATE_SAPLING("Pomegranate Sapling", TreeType.POMEGRANATE);

    private final String name;
    private final TreeType treeType;

    TreeSaplingType(String name, TreeType treeType) {
        this.name = name;
        this.treeType = treeType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TreeType getTreeType() {
        return treeType;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
