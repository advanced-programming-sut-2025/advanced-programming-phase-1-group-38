// views/NpcQuestPopupView.java  (only the important parts shown)
package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.controllers.WorldController;
import io.github.StardewValley.models.*;

import java.util.List;
import java.util.Map;

public class NpcQuestPopupView {
    private static final float W = 620f, H = 380f;
    private static final float PADDING = 18f;
    private static final float ROW_H = 90f;

    private final WorldController world;
    private final GameController  playerCtrl;

    private final Texture panelBg;
    private final Texture completeBtn;
    private final Texture lockIcon;
    private final Texture goldIcon;
    private final Texture heartIcon;

    private final BitmapFont titleFont;
    private final BitmapFont rowFont;
    private final BitmapFont smallFont;

    private boolean visible = false;
    private NpcCharacter npc;

    public NpcQuestPopupView(WorldController world, GameController playerCtrl) {
        this.world = world;
        this.playerCtrl = playerCtrl;

        GameAssetManager am = GameAssetManager.getGameAssetManager();
        panelBg     = am.getTexture("inventory/panel_bg.png");
        completeBtn = am.getTexture("inventory/cook_button.png");
        lockIcon    = am.getTexture("black.png");
        goldIcon    = am.getTexture("black.png");
        heartIcon   = am.getTexture("black.png");

        titleFont  = am.getBigFont();
        rowFont    = am.getBigFont();
        smallFont  = am.getSmallFont();
    }

    public boolean isVisible() { return visible; }
    public void open(NpcCharacter npc) { this.npc = npc; this.visible = true; }
    public void close() { visible = false; npc = null; }

    public void render(SpriteBatch b) {
        if (!visible || npc == null) return;

        float sw = Gdx.graphics.getWidth(), sh = Gdx.graphics.getHeight();
        float x = (sw - W) * 0.5f, y = (sh - H) * 0.5f;

        b.setColor(Color.WHITE);
        b.draw(panelBg, x, y, W, H);

        titleFont.draw(b, npc.name + " — Quests", x + PADDING, y + H - 12);

        List<QuestDef> quests = QuestBook.getForNpc(npc.id);
        if (quests.isEmpty()) {
            smallFont.draw(b, "No quests available.", x + PADDING, y + H - 60);
        }

        float rowY = y + H - 60;
        for (int i = 0; i < quests.size(); i++) {
            drawQuestRow(b, quests.get(i), x, rowY);
            rowY -= ROW_H;
            if (rowY < y + 60) break; // simple clipping if too many
        }

        smallFont.setColor(1,1,1,0.75f);
        smallFont.draw(b, "Q or Esc to close", x + PADDING, y + PADDING);
        smallFont.setColor(Color.WHITE);

        handleInput();
    }

