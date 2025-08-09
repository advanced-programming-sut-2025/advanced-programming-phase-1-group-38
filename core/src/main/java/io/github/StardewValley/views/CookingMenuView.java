package io.github.StardewValley.views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import io.github.StardewValley.models.*;

import java.util.List;
import java.util.Map;

public class CookingMenuView {
    private int tabIndex = 0;
    private static final int TAB_COUNT = 2;
    private static final String[] TAB_TITLES = {"Fridge", "Recipes"};

    private final Inventory fridge;
    private final Inventory playerInventory;
    private final List<CookingRecipe> recipes;

    private final Texture panelBg;
    private final Texture slotBg;
    private final Texture slotSel;
    private final Texture cookButton;

    private int selected = 0;
    private int scrollOffset = 0;
    private static final int COLS = 6;
    private static final int ROWS = 6;

    private final Texture leftArrow;
    private final Texture rightArrow;
    private float leftX, rightX, arrowsY, arrowSize;

    private boolean visible = false;
    private int recipeScrollOffset = 0;


    public CookingMenuView(Inventory playerInventory, Inventory fridge, List<CookingRecipe> recipes) {
        this.playerInventory = playerInventory;
        this.fridge = fridge;
        this.recipes = recipes;

        this.panelBg = new Texture("inventory/panel_bg.png");
        this.slotBg = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.leftArrow = new Texture("inventory/arrow_left.png");
        this.rightArrow = new Texture("inventory/arrow_right.png");
        this.cookButton = new Texture("inventory/cook_button.png"); // Add your cook button graphic
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean show) {
        this.visible = show;
    }

    public void setTabIndex(int tab) {
        this.tabIndex = MathUtils.clamp(tab, 0, TAB_COUNT - 1);
    }

    public void render(SpriteBatch batch) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float panelW = 400, panelH = 450;
        float panelX = (screenW - panelW) / 2f;
        float panelY = (screenH - panelH) / 2f;

