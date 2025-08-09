package io.github.StardewValley.views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.*;

public class InventoryMenuView {
    private int tabIndex = 0;
    private static final int TAB_COUNT = 5;
    private static final String[] TAB_TITLES = {"Inventory","Skills","Social","Map", "Settings"};

    private final Texture leftArrow;
    private final Texture rightArrow;
    private float leftX, rightX, arrowsY, arrowSize;

    private final Inventory inventory;
    private boolean visible = false;

    private final Texture panelBg;
    private final Texture slotBg;
    private final Texture slotSel;

    private int selected = 0;
    private int scrollOffset = 0;
    private static final int COLS = 6;
    private static final int ROWS = 6;

    private final Texture trashTex;
    private float trashX, trashY, trashSize;

    private final Inventory fridge;
    private final GameController controller;

    private final Texture moveToFridgeTex;
    private float moveBtnX, moveBtnY, moveBtnW = 65, moveBtnH = 55;

    private String feedbackMessage = null;
    private float feedbackTimer = 0f;

    public InventoryMenuView(Inventory inventory, Inventory fridge, GameController controller) {
        this.inventory = inventory;
        this.fridge = fridge;
        this.controller = controller;

        this.panelBg = new Texture("inventory/panel_bg.png");
        leftArrow  = new Texture("inventory/arrow_left.png");
        rightArrow = new Texture("inventory/arrow_right.png");
        this.slotBg = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        trashTex = new Texture("inventory/trash_can.png");
        moveToFridgeTex = new Texture("inventory/fridge.png"); // new texture
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(SpriteBatch batch) {
        if (feedbackTimer > 0f) {
            feedbackTimer -= Gdx.graphics.getDeltaTime();
            if (feedbackTimer <= 0f) {
                feedbackMessage = null;
            }
        }

        /* —— layout that we need for hit‑tests —— */
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float panelW = 400, panelH = 450;
        float panelX = (screenW - panelW) / 2f;
        float panelY = (screenH - panelH) / 2f;

        arrowSize = 32;
        arrowsY   = panelY + panelH - arrowSize - 12;
        leftX     = panelX + 16;
        rightX    = panelX + panelW - arrowSize - 16;

        trashSize = 48;
        trashX    = panelX + panelW - trashSize - 24;
        trashY    = panelY + 24;

        /* —— mouse click handling BEFORE any drawing —— */
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = Gdx.graphics.getHeight() - Gdx.input.getY();

            // arrows
            if (mx >= leftX && mx <= leftX + arrowSize &&
                my >= arrowsY && my <= arrowsY + arrowSize) {
                tabIndex = (tabIndex - 1 + TAB_COUNT) % TAB_COUNT;
            } else if (mx >= rightX && mx <= rightX + arrowSize &&
                my >= arrowsY && my <= arrowsY + arrowSize) {
                tabIndex = (tabIndex + 1) % TAB_COUNT;
            } else if (tabIndex == 0 && tryHandleTrashClick(mx, my)) {
                /* handled trash */
            }
        }

        /* —— keyboard movement —— */
        handleInput();

        /* —— draw panel & common UI —— */
        batch.draw(panelBg, panelX, panelY, panelW, panelH);
        batch.draw(leftArrow,  leftX,  arrowsY, arrowSize, arrowSize);
        batch.draw(rightArrow, rightX, arrowsY, arrowSize, arrowSize);

        BitmapFont font   = GameAssetManager.getGameAssetManager().getBigFont();
        String     title  = TAB_TITLES[tabIndex];

        GlyphLayout layout = new GlyphLayout(font, title);
        float titleX = panelX + (panelW - layout.width) / 2f;
        float titleY = arrowsY + arrowSize / 2f + layout.height / 2f;
        font.draw(batch, layout, titleX, titleY);

        /* —— draw page‑specific content —— */
        switch (tabIndex) {
            case 0:  drawInventoryGrid(batch, panelX, panelY, panelW, panelH); break;
            case 1:  placeholder(batch, panelX, panelY, panelH, "Skills coming soon…"); break;
            case 2:  placeholder(batch, panelX, panelY, panelH, "Social coming soon…"); break;
            case 3:  placeholder(batch, panelX, panelY, panelH, "Map coming soon…");    break;
            case 4: placeholder(batch, panelX, panelY, panelH, "Settings coming soon…"); break;
        }

        /* —— trash can (only on inventory page) —— */
        if (tabIndex == 0) {
            batch.draw(trashTex, trashX, trashY, trashSize, trashSize);

            if (controller.isPlayerInHouse()) {
                moveBtnX = trashX - moveBtnW;
                moveBtnY = trashY - 5;

                batch.draw(moveToFridgeTex, moveBtnX, moveBtnY, moveBtnW, moveBtnH);

                float mx = Gdx.input.getX();
                float my = Gdx.graphics.getHeight() - Gdx.input.getY();

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (mx >= moveBtnX && mx <= moveBtnX + moveBtnW &&
                        my >= moveBtnY && my <= moveBtnY + moveBtnH) {
                        moveSelectedToFridge();
                    }
                }
            }
        }