    private void drawQuestRow(SpriteBatch b, QuestDef q, float x, float rowY) {
        final float textX = x + PADDING;
        final float icon = 26f;
        final float rewardX = x + W * 0.48f;
        final float btnW = 70, btnH = 70;
        final float btnX = x + W - PADDING - btnW;
        final float btnY = rowY - btnH + 8;

        // title first
        rowFont.draw(b, q.title, textX, rowY);

        // --- COMPLETED? show ribbon and stop drawing requirements/rewards/buttons ---
        boolean done = switch (q.index) {
            case 1 -> world.npc().isQ1Done(npc.id);
            case 2 -> world.npc().isQ2Done(npc.id);
            case 3 -> world.npc().isQ3Done(npc.id);
            default -> false;
        };
        if (done) {
            smallFont.setColor(Color.GOLD);
            smallFont.draw(b, "COMPLETED", btnX - 6, rowY - 22);
            smallFont.setColor(Color.WHITE);
            return; // ← don't draw ingredients after completion
        }

        // --- LOCKED? show lock & reason and stop (no ingredients/button) ---
        int day = world.getSharedTime().getDay();
        int lv  = world.npc().level(playerCtrl, npc.id);
        boolean locked = false;
        String why = "";
        if (q.index == 2 && lv < Math.max(1, q.minFriendLevel)) {
            locked = true; why = "Requires Lv" + Math.max(1, q.minFriendLevel);
        } else if (q.index == 3) {
            boolean unlockedTime = world.npc().isQ3Unlocked(npc.id, day);
            if (!unlockedTime) {
                locked = true;
                int left = world.npc().daysUntilQ3(npc.id, day);
                why = (left < 0) ? "Finish Q2 first" : ("Unlocks in " + left + " day(s)");
            }
        }
        if (locked) {
            b.draw(lockIcon, btnX, btnY, btnW, btnH);
            if (!why.isEmpty()) {
                smallFont.setColor(1f,1f,1f,0.75f);
                smallFont.draw(b, why, btnX - 6, btnY - 6);
                smallFont.setColor(Color.WHITE);
            }
            return; // ← no requirements or button while locked
        }

        // --- Requirements (now only for active, not-completed quests) ---
        float reqCursor = textX;
        Inventory inv = playerCtrl.getPlayer().getInventory();
        for (Map.Entry<ItemType,Integer> e : q.requirements.entrySet()) {
            ItemType type = e.getKey();
            int need = e.getValue();
            int have = inv.getTotalQty(type);

            Texture reqIcon = GameAssetManager.getGameAssetManager().getTexture(type.iconPath());
            b.draw(reqIcon, reqCursor, rowY - 52, icon, icon);

            smallFont.setColor(have >= need ? Color.WHITE : new Color(1f, 0.4f, 0.4f, 1f));
            smallFont.draw(b, have + " / " + need, reqCursor + icon + 6, rowY - 34);
            smallFont.setColor(Color.WHITE);

            reqCursor += 110;
        }

        // --- Rewards (optional visuals) ---
        float rewCursor = rewardX;
        if (q.rewardGold != null && q.rewardGold > 0) {
            b.draw(goldIcon, rewCursor, rowY - 52, icon, icon);
            smallFont.draw(b, String.valueOf(q.rewardGold), rewCursor + icon + 6, rowY - 34);
            rewCursor += 100;
        }
        if (q.rewardFriendPoints != null && q.rewardFriendPoints > 0) {
            b.draw(heartIcon, rewCursor, rowY - 52, icon, icon);
            String hearts = (q.rewardFriendPoints == 200) ? "+1 ❤" : ("+" + q.rewardFriendPoints + " pts");
            smallFont.draw(b, hearts, rewCursor + icon + 6, rowY - 34);
            rewCursor += 100;
        }
        if (q.rewardItems != null) {
            for (var e : q.rewardItems.entrySet()) {
                Texture ico = GameAssetManager.getGameAssetManager().getTexture(e.getKey().iconPath());
                b.draw(ico, rewCursor, rowY - 52, icon, icon);
                smallFont.draw(b, "x" + e.getValue(), rewCursor + icon + 6, rowY - 34);
                rewCursor += 90;
            }
        }

        // --- Complete button (active + not completed) ---
        boolean canComplete = hasAll(q);
        if (canComplete) {
            b.draw(completeBtn, btnX, btnY, btnW, btnH);
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX();
                float my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= btnX && mx <= btnX + btnW && my >= btnY && my <= btnY + btnH) {
                    tryComplete(q);
                }
            }
        } else {
            b.setColor(1,1,1,0.25f);
            b.draw(completeBtn, btnX, btnY, btnW, btnH);
            b.setColor(Color.WHITE);
        }
    }


    // --- replace your hasAll(...) ---
    private boolean hasAll(QuestDef q) {
        Inventory bag = playerCtrl.getPlayer().getInventory();
        for (var e : q.requirements.entrySet()) {
            if (bag.getTotalQty(e.getKey()) < e.getValue()) return false;
        }
        return true;
    }

    private void tryComplete(QuestDef q) {
        Inventory bag = playerCtrl.getPlayer().getInventory();

        // make sure we can (bag-only)
        for (var e : q.requirements.entrySet()) {
            if (bag.getTotalQty(e.getKey()) < e.getValue()) return;
        }

        // ask quest service first (first-wins / timing gates)
        boolean ok = switch (q.index) {
            case 1 -> world.npc().completeQ1(playerCtrl, npc.id, world.getSharedTime());
            case 2 -> world.npc().completeQ2(playerCtrl, npc.id, world.getSharedTime());
            case 3 -> world.npc().completeQ3(playerCtrl, npc.id, world.getSharedTime());
            default -> false;
        };
        if (!ok) return;

        // consume from the bag
        for (var e : q.requirements.entrySet()) {
            int leftover = bag.remove(e.getKey(), e.getValue()); // returns leftover
            if (leftover != 0) {
                // Shouldn’t happen if we passed hasAll(); safe-guard
                Gdx.app.log("Quest", "Leftover removing " + e.getKey() + ": " + leftover);
            }
        }

        // rewards
        if (q.rewardItems != null) {
            for (var e : q.rewardItems.entrySet()) bag.add(e.getKey(), e.getValue());
        }
        if (q.rewardGold != null && q.rewardGold > 0) {
             playerCtrl.getPlayer().getGameEconomy().addGold(q.rewardGold); // or bag.add(GOLD, q.rewardGold);
        }
        if (q.rewardFriendPoints != null && q.rewardFriendPoints > 0) {
            world.npc().addFriendPoints(playerCtrl, npc.id, q.rewardFriendPoints);
        }
    }

    private void handleInput() {

    }
}
