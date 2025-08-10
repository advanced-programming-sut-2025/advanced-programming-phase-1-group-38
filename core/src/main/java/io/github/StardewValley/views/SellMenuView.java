package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.*;

import java.util.ArrayList;
import java.util.List;

/** Simple selling menu opened from the bin icon inside Inventory. */
public class SellMenuView {
    private final Inventory inventory;
    private final Texture panel, slot, slotSel, btnSell, btnPlus, btnMinus;
    private boolean visible = false;
    private int sel = 0, qty = 1;
    private final List<ItemType> items = new ArrayList<>();

    public SellMenuView(Inventory inv) {
        this.inventory = inv;
        this.panel = new Texture("inventory/panel_bg.png");
        this.slot  = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.btnSell = new Texture("inventory/cook_button.png");
        this.btnPlus = new Texture("inventory/arrow_right.png");
        this.btnMinus = new Texture("inventory/arrow_left.png");
        refresh();
    }

    public void refresh() {
        items.clear();
        // هر کدوم از این enum ها که داری رو صدا بزن
        addNonZeroFromEnum(SeedType.values());
        addNonZeroFromEnum(CropType.values());
        addNonZeroFromEnum(FoodType.values());
        sel = Math.min(sel, Math.max(0, items.size() - 1));
        // اگر انتخاب خالی شد، مقدار qty رو ریست کن
        if (items.isEmpty()) qty = 1;
    }
    private <T extends ItemType> void addNonZeroFromEnum(T[] all) {
        for (T t : all) {
            int have = inventory.getTotalQty(t);
            if (have > 0 /* && !(t instanceof ToolType) ← لازم نیست چون ToolType تو این enumها نیست */) {
                items.add(t);
            }
        }
    }

    public void toggle() { visible = !visible; if (visible) refresh(); }
    public boolean isVisible() { return visible; }

    public void render(SpriteBatch batch) {
        if (!visible) return;
        float W = Gdx.graphics.getWidth(), H = Gdx.graphics.getHeight();
        float pw=560, ph=420, px=(W-pw)/2f, py=(H-ph)/2f; batch.draw(panel, px, py, pw, ph);

        BitmapFont big = GameAssetManager.getGameAssetManager().getBigFont();
        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
        big.draw(batch, "Sell", px+16, py+ph-16);

        // grid of items from inventory
        int COLS=6, ROWS=3; float pad=8, cell=72; float gx=px+16, gy=py+ph-64;
        int max = Math.min(items.size(), COLS*ROWS);
        for (int i=0;i<max;i++) {
            int r=i/COLS, c=i%COLS; float cx=gx+c*(cell+pad), cy=gy-r*(cell+pad)-cell;
            batch.draw(slot, cx, cy, cell, cell);
            if (i==sel) batch.draw(slotSel, cx-3, cy-3, cell+6, cell+6);
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(items.get(i).iconPath());
            batch.draw(icon, cx+16, cy+16, 40, 40);
        }

        if (!items.isEmpty()) {
            ItemType t = items.get(sel);
            int have = inventory.getTotalQty(t);
            qty = Math.min(qty, Math.max(1, have));  // سقفِ qty = موجودی
            int price = getSellPrice(t);
            float dx = px+pw-220, dy=py+120;
            small.draw(batch, t.id(), dx, dy+120);
            small.draw(batch, "You have: "+have, dx, dy+100);
            small.draw(batch, "Unit: "+price+"g", dx, dy+80);
            small.draw(batch, "Gold: "+GameEconomy.getGold(), dx, dy+60);

            // qty
            batch.draw(btnMinus, dx, dy+20, 28, 28);
            GlyphLayout ql = new GlyphLayout(big, String.valueOf(qty));
            big.draw(batch, ql, dx+40, dy+42);
            batch.draw(btnPlus, dx+90, dy+20, 28, 28);
            batch.draw(btnSell, dx, dy-10, 150, 44);
            small.draw(batch, "Enter = Sell", dx+20, dy+18);

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx=Gdx.input.getX(), my=H-Gdx.input.getY();
                if (mx>=dx && mx<=dx+28 && my>=dy+20 && my<=dy+48) qty=Math.max(1, qty-1);
                if (mx>=dx+90 && mx<=dx+118 && my>=dy+20 && my<=dy+48) qty=Math.min(have, qty+1);
                if (mx>=dx && mx<=dx+150 && my>=dy-10 && my<=dy+34) trySell(t);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) qty=Math.max(1, qty-1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) qty=Math.min(have, qty+1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) trySell(t);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) visible=false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  sel=Math.max(0, sel-1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) sel=Math.min(items.size()-1, sel+1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))    sel=Math.max(0, sel-6);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))  sel=Math.min(items.size()-1, sel+6);
    }

    private int getSellPrice(ItemType t) {
        // basic rule: half of a guessed buy price
        if (t instanceof SeedType) return 10;
        if (t instanceof CropType) return 20;
        if (t instanceof FoodType) return 30;
        return 5;
    }

    private void trySell(ItemType t) {
        int have = inventory.getTotalQty(t);
        int real = Math.min(qty, have);
        if (real <= 0) return;
        int value = getSellPrice(t) * real;
        inventory.remove(t, real);
        GameEconomy.addGold(value);
        refresh();
    }
}

