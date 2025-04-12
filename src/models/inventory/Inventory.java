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
        this.items = new HashMap<>();
    }

    public boolean hasItem(Item item, int quantity) {
        return false;
    }

    public boolean hasSpaceFor(Item item, int quantity) {
        return false;

    }

    public void addToInventory(Item item, int n) {
    }

    public void CheatAddToInventory(Item item, int n) {
    }

    public void removeFromInventory(Item item, int n) {
    }
}
