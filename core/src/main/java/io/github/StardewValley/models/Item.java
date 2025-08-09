package io.github.StardewValley.models;

public abstract class Item {
    private ItemType type;
    private int price;
    private String name;

    public Item(ItemType type) {
        this.type = type;
    }
    public Item(String name, ItemType type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getPrice() {
        return this.price;
    }

    public String getName() {
        return name != null ? name : getClass().getSimpleName();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return getName();
    }
}
