package io.github.StardewValley.models;

public enum ToolType implements ItemType {
    SCYTHE   ("scythe",    "tools/scythe.png"),
    PICKAXE  ("pickaxe",   "tools/scythe.png"),
    // --- NEW ---
    MILK_PAIL("milk_pail", "tools/milk_pail.png"),
    SHEAR    ("shear",     "tools/shears.png");

    private final String id;
    private final String iconPath;

    ToolType(String id, String iconPath) {
        this.id = id;
        this.iconPath = iconPath;
    }

    @Override public String id()        { return id; }
    @Override public String iconPath()  { return iconPath; }
    @Override public int    maxStack()  { return 1; }
}