package models;

import models.enums.Types.ShopType;

import java.util.HashMap;

public class Shop {
    protected String name;
    protected String owner;
    protected ShopType type;
    protected HashMap<Item, Integer> shopInventory;

    public Shop(ShopType type) {
        this.type = type;
        this.shopInventory = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public HashMap<Item, Integer> getShopInventory() {
        return shopInventory;
    }

    public String getOwner() {
        return owner;
    }

    void addProduct(Item item, int count) {
    }

    void removeProduct(Item item, int count) {
    }

    void sellProduct(Item item, int count) {
    }

    void showAvailableProducts() {
    }
}
