package io.github.StardewValley.models;

public class ItemStack {
    public final ItemType type;
    public int amount;

    public ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ItemType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
