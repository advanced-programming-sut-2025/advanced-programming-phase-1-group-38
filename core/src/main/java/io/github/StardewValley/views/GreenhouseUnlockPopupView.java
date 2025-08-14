// views/GreenhouseUnlockPopupView.java
package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.enums.Types.MaterialType;

public class GreenhouseUnlockPopupView {
    private static final float W = 460f, H = 220f, P = 18f;
    private static final int COST_GOLD = 500;
    private static final int COST_WOOD = 1000;

    private final GameController ctrl;

    private final Texture panelBg;
    private final Texture btnOk;
    private final Texture btnCancel;
    private final Texture goldIcon;
    private final Texture woodIcon;
    private final BitmapFont big;
    private final BitmapFont small;

    private boolean visible = false;
    private Door pendingDoor;

    public GreenhouseUnlockPopupView(GameController ctrl) {
        this.ctrl = ctrl;
        var am = GameAssetManager.getGameAssetManager();
        panelBg   = am.getTexture("inventory/panel_bg.png");
        btnOk     = am.getTexture("inventory/complete.png");
        btnCancel = am.getTexture("inventory/lock.png");
        goldIcon  = am.getTexture("gold_coin.png");
        woodIcon  = am.getTexture(MaterialType.Wood.iconPath());
        big   = am.getBigFont();
        small = am.getSmallFont();
    }

    public boolean isVisible() { return visible; }
    public void open(Door door) { pendingDoor = door; visible = true; }
    public void close() { visible = false; pendingDoor = null; }

    public void render(SpriteBatch b) {
        if (!visible) return;

        float sw = Gdx.graphics.getWidth(), sh = Gdx.graphics.getHeight();
        float x = (sw - W) * 0.5f, y = (sh - H) * 0.5f;

        b.setColor(Color.WHITE);
        b.draw(panelBg, x, y, W, H);

        big.draw(b, "Unlock Greenhouse", x + P, y + H - 14);

        float rowY = y + H - 60f;
        float icon = 28f;
        b.draw(goldIcon, x + P, rowY - icon + 8, icon, icon);
        small.draw(b, COST_GOLD + " gold", x + P + icon + 8, rowY);

        b.draw(woodIcon, x + P + 180, rowY - icon + 8, icon, icon);
        small.draw(b, COST_WOOD + " wood", x + P + 180 + icon + 8, rowY);

        float btnW = 80f, btnH = 80f;
        float okX = x + W - P - btnW - 10f, okY = y + P + 10f;
        float cancelX = okX - 90f,          cancelY = okY;

        boolean canAfford = canAfford();

        if (canAfford) b.draw(btnOk, okX, okY, btnW, btnH);
        else { b.setColor(1,1,1,0.3f); b.draw(btnOk, okX, okY, btnW, btnH); b.setColor(Color.WHITE);
            small.setColor(1f, 0.5f, 0.5f, 1f);
            small.draw(b, "Insufficient resources", x + P, y + 32f);
            small.setColor(Color.WHITE); }

        b.draw(btnCancel, cancelX, cancelY, btnW, btnH);
        small.draw(b, " Q: Close", x + P, y + 18f);

        // keyboard
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) close();

        // mouse
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX(), my = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (mx >= okX && mx <= okX+btnW && my >= okY && my <= okY+btnH) {
                if (canAfford) tryUnlock();     // ← only the click unlocks
            }
            if (mx >= cancelX && mx <= cancelX+btnW && my >= cancelY && my <= cancelY+btnH) {
                close();
            }
        }
    }

    private boolean canAfford() {
        if (ctrl.isGreenhouseUnlocked()) return true;
        var econ = ctrl.getPlayer().getGameEconomy();
        Inventory bag = ctrl.getPlayer().getInventory();
        return econ.getGold() >= COST_GOLD && bag.getTotalQty(MaterialType.Wood) >= COST_WOOD;
    }

    private void tryUnlock() {
        if (!ctrl.isGreenhouseUnlocked()) {
            var econ = ctrl.getPlayer().getGameEconomy();
            Inventory bag = ctrl.getPlayer().getInventory();
            if (econ.getGold() < COST_GOLD || bag.getTotalQty(MaterialType.Wood) < COST_WOOD) return;
            econ.addGold(-COST_GOLD);
            bag.remove(MaterialType.Wood, COST_WOOD);
            ctrl.setGreenhouseUnlocked(true);
        }
        // enter now
        if (pendingDoor != null) ctrl.enterDoorFromUI(pendingDoor);
        close();
    }
}
