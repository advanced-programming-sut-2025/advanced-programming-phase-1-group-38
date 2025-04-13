package models;

import models.enums.Types.ShopType;

import java.util.HashMap;

public class Shop {
    protected String name;
    protected ShopType type;
    protected HashMap<Item, Integer> shopInventory;
    protected int balance;
    protected NPC owner;

    public Shop(ShopType type) {
        this.type = type;
        this.shopInventory = new HashMap<>();
        this.balance = 0;
    }

    public String getName() {
        return name;
    }

    public HashMap<Item, Integer> getShopInventory() {
        return shopInventory;
    }

    public int getBalance() {
        return balance;
    }

    public NPC getOwner() {
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
