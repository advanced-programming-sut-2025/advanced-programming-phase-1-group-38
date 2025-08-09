package io.github.StardewValley.models;

import io.github.StardewValley.views.CookingMenuView;
import io.github.StardewValley.views.InventoryMenuView;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.TimeUtils;

public class InventoryScrollHandler extends InputAdapter {
    private final InventoryRenderer inventoryRenderer;
    private final InventoryMenuView inventoryMenuView;
    private final CookingMenuView cookingMenuView;

    private static final long SCROLL_COOLDOWN_MS = 400;
    private long lastScrollTime = 0;

    public InventoryScrollHandler(InventoryRenderer renderer, InventoryMenuView menuView, CookingMenuView cookingMenuView) {
        this.inventoryRenderer = renderer;
        this.inventoryMenuView = menuView;
        this.cookingMenuView = cookingMenuView;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        long currentTime = TimeUtils.millis();

        if (currentTime - lastScrollTime < SCROLL_COOLDOWN_MS) {
            return false;
        }

        int scroll = (int) Math.signum(amountY);
        if (scroll == 0) return false;

        lastScrollTime = currentTime;

        // 🎯 Cooking menu scroll
        if (cookingMenuView != null && cookingMenuView.isVisible()) {
            cookingMenuView.scrollRecipes(scroll);
            return true;
        }

        // 🧳 Inventory menu scroll
        if (inventoryMenuView.isVisible()) {
            if (inventoryMenuView.getActiveTab() == 0) {
                inventoryMenuView.scrollBy(scroll);
            }
            return true;
        }

        // 🎯 Hotbar scroll fallback
        inventoryRenderer.scrollBy(scroll);
        return true;
    }
}
