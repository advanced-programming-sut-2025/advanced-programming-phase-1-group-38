package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.controllers.WorldController;
import io.github.StardewValley.models.GameAssetManager;
import io.github.StardewValley.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class PlayerChatOverlay {
    private boolean visible = false;
    private final WorldController world;
    private final GameController me;

    private int scroll = 0;              // how many lines up from the newest
    private static final int LINES_VISIBLE = 8;
    private static final float LINE_STEP = 22f;

    private static final float PANEL_W = 500f, PANEL_H = 400f;
    private static final float LIST_W  = 140f;   // left column width
    private static final float ROW_H   = 40f;    // avatar row height
    private static final float RADIUS  = 96f;    // proximity for list

    private String selectedId = null;            // current target playerId
    private final Texture avatarBg = GameAssetManager.getGameAssetManager().getTexture("inventory/slot.png");

    private final Texture panelBg = GameAssetManager.getGameAssetManager().getTexture("inventory/panel_bg.png");
    private final BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();
    private final List<String> buffer = new ArrayList<>(); // tiny ring buffer
    private String input = "";

    public PlayerChatOverlay(WorldController world, GameController me) {
        this.world = world; this.me = me;
    }
    public void toggle(){ visible = !visible; }
    public boolean isVisible(){ return visible; }

    public void render(SpriteBatch b) {
        if (!visible) return;
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float w = PANEL_W, h = PANEL_H;
        float x = (screenW - w) * 0.5f;
        float y = (screenH - h) * 0.5f;

        b.draw(panelBg, x, y, w, h);

        // Build nearby list
        java.util.List<GameController> nearby = world.nearbyPlayers(me, RADIUS);
        // Ensure selection is valid
        if ((selectedId == null || nearby.stream().noneMatch(gc -> world.playerIdOf(gc).equals(selectedId)))
            && !nearby.isEmpty()) {
            selectedId = world.playerIdOf(nearby.get(0));
        }

        // LEFT: nearby players
        float listX = x + 10, listY = y + h - 16;
        small.draw(b, "Nearby", listX, listY);
        float cy = listY - 10;

        int maxRows = (int)((h - 40) / ROW_H);
        int shown = 0;
        for (GameController gc : nearby) {
            if (shown >= maxRows) break;
            String pid = world.playerIdOf(gc);

            float rowTop = cy - shown * ROW_H;
            float rx = listX;
            float ry = rowTop - ROW_H + 4;

            // background
            b.draw(avatarBg, rx, ry, LIST_W - 20, ROW_H - 6);

            // tiny avatar (use player frame if you have it)
            var fr = gc.getPlayer().getCurrentFrame();
            if (fr != null) {
                b.draw(fr, rx + 6, ry + 4, 32, 32);
            }

            // name + level
            int lvl = world.playerFriends().level(world.playerIdOf(me), pid);
            small.draw(b, pid + "  (Lv" + lvl + ")", rx + 44, ry + 24);

            // selection highlight
            if (pid.equals(selectedId)) {
                // reuse slot_selected if you want a highlight; otherwise text marker:
                small.draw(b, "●", rx + LIST_W - 30, ry + 24);
            }

            // click to select
            if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX(), my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= rx && mx <= rx + (LIST_W - 20) && my >= ry && my <= ry + (ROW_H - 6)) {
                    selectedId = pid;
                }
            }
            shown++;
        }

        // RIGHT: chat with selectedId
        float chatX = x + LIST_W + 10f;
        float chatW = w - LIST_W - 20f;
        float chatY = y + 14f;
        float chatH = h - 84f;

        String header = (selectedId != null) ? ("Chat with " + selectedId) : "No one nearby";
        small.draw(b, header, chatX, y + h - 16);

        String myId = world.playerIdOf(me);
        java.util.Deque<Notification> inbox = world.getInbox(myId);

        if (selectedId != null) {
            // collect full convo oldest→newest
            java.util.ArrayList<Notification> convo = new java.util.ArrayList<>();
            for (var it = inbox.descendingIterator(); it.hasNext();) {
                Notification n = it.next();
                if ((n.fromId.equals(selectedId) && n.toId.equals(myId)) ||
                    (n.fromId.equals(myId)       && n.toId.equals(selectedId))) {
                    convo.add(n);
                }
            }
            java.util.Collections.reverse(convo); // now oldest → newest

            int linesVisible = Math.max(1, (int)(chatH / LINE_STEP)); // adapt to panel size
            int maxScroll = Math.max(0, convo.size() - linesVisible);
            scroll = Math.max(0, Math.min(scroll, maxScroll));        // clamp

            int start = Math.max(0, convo.size() - linesVisible - scroll);
            int end   = Math.min(convo.size(), start + linesVisible);

            float bottom = chatY + 10f; // draw from bottom up
            for (int i = 0; i < (end - start); i++) {
                Notification n = convo.get(start + i);
                String who = n.fromId.equals(myId) ? "You" : n.fromId;
                float lineY = bottom + i * LINE_STEP;
                small.draw(b, who + ": " + n.text, chatX, lineY);
            }
        } else {
            small.draw(b, "No nearby players.", chatX, chatY + 10f);
        }

        // gift button (only if someone selected)
        if (selectedId != null) {
            Texture giftBtn = GameAssetManager.getGameAssetManager().getTexture("gift.png");
            float gx = chatX + chatW - 54, gy = y + 8, gw = 44, gh = 44;
            b.draw(giftBtn, gx, gy, gw, gh);
            if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX(), my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= gx && mx <= gx + gw && my >= gy && my <= gy + gh) {
                    // find the selected controller and open player gift popup
                    GameController target = null;
                    for (GameController gc : world.nearbyPlayers(me, RADIUS)) {
                        if (world.playerIdOf(gc).equals(selectedId)) { target = gc; break; }
                    }
                    if (target != null) me.getGiftPopup().openForPlayer(target);
                }
            }
        }

        // input line
        small.draw(b, "> " + input + "_", chatX + 10, y + 24);
        handleTyping();
    }

    private void handleTyping() {
        // keyboard scrolling while overlay is open
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            scroll++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            scroll = Math.max(0, scroll - 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (!input.isBlank()) {
                sendText(input);
                input = "";
            }
            return;
        }

        for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) if (Gdx.input.isKeyJustPressed(key)) input += (char)('a' + (key - Input.Keys.A));
        for (int key = Input.Keys.NUM_0; key <= Input.Keys.NUM_9; key++) if (Gdx.input.isKeyJustPressed(key)) input += (char)('0' + (key - Input.Keys.NUM_0));
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) input += " ";
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && !input.isEmpty()) input = input.substring(0, input.length()-1);
    }

    private void sendText(String text) {
        if (selectedId == null) return;

        // still nearby?
        GameController target = null;
        for (GameController gc : world.nearbyPlayers(me, RADIUS)) {
            if (world.playerIdOf(gc).equals(selectedId)) { target = gc; break; }
        }
        if (target == null) return;

        String meId = world.playerIdOf(me);
        String toId = selectedId;

        world.playerFriends().talkAnytime(meId, toId, 10);

        long now = System.currentTimeMillis();
        world.pushNotification(toId, new Notification(meId, toId, text, now), true);   // unread for receiver
        world.pushNotification(meId, new Notification(meId, toId, text, now), false);  // echo, not unread

        scroll = 0; // snap to newest after sending
    }

}
