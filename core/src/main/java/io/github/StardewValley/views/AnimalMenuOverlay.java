package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.*;

public class AnimalMenuOverlay {

    public interface Listener {
        default void onFed(Animal a) {}
        default void onPetted(Animal a) {}
        default void onCollected(Animal a, ItemType product, int amount) {}
        default void onSold(Animal a, int price) {}
    }

    private final GameController gc;
    private final Player player;
    private final Listener listener;

    private final Texture heart;

    private final Texture panel;
    private final Texture iconFeed;
    private final Texture iconPet;
    private final Texture iconCollect;
    private final Texture iconSell;
    private final Texture iconClose;

    private boolean visible = false;
    private Animal animal;

    private String msg = "";
    private long   msgUntilMs = 0;

    public AnimalMenuOverlay(GameController gc, Player player, Listener listener) {
        this.gc = gc;
        this.player = player;
        this.listener = (listener != null) ? listener : new Listener(){};

        this.panel      = new Texture("inventory/panel_bg.png");
        this.iconFeed   = new Texture("ring.png");
        this.iconPet    = new Texture("ring.png");
        this.iconCollect= new Texture("ring.png");
        this.iconSell   = new Texture("shops/sell.png");
        this.iconClose  = new Texture("shops/closed.png");
        this.heart = new Texture("inventory/heart.png");

    }

    public void dispose() {
        panel.dispose();
        iconFeed.dispose();
        iconPet.dispose();
        iconCollect.dispose();
        iconSell.dispose();
        iconClose.dispose();
        heart.dispose();
    }

    public boolean isVisible() { return visible; }

    public void open(Animal a) {
        if (a == null) return;
        this.animal = a;
        this.visible = true;
        this.msg = "";
        this.msgUntilMs = 0;
    }

    public void close() {
        visible = false;
        animal = null;
        msg = "";
    }

    private void toast(String text, int millis) {
        msg = text;
        msgUntilMs = System.currentTimeMillis() + millis;
    }

