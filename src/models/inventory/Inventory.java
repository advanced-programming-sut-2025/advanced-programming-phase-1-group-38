package models.inventory;

import models.Item;

import java.util.HashMap;
import java.util.Map;

public abstract class Inventory {
    protected int capacity;
    protected boolean isCapacityUnlimited;
    protected Map<Item, Integer> items;

    public Inventory(int capacity, boolean isCapacityUnlimited) {
        this.capacity = capacity;
        this.isCapacityUnlimited = isCapacityUnlimited;
    }

    public boolean hasItem(Item item, int quantity) {
        return items.getOrDefault(item, 0) >= quantity;
    }

    public abstract boolean hasSpaceFor(Item item, int quantity);

    public abstract void addToInventory(Item item, int n);

    public abstract void CheatAddToInventory(Item item, int n);

    public abstract void removeFromInventory(Item item, int n);

    public abstract boolean containsItem(Item item);
}
