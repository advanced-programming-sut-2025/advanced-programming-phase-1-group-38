package models;

import models.enums.Types.ItemType;

public abstract class Item {
    private ItemType type;
    private int price;
    private String name;
    private int basePrice;

    public Item(ItemType type) {
        this.type = type;
    }
    public Item(String name, ItemType type, int basePrice) {
        this.name = name;
        this.type = type;
        this.basePrice = basePrice;
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

    public void setPrice(int price) {
        this.price = price;
    }
}
