package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.enums.Shop.ShopEntry;
import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple shop UI per spec: shows all products, allows filter, darkens out-of-stock,
 * shows +/- quantity, and Buy with stock/money checks. ESC closes.
 */
public class ShopView {
    private final Inventory inventory;
    private  final GameEconomy gameEconomy;
    private final List<ShopProduct> all;
    private final List<ShopProduct> filtered = new ArrayList<>();

    private String errorMessage = null;
    private long errorMessageEndTime = 0;

    private final Texture panel;
    private final Texture slot;
    private final Texture slotSel;
    private final Texture btnBuy;
    private final Texture btnPlus;
    private final Texture btnMinus;
    private String title = "Shop";

    private static final int FILTER_ALL = 0;
    private static final int FILTER_SEEDS = 1;
    private static final int FILTER_TOOLS = 2;
    private static final int FILTER_FOOD = 3;
    private static final int FILTER_MATERIALS = 4;
    private static final int FILTER_IN_STOCK = 5;
    private static final int FILTER_OUT_OF_STOCK = 6;

    private int filter = FILTER_ALL;

    private boolean visible = false;
    private int sel = 0;
    private int qty = 1;


    public ShopView(Inventory inv, List<ShopProduct> products, GameEconomy gameEconomy) {
        this.inventory = inv;
        this.all = products;
        this.gameEconomy = gameEconomy;
        this.filtered.addAll(products);
        this.panel = new Texture("inventory/panel_bg.png");
        this.slot  = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.btnBuy = new Texture("shops/buy.png");
        this.btnPlus = new Texture("inventory/arrow_right.png");
        this.btnMinus = new Texture("inventory/arrow_left.png");
    }

    public void toggle() { visible = !visible; }
    public void setVisible(boolean v) { visible = v; }
    public boolean isVisible() { return visible; }

    public void dispose() {
        panel.dispose(); slot.dispose(); slotSel.dispose();
        btnBuy.dispose(); btnPlus.dispose(); btnMinus.dispose();
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;
        float W = Gdx.graphics.getWidth();
        float H = Gdx.graphics.getHeight();

        float pw = 640, ph = 460;
        float px = (W - pw) / 2f;
        float py = (H - ph) / 2f;
        batch.draw(panel, px, py, pw, ph);

        BitmapFont big = GameAssetManager.getGameAssetManager().getBigFont();
        big.draw(batch, title, px + 16, py + ph - 16);

        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();

        // Filters
        String[] tabs = {"All", "Seeds", "Tools", "Food", "Materials", "In Stock", "Out of Stock"};
        float tabX = px + 16, tabY = py + ph - 44;
        for (int i = 0; i < tabs.length; i++) {
            String t = (i == filter ? "[" + tabs[i] + "]" : tabs[i]);
            small.draw(batch, t, tabX, tabY);
            tabX += 80; // a little wider to fit labels
        }

        // Grid
        int COLS = 5, ROWS = 3; float pad = 10, cell = 80;
        float gx = px + 16, gy = py + ph - 80;
        int start = 0, end = Math.min(filtered.size(), COLS * ROWS);
        for (int i = start; i < end; i++) {
            int r = (i - start) / COLS, c = (i - start) % COLS;
            float cx = gx + c * (cell + pad);
            float cy = gy - r * (cell + pad) - cell;
            ShopProduct p = filtered.get(i);
            batch.draw(slot, cx, cy, cell, cell);
            if (i == sel) batch.draw(slotSel, cx - 4, cy - 4, cell + 8, cell + 8);
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(p.getItem().iconPath());
            if (p.isOutOfStock()) batch.setColor(1, 1, 1, 0.35f);
            batch.draw(icon, cx + 16, cy + 16, 48, 48);
            batch.setColor(1, 1, 1, 1);
            small.draw(batch, p.getItem().id(), cx, cy - 4);
            small.draw(batch, p.getPrice() + "g", cx + cell - 40, cy - 4);
        }

        // Detail + qty + Buy/Sell
        if (!filtered.isEmpty()) {
            ShopProduct p = filtered.get(sel);
            float dx = px + pw - 220, dy = py + 120;
            small.draw(batch, p.getItem().id(), dx, dy + 120);
            small.draw(batch, "Price: " + p.getPrice() + "g", dx, dy + 100);
            int stock = p.getStock();
            String stockText = (stock == Integer.MAX_VALUE) ? "unlimited" : String.valueOf(stock);
            small.draw(batch, "Stock: " + stockText, dx, dy + 80);
            small.draw(batch, "Gold: " + gameEconomy.getGold(), dx, dy + 60);

            // qty controls
            float minusX = dx;
            float minusY = dy + 20;
            float plusX = dx + 90;
            float plusY = dy + 20;

            batch.draw(btnMinus, minusX, minusY, 28, 28);
            GlyphLayout ql = new GlyphLayout(big, String.valueOf(qty));
            big.draw(batch, ql, dx + 40, dy + 42);
            batch.draw(btnPlus, plusX, plusY, 28, 28);

            // Buy/Sell button rect (single source of truth)
            float buyX = dx;
            float buyY = dy - 70;
            float buyW = 110f;
            float buyH = 80f;
            batch.draw(btnBuy, buyX, buyY, buyW, buyH);
            small.draw(batch, "Enter = Buy", dx + 16, dy + 18);

            // mouse
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX();
                float my = H - Gdx.input.getY(); // flip Y for UI coords

                // qty buttons
                if (mx >= minusX && mx <= minusX + 28 && my >= minusY && my <= minusY + 28)
                    qty = Math.max(1, qty - 1);
                if (mx >= plusX && mx <= plusX + 28 && my >= plusY && my <= plusY + 28)
                    qty = Math.min(99, qty + 1);

                // buy/sell button
                if (mx >= buyX && mx <= buyX + buyW && my >= buyY && my <= buyY + buyH)
                    tryBuy(p);
            }
        }

