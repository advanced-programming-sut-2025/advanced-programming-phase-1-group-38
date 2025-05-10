package models.inventory;

import models.Item;

public class Refrigerator extends Inventory {
    public Refrigerator(int capacity, boolean isCapacityUnlimited) {
        super(capacity, isCapacityUnlimited);
    }

    @Override
    public boolean hasSpaceFor(Item item, int quantity) {
        return false;
    }

    @Override
    public void addToInventory(Item item, int n) {

    }

    @Override
    public void CheatAddToInventory(Item item, int n) {

    }

    @Override
    public void removeFromInventory(Item item, int n) {

    }

    @Override
    public boolean containsItem(Item item) {
        return false;
    }

    private void put(Item item) {
    }
    private void pick(Item item) {
    }
}
