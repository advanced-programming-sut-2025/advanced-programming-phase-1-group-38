package io.github.StardewValley.models;

public class GroundDrop {
    private final ItemType item;
    public GroundDrop(ItemType item) { this.item = item; }
    public ItemType item() { return item; }
    public String iconPath() { return item.iconPath(); }
}