        arrowSize = 32;
        arrowsY = panelY + panelH - arrowSize - 12;
        leftX = panelX + 16;
        rightX = panelX + panelW - arrowSize - 16;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (mx >= leftX && mx <= leftX + arrowSize &&
                my >= arrowsY && my <= arrowsY + arrowSize) {
                tabIndex = (tabIndex - 1 + TAB_COUNT) % TAB_COUNT;
            } else if (mx >= rightX && mx <= rightX + arrowSize &&
                my >= arrowsY && my <= arrowsY + arrowSize) {
                tabIndex = (tabIndex + 1) % TAB_COUNT;
            }
        }

        handleInput();

        batch.draw(panelBg, panelX, panelY, panelW, panelH);
        batch.draw(leftArrow, leftX, arrowsY, arrowSize, arrowSize);
        batch.draw(rightArrow, rightX, arrowsY, arrowSize, arrowSize);

        BitmapFont font = GameAssetManager.getGameAssetManager().getBigFont();
        String title = TAB_TITLES[tabIndex];

        GlyphLayout layout = new GlyphLayout(font, title);
        float titleX = panelX + (panelW - layout.width) / 2f;
        float titleY = arrowsY + arrowSize / 2f + layout.height / 2f;
        font.draw(batch, layout, titleX, titleY);

        if (tabIndex == 0) {
            drawInventoryGrid(batch, panelX, panelY, panelW, panelH, fridge);
        } else if (tabIndex == 1) {
            drawRecipes(batch, panelX, panelY, panelW, panelH);
        }
    }

    private void drawInventoryGrid(SpriteBatch batch, float panelX, float panelY, float panelW, float panelH, Inventory inv) {
        final float slotSize = 48;
        final float pad = 6;
        float gridStartX = panelX + 40;
        float gridStartY = panelY + panelH - 90;

        int itemCount = inv.size();
        int totalRows = (itemCount + COLS - 1) / COLS;
        int maxScroll = Math.max(0, totalRows - ROWS);
        scrollOffset = MathUtils.clamp(scrollOffset, 0, maxScroll);

        int visibleStart = scrollOffset * COLS;
        int visibleEnd = Math.min(itemCount, visibleStart + ROWS * COLS);

        for (int i = visibleStart; i < visibleEnd; i++) {
            int relIndex = i - visibleStart;
            int col = relIndex % COLS;
            int row = relIndex / COLS;

            float x = gridStartX + col * (slotSize + pad);
            float y = gridStartY - row * (slotSize + pad);

            batch.draw(slotBg, x, y, slotSize, slotSize);
            if (i == selected) {
                batch.draw(slotSel, x - 6, y - 6, slotSize + 15, slotSize + 15);
            }

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX();
                float my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= x && mx <= x + slotSize && my >= y && my <= y + slotSize) {
                    selected = i;
                }
            }

            Inventory.Stack stack = inv.peek(i);
            if (stack == null) continue;

            String iconPath = stack.getType().iconPath();
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(iconPath);
            batch.draw(icon, x + 8, y + 9, 32, 32);

            if (stack.getQty() > 1) {
                GameAssetManager.getGameAssetManager().getSmallFont().draw(batch,
                    String.valueOf(stack.getQty()), x + slotSize - 4, y + 14,
                    0, Align.right, false);
            }
        }
    }

    private void drawRecipes(SpriteBatch batch, float panelX, float panelY, float panelW, float panelH) {
        BitmapFont font = GameAssetManager.getGameAssetManager().getSmallFont();
        float startY = panelY + panelH - 100;
        float x = panelX + 30;
        float lineHeight = 64;
        float iconSize = 24;
        float cookBtnX = panelX + panelW - 90;

        int visibleRows = 5; // number of recipes visible per page
        int maxOffset = Math.max(0, recipes.size() - visibleRows);
        recipeScrollOffset = MathUtils.clamp(recipeScrollOffset, 0, maxOffset);

        for (int i = recipeScrollOffset; i < Math.min(recipes.size(), recipeScrollOffset + visibleRows); i++) {
            CookingRecipe recipe = recipes.get(i);
            float y = startY - (i - recipeScrollOffset) * lineHeight;

            // Draw recipe name
            font.draw(batch, recipe.getName(), x, y);

            // Draw ingredients
            float ingredientX = x + 100;
            for (Map.Entry<ItemType, Integer> entry : recipe.getIngredients().entrySet()) {
                ItemType type = entry.getKey();
                int requiredQty = entry.getValue();
                int availableQty = playerInventory.getTotalQty(type);

                String iconPath;
                if (type instanceof CropType) {
                    iconPath = ((CropType) type).cropTexture();
                } else {
                    iconPath = type.iconPath();
                }

                Texture icon = GameAssetManager.getGameAssetManager().getTexture(iconPath);
                batch.draw(icon, ingredientX, y - 14, iconSize, iconSize);

                String text = availableQty + "/" + requiredQty;
                boolean enough = availableQty >= requiredQty;
                font.setColor(enough ? Color.WHITE : new Color(1f, 0.4f, 0.4f, 1f));
                font.draw(batch, text, ingredientX + iconSize + 4, y - 4);

                ingredientX += 80;
            }

            font.setColor(Color.WHITE); // reset color

            if (canCook(recipe)) {
                float btnY = y - 38;
                float btnW = 70;
                float btnH = 70;

                batch.draw(cookButton, cookBtnX, btnY, btnW, btnH);

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    float mx = Gdx.input.getX();
                    float my = Gdx.graphics.getHeight() - Gdx.input.getY();

                    if (mx >= cookBtnX && mx <= cookBtnX + btnW &&
                        my >= btnY && my <= btnY + btnH) {
                        cook(recipe);
                    }
                }
            }

        }
    }


    private boolean canCook(CookingRecipe recipe) {
        for (Map.Entry<ItemType, Integer> entry : recipe.getIngredients().entrySet()) {
            ItemType type = entry.getKey();
            int requiredQty = entry.getValue();

            int totalAvailable = playerInventory.getTotalQty(type);
            if (totalAvailable < requiredQty) return false;
        }
        return true;
    }


    private void cook(CookingRecipe recipe) {
        for (Map.Entry<ItemType, Integer> entry : recipe.getIngredients().entrySet()) {
            playerInventory.remove(entry.getKey(), entry.getValue());
        }
        playerInventory.add(recipe.getResult(), 1);
    }

    private void handleInput() {
        // Could add up/down scroll for recipes here if needed
    }

    public void scrollRecipes(int scrollAmount) {
        int visibleRows = 5;
        int maxOffset = Math.max(0, recipes.size() - visibleRows);
        recipeScrollOffset = MathUtils.clamp(recipeScrollOffset + scrollAmount, 0, maxOffset);
    }


    public void dispose() {
        panelBg.dispose();
        slotBg.dispose();
        slotSel.dispose();
        leftArrow.dispose();
        rightArrow.dispose();
        cookButton.dispose();
    }
}
