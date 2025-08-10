package io.github.StardewValley.models;

public class ShopProduct {
    private final ItemType item;
    private final int price;
    private int stock; // -1 for infinite

    public ShopProduct(ItemType item, int price, int stock) {
        this.item = item;
        this.price = price;
        this.stock = stock;
    }

    public ItemType getItem() { return item; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isOutOfStock() { return stock == 0; }

    /** reduce stock; no-op if infinite */
    public void take(int qty) { if (stock > 0) stock = Math.max(0, stock - qty); }
}