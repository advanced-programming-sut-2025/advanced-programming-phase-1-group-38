package io.github.StardewValley.models;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Central registry for ItemType singletons used by inventory, crafting, etc. */
public final class ItemCatalog {

    // --- Internal registry (in insertion order for nice listing) ---
    private static final Map<String, ItemType> REGISTRY = new LinkedHashMap<>();

    // --- Simple concrete item for stackables when no existing type exists ---
    private static final class BasicItem implements ItemType {
        private final String id;
        private final String title;
        private final String spritePath;
        private final int maxStack;

        BasicItem(String id, String title, String spritePath, int maxStack) {
            this.id = id;
            this.title = title;
            this.spritePath = spritePath;
            this.maxStack = maxStack;
        }

        @Override public String id()       { return id; }
        public String title()              { return title; }
        public String spritePath()         { return spritePath; }
        @Override public String iconPath() { return spritePath; }
        @Override public int maxStack()    { return maxStack; }

        @Override public String toString() { return "Item(" + id + ")"; }
    }

    // --- Built-in items (singletons) ---

    // Feed — use the SAME instances as your crops, so identity matches inventory contents.
    private static final ItemType WHEAT = CropType.WHEAT;
    private static final ItemType CORN  = CropType.CORN;

    // Products — keep BasicItem unless you have dedicated product types elsewhere.
    private static final BasicItem EGG = new BasicItem("egg", "Egg", "items/Egg.png", 0);
    private static final BasicItem MILK = new BasicItem("milk", "Milk", "items/Milk.png", 0);

    // Animal crates
    private static final AnimalCrateType CHICKEN_CRATE = new AnimalCrateType(AnimalSpecies.CHICKEN);
    private static final AnimalCrateType COW_CRATE     = new AnimalCrateType(AnimalSpecies.COW);

    // --- Static init: register everything once ---
    static {
        // Register using each item's own id (whatever it is)
        register(WHEAT);
        register(CORN);
        register(EGG);
        register(MILK);
        register(CHICKEN_CRATE);
        register(COW_CRATE);

        // Optional: ensure friendly lowercase lookups resolve to the same instances,
        // in case CropType ids are not exactly "wheat"/"corn".
        alias("wheat", WHEAT);
        alias("corn",  CORN);
        alias("egg",   EGG);
        alias("milk",  MILK);
    }

    private ItemCatalog() {}

    // --- Public API ---

    /** Lookup an item by its id (e.g., "wheat", "egg"). Returns null if not found. */
    public static ItemType get(String id) {
        if (id == null) return null;
        return REGISTRY.get(id.toLowerCase());
    }

    /** True if an item with this id is registered. */
    public static boolean exists(String id) {
        return get(id) != null;
    }

    /** Immutable view of all registered items. */
    public static Collection<ItemType> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    // Convenience accessors
    public static ItemType wheat()        { return WHEAT; }
    public static ItemType corn()         { return CORN; }
    public static ItemType egg()          { return EGG; }
    public static ItemType milk()         { return MILK; }
    public static AnimalCrateType chickenCrate() { return CHICKEN_CRATE; }
    public static AnimalCrateType cowCrate()     { return COW_CRATE; }

    // --- Extensibility ---

    /** Register a new singleton item under its own id (case-insensitive). Replaces if exists. */
    public static void register(ItemType item) {
        if (item == null || item.id() == null || item.id().isEmpty()) {
            throw new IllegalArgumentException("Item or item.id is null/empty");
        }
        REGISTRY.put(item.id().toLowerCase(), item);
    }

    /** Also map an extra id/alias to the same instance (case-insensitive). */
    public static void alias(String id, ItemType target) {
        if (id == null || id.isEmpty() || target == null) return;
        REGISTRY.put(id.toLowerCase(), target);
    }
}
