package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import io.github.StardewValley.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple shop UI per spec: shows all products, allows filter, darkens out-of-stock,
 * shows +/- quantity, and Buy with stock/money checks. ESC closes.
 */
public class ShopView {
    private final Inventory inventory;
    private final List<ShopProduct> all;
    private final List<ShopProduct> filtered = new ArrayList<>();

    private final Texture panel;
    private final Texture slot;
    private final Texture slotSel;
    private final Texture btnBuy;
    private final Texture btnPlus;
    private final Texture btnMinus;

    private boolean visible = false;
    private int sel = 0;
    private int qty = 1;

    private int filter = 0; // 0=All, 1=Seeds, 2=Tools, 3=Food, 4=Materials

    public ShopView(Inventory inv, List<ShopProduct> products) {
        this.inventory = inv;
        this.all = products;
        this.filtered.addAll(products);
        this.panel = new Texture("inventory/panel_bg.png");
        this.slot  = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.btnBuy = new Texture("inventory/cook_button.png");
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
        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
        big.draw(batch, "Shop", px + 16, py + ph - 16);

        // Filters
        String[] tabs = {"All", "Seeds", "Tools", "Food", "Materials"};
        float tabX = px + 16, tabY = py + ph - 44;
        for (int i = 0; i < tabs.length; i++) {
            String t = (i == filter ? "["+tabs[i]+"]" : tabs[i]);
            small.draw(batch, t, tabX, tabY);
            tabX += 90;
        }

        // Grid
        int COLS = 5, ROWS = 3; float pad = 10, cell = 80;
        float gx = px + 16, gy = py + ph - 80;
        int start = 0, end = Math.min(filtered.size(), COLS*ROWS);
        for (int i = start; i < end; i++) {
            int r = (i-start)/COLS, c = (i-start)%COLS;
            float cx = gx + c*(cell+pad);
            float cy = gy - r*(cell+pad) - cell;
            ShopProduct p = filtered.get(i);
            batch.draw(slot, cx, cy, cell, cell);
            if (i == sel) batch.draw(slotSel, cx-4, cy-4, cell+8, cell+8);
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(p.getItem().iconPath());
            if (p.isOutOfStock()) batch.setColor(1,1,1,0.35f);
            batch.draw(icon, cx+16, cy+16, 48, 48);
            batch.setColor(1,1,1,1);
            small.draw(batch, p.getItem().id(), cx, cy-4);
            small.draw(batch, p.getPrice()+"g", cx+cell-40, cy-4);
        }

        // Detail + qty + Buy
        if (!filtered.isEmpty()) {
            ShopProduct p = filtered.get(sel);
            float dx = px + pw - 220, dy = py + 120;
            small.draw(batch, p.getItem().id(), dx, dy+120);
            small.draw(batch, "Price: "+p.getPrice()+"g", dx, dy+100);
            small.draw(batch, "Stock: "+(p.getStock()<0?"∞":p.getStock()), dx, dy+80);
            small.draw(batch, "Gold: "+ GameEconomy.getGold(), dx, dy+60);

            // qty controls
            batch.draw(btnMinus, dx, dy+20, 28, 28);
            GlyphLayout ql = new GlyphLayout(big, String.valueOf(qty));
            big.draw(batch, ql, dx+40, dy+42);
            batch.draw(btnPlus, dx+90, dy+20, 28, 28);

            // BUY
            batch.draw(btnBuy, dx, dy-10, 150, 44);
            small.draw(batch, "Enter = Buy", dx+16, dy+18);

            // mouse
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX();
                float my = H - Gdx.input.getY();
                if (mx>=dx && mx<=dx+28 && my>=dy+20 && my<=dy+48) qty = Math.max(1, qty-1);
                if (mx>=dx+90 && mx<=dx+118 && my>=dy+20 && my<=dy+48) qty = Math.min(99, qty+1);
                if (mx>=dx && mx<=dx+150 && my>=dy-10 && my<=dy+34) tryBuy(p);
            }
        }

        // input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) visible = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  sel = Math.max(0, sel-1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) sel = Math.min(filtered.size()-1, sel+1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))    sel = Math.max(0, sel-5);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))  sel = Math.min(filtered.size()-1, sel+5);

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) qty = Math.max(1, qty-1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) qty = Math.min(99, qty+1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !filtered.isEmpty()) tryBuy(filtered.get(sel));

        // filter keys 1..5
        for (int i=0;i<5;i++) if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1+i)) { filter=i; applyFilter(); sel=0; }
    }

    private void applyFilter() {
        filtered.clear();
        for (ShopProduct p : all) {
            switch (filter) {
                case 1: if (p.getItem() instanceof SeedType)   filtered.add(p); break;
                case 2: if (p.getItem() instanceof ToolType)   filtered.add(p); break;
                case 3: if (p.getItem() instanceof FoodType)   filtered.add(p); break;
                // case 4: if (p.getItem() instanceof MaterialType) filtered.add(p); break;
                default: filtered.add(p);
            }
        }
    }

    private void tryBuy(ShopProduct p) {
        if (p.isOutOfStock()) return;
        int total = p.getPrice() * qty;
        if (!GameEconomy.spendGold(total)) return; // not enough gold
        // clamp qty to stock
        int real = qty;
        if (p.getStock() > 0) real = Math.min(qty, p.getStock());
        p.take(real);
        inventory.add(p.getItem(), real);
    }
}