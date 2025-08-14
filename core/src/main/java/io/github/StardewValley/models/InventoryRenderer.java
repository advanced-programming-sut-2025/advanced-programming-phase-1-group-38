package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class InventoryRenderer {

    private static final int SLOT_SIZE = 48;
    private static final int SLOT_PAD = 4;
    private static final int COLS = 6;
    private static final float LERP_SPEED = 10f;

    private final Inventory inventory;
    private final Texture slotBg;
    private final Texture slotSel;
    private final Texture slotRowBg;

    private int selected = 0;
    private int visibleRow = 0;
    private float animatedRow = 0f;
    private boolean needsRebuild = false;


    private boolean inputEnabled = true;

    public InventoryRenderer(Inventory inv) {
        this.inventory = inv;
        this.slotBg = new Texture("inventory/slot.png");
        this.slotSel = new Texture("inventory/slot_selected.png");
        this.slotRowBg = new Texture("inventory/bg_bar.png");
    }

    public void render(SpriteBatch batch) {
        if (needsRebuild) {
            rebuildFromInventory();
            needsRebuild = false;
        }
        handleInput();

        animatedRow = MathUtils.lerp(animatedRow, visibleRow, Gdx.graphics.getDeltaTime() * LERP_SPEED);
        float slideOffset = (animatedRow - visibleRow) * (COLS * (SLOT_SIZE + SLOT_PAD));

        float baseY = 8;
        float startX = (Gdx.graphics.getWidth() - COLS * (SLOT_SIZE + SLOT_PAD) + SLOT_PAD) / 2f - slideOffset;

        int rowStartIndex = visibleRow * COLS;
        int rowEndIndex = Math.min(rowStartIndex + COLS, inventory.size());

        // Compute background width and height
        float bgWidth = COLS * (SLOT_SIZE + SLOT_PAD) - SLOT_PAD + 15;
        float bgHeight = SLOT_SIZE + 10f; // some padding above/below

        // Position background behind slots
        float bgX = (Gdx.graphics.getWidth() - bgWidth) / 2f;
        float bgY = baseY - 5f;


        batch.draw(slotRowBg, bgX, bgY, bgWidth, bgHeight);


        for (int i = rowStartIndex; i < rowEndIndex; i++) {
            int slotIndex = i - rowStartIndex;
            float x = startX + slotIndex * (SLOT_SIZE + SLOT_PAD);
            float y = baseY;

            batch.draw(slotBg, x, y, SLOT_SIZE, SLOT_SIZE);

            if (i == selected) {
                float baseScale = 1.35f;
                float pulse = 1f + 0.05f * MathUtils.sinDeg((TimeUtils.millis() % 1000) / 1000f * 360f);
                float selectorSize = SLOT_SIZE * baseScale * pulse;
                float offset = (selectorSize - SLOT_SIZE) / 2f - 2;
                batch.draw(slotSel, x - offset, y - offset, selectorSize, selectorSize);
            }

            Inventory.Stack stack = inventory.peek(i);
            if (stack != null) {
                String iconPath;
                ItemType type = stack.getType();
                if (type instanceof CropType) {
                    iconPath = ((CropType)type).cropTexture();
                } else {
                    iconPath = type.iconPath();
                }
                Texture icon = GameAssetManager.getGameAssetManager().getTexture(iconPath);

                batch.draw(icon, x + (SLOT_SIZE - 16) / 2f - 8, y + (SLOT_SIZE - 16) / 2f - 7, 32, 32);

                if (stack.getQty() > 1) {
                    GameAssetManager.getGameAssetManager().getSmallFont()
                        .draw(batch, String.valueOf(stack.getQty()), x + SLOT_SIZE - 2, y + 10, 0, Align.right, false);
                }
            }
        }
    }

    private void handleInput() {
        if (!inputEnabled) return;
        // Use LEFT/RIGHT arrow or A/D keys to scroll
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (visibleRow > 0) visibleRow--;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (visibleRow < getRowCount() - 1) visibleRow++;
        }

        // Use number keys to select slots in current visible row
        for (int i = Input.Keys.NUM_1; i <= Input.Keys.NUM_6; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                int idx = i - Input.Keys.NUM_1;
                int globalIndex = visibleRow * COLS + idx;
                if (globalIndex < inventory.size()) {
                    selected = globalIndex;
                }
            }
        }

        // Clamp selected index to inventory size
        selected = MathUtils.clamp(selected, 0, inventory.size() - 1);
    }
    
    public void onInventoryChanged() {
        needsRebuild = true;
    }

    private void rebuildFromInventory() {
        int rowCount = getRowCount();

        selected   = MathUtils.clamp(selected, 0, Math.max(0, inventory.size() - 1));
        visibleRow = MathUtils.clamp(visibleRow, 0, Math.max(0, rowCount - 1));

        visibleRow = selected / COLS;
    }


    public void dispose() {
        slotBg.dispose();
        slotSel.dispose();
    }

    public ItemType getSelectedType() {
        Inventory.Stack s = inventory.peek(selected);
        return s == null ? null : s.getType();
    }

    public void scrollBy(int direction) {
        if (direction > 0 && visibleRow < getRowCount() - 1) visibleRow++;
        else if (direction < 0 && visibleRow > 0) visibleRow--;
    }

    public void setInputEnabled(boolean enabled) {
        this.inputEnabled = enabled;
    }

    public void setSelectedIndex(int index) {
        selected = MathUtils.clamp(index, 0, inventory.size() - 1);
        visibleRow = selected / COLS;
    }

    public int getSelectedIndex() {
        return selected;
    }

    private int getRowCount() {
        return Math.max(1, (inventory.size() + COLS - 1) / COLS);
    }
}
