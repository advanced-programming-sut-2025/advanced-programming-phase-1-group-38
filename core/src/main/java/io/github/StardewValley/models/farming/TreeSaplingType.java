package io.github.StardewValley.models.farming;

import io.github.StardewValley.models.ItemType;

public enum TreeSaplingType implements ItemType {
//    OAK_SAPLING   ("OAK_SAPLING",   "trees/oak/icon.png",   999, TreeType.OAK),
//    MAPLE_SAPLING ("MAPLE_SAPLING", "trees/maple/icon.png", 999, TreeType.MAPLE),
//    ORANGE_SAPLING("ORANGE_SAPLING","trees/orange/icon.png",999, TreeType.ORANGE_TREE);
    APPLE_SAPLING("APPLE_SAPLING","trees/sapling/apple.png",0, TreeType.APPLE);

    private final String id, iconPath;
    private final int maxStack;
    private final TreeType treeType;

    TreeSaplingType(String id, String iconPath, int maxStack, TreeType treeType) {
        this.id = id; this.iconPath = iconPath; this.maxStack = maxStack; this.treeType = treeType;
    }

    @Override public String id() { return id; }
    @Override public String iconPath() { return iconPath; }
    @Override public int maxStack() { return maxStack; }

    public TreeType growsInto() { return treeType; }
}

