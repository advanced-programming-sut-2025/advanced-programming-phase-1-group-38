package models;

import models.enums.ItemType;

public abstract class Item {
    private ItemType type;

    public Item(ItemType type) {
        this.type = type;
    }

    public void use(){

    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

}
