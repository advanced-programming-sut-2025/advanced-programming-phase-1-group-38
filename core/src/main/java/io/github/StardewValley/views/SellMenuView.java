package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Sell menu that shows *all* inventory items; tools are visible but unsellable. */
public class SellMenuView {
    private final Inventory inventory;
    private final GameEconomy gameEconomy;

    private final Texture panel, slot, slotSel, btnSell, btnPlus, btnMinus;
    private boolean visible = false;
    private int sel = 0, qty = 1;

    private final List<ItemType> items = new ArrayList<>();

    // Error banner (UI)
    private String errorMessage = null;
    private long   errorMessageEndTime = 0L;

    public SellMenuView(Inventory inv, GameEconomy gameEconomy) {
        this.inventory = inv;
        this.gameEconomy = gameEconomy;
        this.panel   = new Texture("inventory/panel_bg.png");
        this.slot    = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.btnSell = new Texture("shops/sell.png");
        this.btnPlus = new Texture("inventory/arrow_right.png");
        this.btnMinus= new Texture("inventory/arrow_left.png");
        refresh();
    }

    /** Rebuild list from actual inventory slots; includes tools too. */
    public void refresh() {
        items.clear();
        Set<ItemType> seen = new LinkedHashSet<>();

        int n = inventory.size();
        for (int i = 0; i < n; i++) {
            Inventory.Stack s = inventory.peek(i);
            if (s == null) continue;
            ItemType t = s.getType();
            if (t == null) continue;
            if (s.getQty() > 0) seen.add(t); // include tools too
        }

        items.addAll(seen);

        sel = Math.min(sel, Math.max(0, items.size() - 1));
        if (items.isEmpty()) qty = 1;
    }

    public void toggle() { visible = !visible; if (visible) refresh(); }
    public boolean isVisible() { return visible; }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        float W = Gdx.graphics.getWidth(), H = Gdx.graphics.getHeight();
        float pw = 560, ph = 420, px = (W - pw) / 2f, py = (H - ph) / 2f;

        batch.draw(panel, px, py, pw, ph);

        BitmapFont big   = GameAssetManager.getGameAssetManager().getBigFont();
        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
        big.draw(batch, "Sell", px + 16, py + ph - 16);

        // Grid
        int COLS = 6, ROWS = 3; float pad = 8, cell = 72;
        float gx = px + 16, gy = py + ph - 64;
        int max = Math.min(items.size(), COLS * ROWS);

        for (int i = 0; i < max; i++) {
            int r = i / COLS, c = i % COLS;
            float cx = gx + c * (cell + pad);
            float cy = gy - r * (cell + pad) - cell;

            batch.draw(slot, cx, cy, cell, cell);
            if (i == sel) batch.draw(slotSel, cx - 3, cy - 3, cell + 6, cell + 6);

            ItemType t = items.get(i);
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(iconPathFor(t));

            // Optionally dim tools in the grid (to hint they’re not sellable)
            if (t instanceof ToolType) batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(icon, cx + 16, cy + 16, 40, 40);
            batch.setColor(1f, 1f, 1f, 1f);
        }

