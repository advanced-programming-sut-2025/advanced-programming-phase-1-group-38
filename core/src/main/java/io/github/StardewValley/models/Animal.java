package io.github.StardewValley.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animal {
    private final String id;
    private final String ownerId;
    private final AnimalSpecies species;
    private String name;

    private String mapId;
    private float x, y;

    // Game-hour production
    private float produceHoursRemaining;
    private boolean productReady;
    private int friendship;

    private static final int MAX_FRIENDSHIP  = 400;  // 4 hearts × 100 pts each
    private static final int PET_FRIENDSHIP  = 12;   // tweak as you like
    private static final int FEED_FRIENDSHIP = 5;

    // gating: one feed per production cycle
    private boolean fedThisCycle = false;

    // ---- Feeding results for UI messaging ----
    public enum FeedResult { OK, WRONG_FOOD, PRODUCT_READY, ALREADY_FED }

    public Animal(String id, String ownerId, AnimalSpecies sp, String name, String mapId, float x, float y) {
        this.id = id; this.ownerId = ownerId; this.species = sp; this.name = name;
        this.mapId = mapId; this.x = x; this.y = y;
        this.produceHoursRemaining = sp.intervalHours;
        this.productReady = false;
        this.friendship = 0;
    }

    public void update(float dt) {
        // production is advanced in game hours by GameController.updateMachines(...)
    }

    /** Advance by *game* hours. */
    public void advanceGameHours(float hours) {
        if (productReady) return;
        produceHoursRemaining -= hours;
        if (produceHoursRemaining <= 0f) {
            productReady = true;
            produceHoursRemaining = 0f;
        }
    }

    // Animal.java
    public void render(SpriteBatch batch) {
        String path = (species == AnimalSpecies.CHICKEN) ? "animals/chicken.png" : "animals/cow.png";
        Texture tex = GameAssetManager.getGameAssetManager().getTexture(path);
        tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        float w = species.drawW;
        float h = species.drawH;

        float drawX = x - w * 0.5f;
        float drawY = y;
        batch.draw(tex, drawX, drawY, w, h);
    }

    // ---------- Interactions ----------
    public boolean canCollectProduct() { return productReady; }
    public String  productItemId()     { return species.productItemId; }

    public void onProductCollected() {
        productReady = false;
        produceHoursRemaining = species.intervalHours;
        fedThisCycle = false; // new cycle
        bumpFriendship(8);
    }

    /** Quick check to drive button enabling. */
    public boolean canFeed() { return !productReady && !fedThisCycle; }

    /** If feeding is blocked, return a short reason (else null). */
    public String feedBlockReason() {
        if (productReady) return "Product is ready. Collect it first.";
        if (fedThisCycle) return "Already fed this cycle.";
        return null;
    }

    /**
     * Preferred API: try to feed using the actual ItemType. Nothing is consumed here;
     * caller should remove the item from inventory only when result == OK.
     */

    public FeedResult tryFeed(ItemType feedItem) {
        ItemType required = ItemCatalog.get(species.feedItemId);
        if (required == null || feedItem != required) return FeedResult.WRONG_FOOD;
        if (productReady)   return FeedResult.PRODUCT_READY;
        if (fedThisCycle)   return FeedResult.ALREADY_FED;

        produceHoursRemaining = Math.max(0.25f, produceHoursRemaining - 1f); // speed up 1h
        fedThisCycle = true;
        bumpFriendship(5);
        return FeedResult.OK;
    }

    /**
     * Back-compat wrapper for old callers. Returns true only if feeding succeeded.
     * (Use tryFeed(...) if you want the specific reason.)
     */
    public boolean feed(ItemType feedItem) {
        return tryFeed(feedItem) == FeedResult.OK;
    }

    public void pet() { bumpFriendship(3); }
    public int sellPrice() { return species.sellPrice + friendship / 5; }

    // Hearts = 0..10 for 0..1000 friendship
    // --- Friendship accessors for 4-heart system ---
    public int   getFriendship()        { return friendship; }                   // 0..400
    public int   getFriendshipHearts()  { return Math.min(4, friendship / 100); } // 0..4
    public int   getFriendshipLevel()   { return getFriendshipHearts(); }         // alias, 0..4
    public float getFriendship01()      { return Math.min(1f, friendship / 400f); }



    private void bumpFriendship(int pts) { friendship = Math.min(MAX_FRIENDSHIP, friendship + Math.max(0, pts)); }

    // getters
    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public AnimalSpecies getSpecies() { return species; }
    public String getName() { return name; }
    public String getMapId() { return mapId; }
    public float getX() { return x; }
    public float getY() { return y; }
}
