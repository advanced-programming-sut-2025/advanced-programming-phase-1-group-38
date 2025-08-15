package io.github.StardewValley.models;

public enum ToolType implements ItemType {
    SCYTHE ("scythe",  "tools/scythe.png"),
    PICKAXE("pickaxe", "tools/pickaxe.png"),
    WATERCAN("watercan", "tools/watercan/basic.png"),
    AXE("axe", "tools/axe/basic.png"),
    HOE("hoe", "tools/hoe.png"),
    SHEARS("shears", "tools/shears.png"),
    MILKPAIL("milkpail", "tools/milkpail.png"),
    FISHINGPOLE("fishingpole", "tools/fishingpole.png");

    private final String id;
    private final String iconPath;

    ToolType(String id, String iconPath) {
        this.id = id;
        this.iconPath = iconPath;
    }

    @Override public String id()        { return id; }
    @Override public String iconPath()  { return iconPath; }
    @Override public int    maxStack()  { return 1; }   // tools are never stackable
}