        if (!items.isEmpty()) {
            ItemType t = items.get(sel);
            boolean sellable = isSellable(t);

            int have = inventory.getTotalQty(t);
            qty = Math.min(qty, Math.max(1, have)); // clamp
            if (!sellable) qty = 1;                 // tools are not sellable; force 1

            int price = sellable ? getSellPrice(t) : 0;
            float dx = px + pw - 220, dy = py + 120;

            small.draw(batch, t.id(), dx, dy + 120);
            small.draw(batch, "You have: " + have, dx, dy + 100);
            if (sellable) {
                small.draw(batch, "Unit: " + price + "g", dx, dy + 80);
            } else {
                small.draw(batch, "Not sellable here", dx, dy + 80);
            }
            small.draw(batch, "Gold: " + gameEconomy.getGold(), dx, dy + 60);

            // Qty controls
            float minusX = dx, minusY = dy + 20;
            float plusX  = dx + 90, plusY = dy + 20;

            // Disable qty buttons for non-sellable items (visual + functional)
            if (!sellable) batch.setColor(1f, 1f, 1f, 0.4f);
            batch.draw(btnMinus, minusX, minusY, 28, 28);
            GlyphLayout ql = new GlyphLayout(big, String.valueOf(qty));
            big.draw(batch, ql, dx + 40, dy + 42);
            batch.draw(btnPlus, plusX, plusY, 28, 28);
            batch.setColor(1f, 1f, 1f, 1f);

            // Sell button (consistent rect for drawing + hit-test)
            float sellX = dx, sellY = dy - 60, sellW = 110f, sellH = 80f;
            if (!sellable) batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(btnSell, sellX, sellY, sellW, sellH);
            batch.setColor(1f, 1f, 1f, 1f);

            small.draw(batch, "Enter = Sell", dx + 20, dy + 18);

            // Error banner
            if (errorMessage != null) {
                if (System.currentTimeMillis() < errorMessageEndTime) {
                    big.setColor(1f, 0.25f, 0.25f, 1f);
                    big.draw(batch, errorMessage, px + 16, py + ph - 44);
                    big.setColor(1f, 1f, 1f, 1f);
                } else {
                    errorMessage = null;
                }
            }

            // Mouse input
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX(), my = H - Gdx.input.getY();

                // qty buttons (ignore when not sellable)
                if (sellable && mx >= minusX && mx <= minusX + 28 && my >= minusY && my <= minusY + 28)
                    qty = Math.max(1, qty - 1);

                if (sellable && mx >= plusX && mx <= plusX + 28 && my >= plusY && my <= plusY + 28)
                    qty = Math.min(have, qty + 1);

                // sell button
                if (mx >= sellX && mx <= sellX + sellW && my >= sellY && my <= sellY + sellH) {
                    if (sellable) trySell(t);
                    else showError("You can’t sell tools here.");
                }
            }

            // Keyboard input
            if (sellable && Gdx.input.isKeyJustPressed(Input.Keys.MINUS))   qty = Math.max(1, qty - 1);
            if (sellable && Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))  qty = Math.min(have, qty + 1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (sellable) trySell(t);
                else showError("You can’t sell tools here.");
            }
        }

        // Navigation + close
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) visible = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  sel = Math.max(0, sel - 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) sel = Math.min(items.size() - 1, sel + 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))    sel = Math.max(0, sel - 6);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))  sel = Math.min(items.size() - 1, sel + 6);
    }

    /** Simple rule-of-thumb sell prices. */
    private int getSellPrice(ItemType t) {
        if (t instanceof SeedType)  return 10;
        if (t instanceof CropType)  return 20;
        if (t instanceof FoodType)  return 30;
        return 10;
    }

    /** Only non-tools are sellable here. */
    private boolean isSellable(ItemType t) {
        return !(t instanceof ToolType);
    }

    private void trySell(ItemType t) {
        if (!isSellable(t)) {
            showError("You can’t sell tools here.");
            return;
        }
        int have = inventory.getTotalQty(t);
        int real = Math.min(qty, have);
        if (real <= 0) return;

        int value = getSellPrice(t) * real;
        inventory.remove(t, real);
        gameEconomy.addGold(value);
        refresh();
    }

    private void showError(String msg) {
        errorMessage = msg;
        errorMessageEndTime = System.currentTimeMillis() + 2000; // 2s
    }

    private String iconPathFor(ItemType t) {
        if (t instanceof CropType c) return c.cropTexture();
        return t.iconPath();
    }

    public void dispose() {
        panel.dispose();
        slot.dispose();
        slotSel.dispose();
        btnSell.dispose();
        btnPlus.dispose();
        btnMinus.dispose();
    }
}
