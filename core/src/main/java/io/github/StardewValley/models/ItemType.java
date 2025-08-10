package io.github.StardewValley.models;

public interface ItemType {
    String id();
    String iconPath();
    /** 0 means “unlimited”; 1 means non‑stackable; anything else is a hard cap. */
    int maxStack();
    default boolean stackable() { return maxStack() != 1; }
}