    public void render(SpriteBatch batch) {
        if (!visible || animal == null) return;

        float W = Gdx.graphics.getWidth();
        float H = Gdx.graphics.getHeight();

        float pw = 520, ph = 320;
        float px = (W - pw) / 2f;
        float py = (H - ph) / 2f;

        batch.setColor(1,1,1,1);
        batch.draw(panel, px, py, pw, ph);

        BitmapFont big   = GameAssetManager.getGameAssetManager().getBigFont();
        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();

        String title = animal.getSpecies().display + " — " + animal.getName();
        big.draw(batch, title, px + 18, py + ph - 16);

        String feedName = animal.getSpecies().feedItemId.toUpperCase();
        String prodName = animal.productItemId().toUpperCase();
        boolean ready = animal.canCollectProduct();

        small.draw(batch, "Feed: " + feedName, px + 18, py + ph - 48);
        // after:
        // After the product line:
        small.draw(batch, "Product: " + (ready ? ("Ready (" + prodName + ")") : "Not ready"), px + 18, py + ph - 68);

// --- Hearts row (4 hearts) + Level text ---
        float hx = px + 18f;
        float hy = py + ph - 88f;   // adjust spacing as needed
        float hsize = 16f, hgap = 3f;
        int hearts = animal.getFriendshipHearts(); // 0..4

        for (int i = 0; i < 4; i++) {
            if (i >= hearts) batch.setColor(1,1,1,0.25f);  // dim empty
            batch.draw(heart, hx + i * (hsize + hgap), hy - 13, hsize, hsize);
            batch.setColor(1,1,1,1);
        }

// numeric: "Level X/4 (YYY/400)"
        String lvlText = "Level " + animal.getFriendshipLevel() + "/4 (" + animal.getFriendship() + "/400)";
        small.draw(batch, lvlText, hx + 4 * (hsize + hgap) + 8f, hy + hsize - 2f - 13);

        small.draw(batch, "Tip: Q to close", px + 18, py + 22);

        float size = 72f;
        float gap  = 18f;
        float rowY = py + 64f;
        float cx = px + 40f;

        float feedX = cx,                feedY = rowY;
        float petX  = feedX + size + gap,  petY  = rowY;
        float collX = petX  + size + gap,  collY = rowY;
        float sellX = collX + size + gap,  sellY = rowY;
        float closeX = px + pw - 40f,     closeY = py + ph - 40f;

        ItemType feedItem = ItemCatalog.get(animal.getSpecies().feedItemId);
        boolean haveFeed = (feedItem != null) && player.getInventory().contains(feedItem, 1);
// Only enable if we have feed AND the animal allows feeding this cycle
        boolean canFeed = haveFeed && animal.canFeed();

        drawIcon(batch, iconFeed,   feedX, feedY, size, canFeed);
        drawIcon(batch, iconPet,    petX,  petY,  size, true);
        drawIcon(batch, iconCollect,collX, collY, size, ready);
        drawIcon(batch, iconSell,   sellX, sellY, size, true);
        drawIcon(batch, iconClose,  closeX-24, closeY-24, 24, true);

        small.draw(batch, "Feed",    feedX, feedY - 8);
        small.draw(batch, "Pet",     petX,  petY  - 8);
        small.draw(batch, "Collect", collX, collY - 8);
        small.draw(batch, "Sell",    sellX, sellY - 8);

        if (msg != null && !msg.isEmpty() && System.currentTimeMillis() < msgUntilMs) {
            GlyphLayout gl = new GlyphLayout(small, msg);
            float mx = px + (pw - gl.width) / 2f;
            float my = py + 36f;
            small.setColor(Color.SALMON);
            small.draw(batch, gl, mx, my);
            small.setColor(Color.WHITE);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            close();
            return;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = H - Gdx.input.getY();

            if (hit(mx, my, closeX-24, closeY-24, 24, 24)) { close(); return; }

            if (hit(mx, my, feedX, feedY, size, size)) {
                if (!haveFeed) { toast("You need " + feedName + ".", 1600); return; }

                // Ask the animal FIRST; do not spend the item yet
                Animal.FeedResult r = animal.tryFeed(feedItem);
                switch (r) {
                    case OK -> {
                        // spend only on success
                        player.getInventory().remove(feedItem, 1);
                        listener.onFed(animal);

                        // Prefer CropType.cropTexture(); fall back to iconPath(); finally to a heart
                        String icon = "inventory/heart.png";
                        if (feedItem instanceof CropType ct) {
                            String tex = ct.cropTexture();
                            icon = (tex != null && !tex.isEmpty()) ? tex
                                : (feedItem.iconPath() != null && !feedItem.iconPath().isEmpty() ? feedItem.iconPath() : icon);
                        } else if (feedItem != null && feedItem.iconPath() != null && !feedItem.iconPath().isEmpty()) {
                            icon = feedItem.iconPath();
                        }

                        gc.spawnFloatingIcon(icon, animal.getX(), animal.getY() + 16f, 0.9f);
                        toast("Fed successfully.", 1200);
                    }
                    case PRODUCT_READY -> toast("Product ready — collect it first.", 1600);
                    case ALREADY_FED   -> toast("Already fed this cycle.", 1400);
                    case WRONG_FOOD    -> toast("This animal doesn't eat that.", 1600);
                }
                return;
            }

            if (hit(mx, my, petX, petY, size, size)) {
                animal.pet();
                listener.onPetted(animal);
                gc.spawnFloatingIcon("inventory/heart.png", animal.getX(), animal.getY() + 16f, 0.9f);  // NEW
                toast("You petted " + animal.getName() + ".", 1200);
                return;
            }

            if (hit(mx, my, collX, collY, size, size)) {
                if (!ready) { toast("No product yet.", 1200); return; }
                ItemType product = ItemCatalog.get(animal.productItemId());
                if (product == null) { toast("Missing product item: " + prodName, 1600); return; }
                player.getInventory().add(product, 1);
                animal.onProductCollected();
                listener.onCollected(animal, product, 1);
                toast("Collected 1× " + product.id().toUpperCase() + ".", 1500);
                return;
            }

            if (hit(mx, my, sellX, sellY, size, size)) {
                int price = animal.sellPrice();
                gc.animals().remove(animal);
                player.getGameEconomy().addGold(price);
                listener.onSold(animal, price);
                toast("Sold for " + price + "g.", 1500);
                close();
            }
        }
    }

    private static boolean hit(float mx, float my, float x, float y, float w, float h) {
        return (mx >= x && mx <= x + w && my >= y && my <= y + h);
    }

    private static void drawIcon(SpriteBatch batch, Texture tex, float x, float y, float size, boolean enabled) {
        if (!enabled) batch.setColor(1,1,1,0.35f);
        batch.draw(tex, x, y, size, size);
        batch.setColor(1,1,1,1);
    }
}
