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

    private static final int MAX_CONTACTS = 4;
    private int suppressTypingFrames = 0;

    private final Texture starEmpty = GameAssetManager.getGameAssetManager().getTexture("empty_star.png");
    private final Texture starFull  = GameAssetManager.getGameAssetManager().getTexture("full_star.png");

    private int giftScroll = 0;
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
    public void toggle() {
        visible = !visible;
        if (visible) {
            scroll = 0;              // jump to newest
            suppressTypingFrames = 2; // ← ignore typing for the next 2 frames
        }
    }
    public boolean isVisible(){ return visible; }

    public void render(SpriteBatch b) {
        if (!visible) return;

        // Panel placement
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float w = PANEL_W, h = PANEL_H;
        float x = (screenW - w) * 0.5f;
        float y = (screenH - h) * 0.5f;

        b.draw(panelBg, x, y, w, h);

        // Build contact list = all others (limit 4)
        java.util.List<GameController> contacts = new java.util.ArrayList<>();
        for (GameController gc : world.getAllControllers()) {
            if (gc != me) contacts.add(gc);
        }
        if (contacts.size() > MAX_CONTACTS) contacts = contacts.subList(0, MAX_CONTACTS);

        // keep selected if still exists; otherwise pick first (or null if none)
        if (selectedId != null && contacts.stream().noneMatch(gc -> world.playerIdOf(gc).equals(selectedId))) {
            selectedId = null;
        }
        if (selectedId == null && !contacts.isEmpty()) {
            selectedId = world.playerIdOf(contacts.get(0));
        }

        // LEFT: players list (always visible even if far)
        float listX = x + 30, listY = y + h - 20;
        small.draw(b, "Players", listX, listY);
        float cy = listY - 10;
        int maxRows = (int)((h - 40) / ROW_H);

        int shown = 0;
        for (GameController gc : contacts) {
            if (shown >= maxRows) break;
            String pid = world.playerIdOf(gc);

            float rowTop = cy - shown * ROW_H;
            float rx = listX;
            float ry = rowTop - ROW_H + 4;

            // row background
            b.draw(avatarBg, rx, ry - 10, LIST_W - 20, ROW_H - 6);

            // tiny avatar
            var fr = gc.getPlayer().getCurrentFrame();
            if (fr != null) b.draw(fr, rx + 6, ry - 6, 32, 32);

            // proximity + friendship level
            boolean nearby = world.nearbyPlayers(me, RADIUS)
                .stream().anyMatch(p -> world.playerIdOf(p).equals(pid));
            int lvl = world.playerFriends().level(world.playerIdOf(me), pid);

            String nameLine = pid + "  (Lv" + lvl + ")  " + (nearby ? "(nearby)" : "(far)");
            small.draw(b, nameLine, rx + 44, ry + 13);

            // click to select
            if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX(), my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= rx && mx <= rx + (LIST_W - 20) && my >= ry && my <= ry + (ROW_H - 6)) {
                    selectedId = pid;
                    scroll = 0;
                    giftScroll = 0; // reset gift-history scroll on selection change
                }
            }
            shown++;
        }

        // RIGHT side layout
        float chatX = x + LIST_W + 25f;
        float chatW = w - LIST_W - 20f;
        float chatY = y + 14f;
        float chatH = h - 84f;

        // Header
        String header = (selectedId != null) ? ("Chat with " + selectedId) : "Select a player";
        small.draw(b, header, chatX, y + h - 20);

        // Gift history + per-row star rating (full, scrollable)
        drawGiftHistoryAndRating(b, chatX, chatW, y, h);

        // Chat messages for selected contact
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
            java.util.Collections.reverse(convo); // oldest → newest

            int linesVisible = Math.max(1, (int)(chatH / LINE_STEP));
            int maxScroll = Math.max(0, convo.size() - linesVisible);
            scroll = Math.max(0, Math.min(scroll, maxScroll));

            int start = Math.max(0, convo.size() - linesVisible - scroll);
            int end   = Math.min(convo.size(), start + linesVisible);
            int windowSize = end - start;

            // draw TOP → DOWN so newest ends up at the bottom
            float top = chatY + chatH - 10f;
            for (int i = 0; i < windowSize; i++) {
                Notification n = convo.get(start + i);
                String who = n.fromId.equals(myId) ? "You" : n.fromId;
                float lineY = top - i * LINE_STEP;
                small.draw(b, who + ": " + n.text, chatX, lineY);
            }
        } else {
            small.draw(b, "", chatX, chatY + 10f);
        }

        // Gift button (only if someone selected & nearby → popup will check too)
        if (selectedId != null) {
            Texture giftBtn = GameAssetManager.getGameAssetManager().getTexture("gift.png");
            float gx = chatX + chatW - 70, gy = y + 15, gw = 44, gh = 44;
            b.draw(giftBtn, gx, gy, gw, gh);
            if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX(), my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= gx && mx <= gx + gw && my >= gy && my <= gy + gh) {
                    // find the selected controller and open player gift popup (only if within radius)
                    GameController target = null;
                    for (GameController gc : world.nearbyPlayers(me, RADIUS)) {
                        if (world.playerIdOf(gc).equals(selectedId)) { target = gc; break; }
                    }
                    if (target != null) me.getGiftPopup().openForPlayer(target);
                }
            }
        }

        // Input line (only type when nearby)
        boolean canType = selectedId != null && isSelectedNearby();
        if (canType) {
            small.draw(b, "> " + input + "_", chatX + 10, y + 24);
        } else {
            small.draw(b, "> (get closer to chat)", chatX + 10, y + 24);
        }

        // Key handling (typing, chat scroll, etc.)
        handleTyping();
    }

    /** Full, scrollable gift history with per-row heart rating; scroll with LEFT/RIGHT keys. */
    private void drawGiftHistoryAndRating(SpriteBatch b, float chatX, float chatW, float y, float h) {
        if (selectedId == null) return;

        // Right-side panel area
        final float histW   = 170f;
        final float histX   = chatX + chatW - histW;
        final float histTop = y + h - 40f;
        final float histBot = y + 60f;

        small.draw(b, "Gifts from " + selectedId, histX, histTop);

        String myId = world.playerIdOf(me);
        java.util.List<WorldController.GiftReceipt> recs =
            world.giftsBetween(myId, selectedId); // oldest → newest

        // Three-line layout per gift: NAME → DATE → HEARTS
        final float ROW_H        = 44f;  // tall enough for 3 lines
        final float NAME_OFFSET  = 14f;  // rowTop → name baseline
        final float DATE_GAP     = 14f;  // name → date
        final float HEART_GAP    = 14f;  // date → hearts

        int total = recs.size();
        float areaH = Math.max(1f, histTop - histBot);
        int linesVisible = Math.max(1, (int)(areaH / ROW_H));
        int maxGiftScroll = Math.max(0, total - linesVisible);

        // NEW: keyboard scrolling — LEFT = older (scroll up), RIGHT = newer (scroll down)
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            giftScroll = Math.min(maxGiftScroll, giftScroll + 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            giftScroll = Math.max(0, giftScroll - 1);
        }
        // (optional) page jump with SHIFT+LEFT/RIGHT
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        if (shift && Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  giftScroll = Math.min(maxGiftScroll, giftScroll + Math.max(1, linesVisible - 1));
        if (shift && Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) giftScroll = Math.max(0, giftScroll - Math.max(1, linesVisible - 1));

        // clamp (in case list size changed)
        giftScroll = Math.max(0, Math.min(giftScroll, maxGiftScroll));

        int start = Math.max(0, total - linesVisible - giftScroll);
        int end   = Math.min(total, start + linesVisible);

        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("MMM d, HH:mm");

        // Input snapshot for rating hit-tests
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean clicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        // Heart metrics
        final float HEART_SIZE   = 16f;
        final float HEART_GAP_X  = 4f;
        final float heartsTotalW = 5 * HEART_SIZE + 4 * HEART_GAP_X;

        // Draw visible rows (top → down so newer appear lower)
        for (int i = 0; i < (end - start); i++) {
            WorldController.GiftReceipt r = recs.get(start + i);

            float rowTop = histTop - 8f - i * ROW_H;
            if (rowTop < histBot + NAME_OFFSET) break;

            // 1) Name
            float nameY = rowTop - NAME_OFFSET;
            small.draw(b, r.itemName, histX, nameY);

            // 2) Date
            float dateY = nameY - DATE_GAP;
            if (dateY < histBot) continue;
            String when = fmt.format(new java.util.Date(r.ts));
            small.draw(b, "(" + when + ")", histX, dateY);

            // 3) Hearts (UNDER the date, left-aligned)
            float heartsLeft = histX;
            float heartsTop  = dateY - HEART_GAP;
            float heartsY    = heartsTop - HEART_SIZE;

            // Which heart is hovered?
            int hoveredIdx = -1;
            if (my >= heartsY && my <= heartsY + HEART_SIZE &&
                mx >= heartsLeft && mx <= heartsLeft + heartsTotalW) {
                hoveredIdx = (int)((mx - heartsLeft) / (HEART_SIZE + HEART_GAP_X)) + 1;
                if (hoveredIdx < 1) hoveredIdx = 1;
                if (hoveredIdx > 5) hoveredIdx = 5;
            }

            // Draw hearts: filled up to rating; if unrated, preview up to hovered
            for (int s = 1; s <= 5; s++) {
                float sx = heartsLeft + (s - 1) * (HEART_SIZE + HEART_GAP_X);
                Texture tex;
                if (r.rating != null) {
                    tex = (s <= r.rating) ? starFull : starEmpty;
                } else if (hoveredIdx >= 1 && s <= hoveredIdx) {
                    tex = starFull; // hover preview
                } else {
                    tex = starEmpty;
                }
                b.draw(tex, sx, heartsY, HEART_SIZE, HEART_SIZE);
            }

            // Click to rate this specific gift
            if (clicked && r.rating == null && hoveredIdx >= 1) {
                world.rateGift(r, hoveredIdx);
            }
        }
    }


    private void handleTyping() {
        if (selectedId == null || !isSelectedNearby()) return;

        if (suppressTypingFrames > 0) {
            suppressTypingFrames--;
            return;
        }

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

        int day = me.getGameTime().getDay();
        if (world.playerFriends().markFirstTalkToday(meId, toId, day)) {
            world.playerFriends().add(meId, toId, 20); // +20 only once per day (pairwise)
        }

        long now = System.currentTimeMillis();
        world.pushNotification(toId, new Notification(meId, toId, text, now), true);   // unread for receiver
        world.pushNotification(meId, new Notification(meId, toId, text, now), false);  // echo, not unread

        scroll = 0; // snap to newest after sending
    }


    private boolean isSelectedNearby() {
        if (selectedId == null) return false;
        for (GameController gc : world.nearbyPlayers(me, RADIUS)) {
            if (world.playerIdOf(gc).equals(selectedId)) return true;
        }
        return false;
    }

}