        if (feedbackMessage != null && feedbackTimer > 0f) {
            BitmapFont fontRed = GameAssetManager.getGameAssetManager().getErrorFont();
            float x = screenW / 2f;
            float y = 80;

            GlyphLayout feedbackLayout = new GlyphLayout(fontRed, feedbackMessage);
            fontRed.setColor(1f, 0.5f, 0.5f, 1f); // soft red tone
            fontRed.draw(batch, feedbackLayout, x - feedbackLayout.width / 2, y + 20);
        }
    }

    private void ensureSelectedVisible() {
        int firstVisible = scrollOffset * COLS;
        int lastVisible  = firstVisible + ROWS * COLS - 1;

        if (selected < firstVisible) {
            scrollOffset = selected / COLS;
        } else if (selected > lastVisible) {
            scrollOffset = selected / COLS - ROWS + 1;
        }
    }

    private boolean tryHandleTrashClick(float mx, float my) {
        if (mx < trashX || mx > trashX + trashSize ||
            my < trashY || my > trashY + trashSize) return false;

        Inventory.Stack stack = inventory.peek(selected);
        if (stack != null && !(stack.getType() instanceof ToolType)) {
            inventory.remove(stack.getType(), 1);     // delete ONE
            ensureSelectedVisible();
        }
        return true;
    }

    private void handleInput() {
        if (tabIndex != 0) return;                   // ← ignore on other pages

        int maxIndex = inventory.size() - 1;
        int col = selected % COLS;
        int row = selected / COLS;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && row > 0) {
            row--;  selected = row * COLS + col;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) &&
            (row + 1) * COLS < inventory.size()) {
            row++;  selected = row * COLS + col;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && selected > 0) {
            selected--;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) &&
            selected < maxIndex) {
            selected++;
        }
        selected = MathUtils.clamp(selected, 0, maxIndex);
        ensureSelectedVisible();
    }

    public void setSelectedIndex(int index) {
        selected = MathUtils.clamp(index, 0, inventory.size() - 1);
        ensureSelectedVisible();
    }

    public int getSelectedIndex() {
        return selected;
    }

    public void scrollBy(int direction) {
        if (tabIndex != 0)          // <- do nothing on the other pages
            return;

        int itemCount = inventory.size();
        int totalRows = (itemCount + COLS - 1) / COLS;
        int maxScroll = Math.max(0, totalRows - ROWS);

        if (direction > 0 && scrollOffset < maxScroll)      // wheel‑down
            scrollOffset++;
        else if (direction < 0 && scrollOffset > 0)         // wheel‑up
            scrollOffset--;
    }

    /* ─────────────────────────────────────────────────────────────
   Draw the inventory grid (page 0)
   ────────────────────────────────────────────────────────────*/
    private void drawInventoryGrid(SpriteBatch batch,
                                   float panelX, float panelY,
                                   float panelW, float panelH) {

        final float slotSize  = 48;
        final float pad       = 6;

        float gridStartX = panelX + 40;
        float gridStartY = panelY + panelH - 90;

        /* ---------- paging math ---------- */
        int itemCount  = inventory.size();
        int totalRows  = (itemCount + COLS - 1) / COLS;
        int maxScroll  = Math.max(0, totalRows - ROWS);

        scrollOffset   = MathUtils.clamp(scrollOffset, 0, maxScroll);
        int visibleStart = scrollOffset * COLS;
        int visibleEnd   = Math.min(itemCount, visibleStart + ROWS * COLS);

        /* ---------- grid ---------- */
        for (int i = visibleStart; i < visibleEnd; i++) {

            int relIndex = i - visibleStart;
            int col      = relIndex % COLS;
            int row      = relIndex / COLS;

            float x = gridStartX + col * (slotSize + pad);
            float y = gridStartY - row * (slotSize + pad);

            batch.draw(slotBg, x, y, slotSize, slotSize);

            if (i == selected) {
                batch.draw(slotSel, x - 6, y - 6, slotSize + 15, slotSize + 15);
            }

            /* -------- mouse select -------- */
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX();
                float my = Gdx.graphics.getHeight() - Gdx.input.getY(); // flip Y
                if (mx >= x && mx <= x + slotSize &&
                    my >= y && my <= y + slotSize) {

                    selected = i;
                    ensureSelectedVisible();
                }
            }

            /* -------- icon & stack size -------- */
            Inventory.Stack stack = inventory.peek(i);
            if (stack == null) continue;

            String iconPath;
            ItemType type = stack.getType();
            if (type instanceof CropType) {
                iconPath = ((CropType) type).cropTexture();
            } else {
                iconPath = type.iconPath();
            }
            Texture icon = GameAssetManager.getGameAssetManager().getTexture(iconPath);
            batch.draw(icon, x + 8, y + 9, 32, 32);

            if (stack.getQty() > 1) {
                GameAssetManager.getGameAssetManager().getSmallFont().draw(
                    batch, String.valueOf(stack.getQty()),
                    x + slotSize - 4, y + 14,
                    0, Align.right, false);
            }
        }
    }


    private void placeholder(SpriteBatch b,float px,float py,float ph,String txt){
        GameAssetManager.getGameAssetManager().getBigFont().draw(
            b, txt, px + 40, py + ph - 120);
    }

    private void moveSelectedToFridge() {
        Inventory.Stack stack = inventory.peek(selected);
        if (stack == null) return;

        ItemType type = stack.getType();
        if (!(type instanceof FoodType)) {
            showFeedback("Only food can be moved to the fridge");
            return;
        }

        inventory.remove(type, 1);
        fridge.add(type, 1);
        ensureSelectedVisible();
    }

    private void showFeedback(String message) {
        feedbackMessage = message;
        feedbackTimer = 2.5f; // show message for 2.5 seconds
    }

    public int getActiveTab() { return tabIndex; }

    public void dispose() {
        panelBg.dispose();
        slotBg.dispose();
        slotSel.dispose();
        trashTex.dispose();
        leftArrow.dispose();
        rightArrow.dispose();
    }
}