        if (errorMessage != null && System.currentTimeMillis() < errorMessageEndTime) {
            big.setColor(1, 0.2f, 0.2f, 1f); // red
            big.draw(batch, errorMessage, px + pw/2f - 80, py + ph - 60); // adjust position
            big.setColor(1, 1, 1, 1f); // reset color
        } else if (errorMessage != null) {
            errorMessage = null; // clear after time expires
        }

        // input keys
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) visible = false;

        for (int i = 0; i < 7; i++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                filter = i;          // 0..6
                applyFilter();
                sel = Math.min(sel, Math.max(0, filtered.size() - 1));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) sel = Math.max(0, sel - 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) sel = Math.min(filtered.size() - 1, sel + 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) sel = Math.max(0, sel - 5);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) sel = Math.min(filtered.size() - 1, sel + 5);

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) qty = Math.max(1, qty - 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) qty = Math.min(99, qty + 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !filtered.isEmpty()) tryBuy(filtered.get(sel));

        // filter keys 1..5
        for (int i = 0; i < 5; i++)
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                filter = i;
                applyFilter();
                sel = 0;
            }
    }

    private void applyFilter() {
        filtered.clear();
        for (ShopProduct p : all) {
            if (!matchesCategory(p, filter)) continue;
            if (!matchesStock(p, filter)) continue;
            filtered.add(p);
        }
    }

    private boolean matchesCategory(ShopProduct p, int f) {
        return switch (f) {
            case FILTER_SEEDS      -> p.getItem() instanceof SeedType;
            case FILTER_TOOLS      -> p.getItem() instanceof ToolType;
            case FILTER_FOOD       -> p.getItem() instanceof FoodType;
            case FILTER_MATERIALS  -> p.getItem() instanceof MaterialType;
            default                -> true; // All / In Stock / Out of Stock handled elsewhere
        };
    }

    private boolean matchesStock(ShopProduct p, int f) {
        int stock = p.getStock(); // Integer.MAX_VALUE means unlimited
        boolean inStock = (stock == Integer.MAX_VALUE) || (stock > 0);
        return switch (f) {
            case FILTER_IN_STOCK   -> inStock;
            case FILTER_OUT_OF_STOCK -> !inStock; // i.e., stock == 0
            default                -> true;
        };
    }

    private void tryBuy(ShopProduct p) {
        if (p.isOutOfStock()) return;

        int want = qty;
        int stock = p.getStock();
        if (stock > 0 && stock != Integer.MAX_VALUE) {
            want = Math.min(want, stock);
        }

        int total = p.getPrice() * want;

        // --- Show graphical error BEFORE invoking listener ---
        int goldNow = gameEconomy.getGold(); // or gameEconomy.hasGold(total) if you have it
        if (goldNow < total) {
            errorMessage = "Not enough gold!";
            errorMessageEndTime = System.currentTimeMillis() + 2000; // 2s
            return;
        }

        // Prefer listener (it will actually spend gold / handle tools, etc.)
        if (listener != null && p.getEntry() != null) {
            boolean ok = listener.onBuy(p.getShopType(), p.getEntry(), want);
            if (!ok) {
                // If your listener can fail for other reasons, you can set other messages here.
                // For gold shortage we already handled above, so do nothing.
                return;
            }
            if (stock != Integer.MAX_VALUE) p.take(want);
            return;
        }

        // Fallback path (ShopView handles payment and stock)
        if (!gameEconomy.spendGold(total)) {
            // Defensive: in case spendGold can still fail (race, etc.)
            errorMessage = "Not enough gold!";
            errorMessageEndTime = System.currentTimeMillis() + 2000;
            return;
        }

        if (stock != Integer.MAX_VALUE) p.take(want);
        inventory.add(p.getItem(), want);
    }


    // ShopView.java
    public interface PurchaseListener {
        boolean onBuy(io.github.StardewValley.models.enums.Types.ShopType shop, ShopEntry entry, int qty);
    }

    public void setCatalog(List<ShopProduct> products) {
        this.all.clear();
        this.all.addAll(products);
        applyFilter();
        sel = 0;
    }

    public void setTitle(String t){ this.title = t; }

    private PurchaseListener listener;
    public void setPurchaseListener(PurchaseListener l){ this.listener = l; }
// call listener.onBuy(...) when the user clicks “Buy”

}
