package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.models.*;

import java.util.List;
import java.util.Map;

public class CraftingMenuView {

    private final Inventory inventory;
    private final List<SimpleCraftingRecipe> recipes;

    private final Texture panelBg;
    private final Texture slotBg;
    private final Texture slotSel;
    private final Texture leftArrow;
    private final Texture rightArrow;
    private final Texture craftButton;

    private boolean visible = false;

    private int selected = 0;
    private int page = 0;
    private static final int COLS = 6;
    private static final int ROWS = 4;
    private static final int PER_PAGE = COLS * ROWS;

    // cached arrow layout
    private float arrowSize, arrowsY, leftX, rightX;

    private String feedback = null;
    private float feedbackTimer = 0f;

    public CraftingMenuView(Inventory inventory, List<SimpleCraftingRecipe> recipes) {
        this.inventory = inventory;
        this.recipes = recipes;
        this.panelBg = new Texture("inventory/panel_bg.png");
        this.slotBg = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.leftArrow  = new Texture("inventory/arrow_left.png");
        this.rightArrow = new Texture("inventory/arrow_right.png");
        this.craftButton = new Texture("inventory/Craft-button.png"); // reuse button art
    }

    public void toggle() { visible = !visible; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean v) { visible = v; }

    public void dispose() {
        panelBg.dispose();
        slotBg.dispose();
        slotSel.dispose();
        leftArrow.dispose();
        rightArrow.dispose();
        craftButton.dispose();
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float panelW = 720, panelH = 480;
        float panelX = (screenW - panelW) / 2f;
        float panelY = (screenH - panelH) / 2f;

        // arrows
        arrowSize = 32;
        arrowsY   = panelY + panelH - arrowSize - 12;
        leftX     = panelX + 16;
        rightX    = panelX + panelW - arrowSize - 16;

        // mouse page
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = screenH - Gdx.input.getY();
            if (mx >= leftX && mx <= leftX + arrowSize && my >= arrowsY && my <= arrowsY + arrowSize) {
                page = Math.max(0, page - 1);
                selected = page * PER_PAGE;
            } else if (mx >= rightX && mx <= rightX + arrowSize && my >= arrowsY && my <= arrowsY + arrowSize) {
                int maxPage = Math.max(0, (recipes.size() - 1) / PER_PAGE);
                page = Math.min(maxPage, page + 1);
                selected = page * PER_PAGE;
            }
        }

        handleInput();

        // draw panel + title
        batch.draw(panelBg, panelX, panelY, panelW, panelH);
        BitmapFont big = GameAssetManager.getGameAssetManager().getBigFont();
        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();

        GlyphLayout title = new GlyphLayout(big, "Crafting");
        big.draw(batch, title, panelX + (panelW - title.width)/2f, panelY + panelH - 16);

        batch.draw(leftArrow,  leftX,  arrowsY, arrowSize, arrowSize);
        batch.draw(rightArrow, rightX, arrowsY, arrowSize, arrowSize);

        drawGrid(batch, panelX, panelY, panelW, panelH);
        drawRecipeDetails(batch, panelX, panelY, panelW, panelH);

        if (feedback != null && feedbackTimer > 0f) {
            feedbackTimer -= Gdx.graphics.getDeltaTime();
            GlyphLayout fb = new GlyphLayout(small, feedback);
            small.draw(batch, fb, panelX + (panelW - fb.width)/2f, panelY - 10);
            if (feedbackTimer <= 0f) feedback = null;
        }
    }

    private void handleInput() {
        // page
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            page = Math.max(0, page - 1);
            selected = page * PER_PAGE;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            int maxPage = Math.max(0, (recipes.size() - 1) / PER_PAGE);
            page = Math.min(maxPage, page + 1);
            selected = page * PER_PAGE;
        }
        // move
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  selected = Math.max(page*PER_PAGE, selected-1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selected = Math.min(Math.min((page+1)*PER_PAGE, recipes.size())-1, selected+1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))    selected = Math.max(page*PER_PAGE, selected-6);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))  selected = Math.min(Math.min((page+1)*PER_PAGE, recipes.size())-1, selected+6);

        // craft
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) tryCraftSelected();
    }

    private void drawGrid(SpriteBatch batch, float panelX, float panelY, float panelW, float panelH) {
        float pad = 8, cell = 80;
        float gx = panelX + 16, gy = panelY + panelH - 60;

        int start = page * PER_PAGE;
        int end   = Math.min(recipes.size(), start + PER_PAGE);

        for (int i = start; i < end; i++) {
            int rel = i - start;
            int col = rel % COLS;
            int row = rel / COLS;
            float x = gx + col * (cell + pad);
            float y = gy - row * (cell + pad) - cell;

            batch.draw(slotBg, x, y, cell, cell);
            if (i == selected) batch.draw(slotSel, x - 4, y - 4, cell + 8, cell + 8);

            SimpleCraftingRecipe r = recipes.get(i);
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(r.getIconPath());
            batch.draw(icon, x + 16, y + 16, 48, 48);
        }
    }

    private void drawRecipeDetails(SpriteBatch batch, float panelX, float panelY, float panelW, float panelH) {
        if (recipes.isEmpty()) return;

        BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
        BitmapFont big   = GameAssetManager.getGameAssetManager().getBigFont();

        SimpleCraftingRecipe r = recipes.get(selected);

        // name
        GlyphLayout name = new GlyphLayout(big, r.getName());
        big.draw(batch, name, panelX + 250, panelY + 140);

        // ingredients
        float x = panelX + 250;
        float y = panelY + 100;

        for (Map.Entry<ItemType, Integer> e : r.getIngredients().entrySet()) {
            ItemType t = e.getKey();
            int need   = e.getValue();
            int have   = inventory.getTotalQty(t);

            Texture ingIcon = GameAssetManager.getGameAssetManager().getTexture(t.iconPath());
            batch.draw(ingIcon, x, y - 16, 20, 20);

            String line = t.id() + "  " + have + "/" + need;
            small.draw(batch, line, x + 26, y);
            y -= 22;
        }

        // button
        float btnW = 150, btnH = 80, btnX = panelX + panelW - btnW - 16, btnY = panelY + 16;
        batch.draw(craftButton, btnX, btnY, btnW, btnH);
        small.draw(batch, "", btnX + 22, btnY + 26);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (mx >= btnX && mx <= btnX + btnW && my >= btnY && my <= btnY + btnH) {
                tryCraftSelected();
            }
        }
    }

    private void tryCraftSelected() {
        if (recipes.isEmpty()) return;
        SimpleCraftingRecipe r = recipes.get(selected);
        if (!canCraft(r)) {
            feedback = "Not enough materials!";
            feedbackTimer = 1.6f;
            return;
        }
        for (Map.Entry<ItemType, Integer> e : r.getIngredients().entrySet()) {
            inventory.remove(e.getKey(), e.getValue());
        }
        inventory.add(r.getOutput(), r.getOutputQty());
        feedback = "Crafted: " + r.getName();
        feedbackTimer = 1.6f;
    }

    private boolean canCraft(SimpleCraftingRecipe r) {
        for (Map.Entry<ItemType, Integer> e : r.getIngredients().entrySet()) {
            if (inventory.getTotalQty(e.getKey()) < e.getValue()) return false;
        }
        return true;
    }
}