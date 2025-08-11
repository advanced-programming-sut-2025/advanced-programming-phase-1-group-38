package io.github.StardewValley.models.Artisan;

import io.github.StardewValley.models.ItemType;

public enum MachineType implements ItemType {
    BEE_HOUSE("Bee House", "items/Bee_house.png"),
    KEG("Keg", "items/keg.png"),
    PRESERVE_JAR("Preserves Jar", "items/preserves_jar.png"),
    CHEESE_PRESS("Cheese Press", "items/cheese_press.png"),
    LOOM("Loom", "items/loom.png"),
    OIL_MAKER("Oil Maker", "items/oil_maker.png"),
    MAYO_MACHINE("Mayonnaise Machine", "items/mayo_machine.png"),
    DEHYDRATOR("Dehydrator", "items/dehydrator.png"),
    CHARCOAL_KILN("Charcoal Kiln", "items/charcoal_kiln.png");

    private final String id;
    private final String icon;

    MachineType(String id, String icon) { this.id=id; this.icon=icon; }

    @Override public String id() { return id; }
    @Override public String iconPath() { return icon; }
    @Override public int maxStack() { return 99; }
    @Override public boolean stackable() { return true; }
}
