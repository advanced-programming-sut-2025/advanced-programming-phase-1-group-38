package io.github.StardewValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.controllers.WorldController;
import io.github.StardewValley.models.*;

import java.util.ArrayList;
import java.util.List;

/** Popup to gift an item to an NPC. First row = favorites, second row = other giftable items. */
public class GiftPopupView {
    private enum Mode { NPC, PLAYER }
    private Mode mode = Mode.NPC;

    private GameController targetPlayer = null;

    private final GameController gc;
    private final WorldController world;
    private final Inventory inventory;

    private boolean visible = false;
    private NpcCharacter npc;

    // tiny toast
    private String toastText = null;
    private float  toastTimer = 0f;

    // UI assets
    private final Texture panelBg = GameAssetManager.getGameAssetManager().getTexture("inventory/panel_bg.png");
    private final Texture slotBg  = GameAssetManager.getGameAssetManager().getTexture("inventory/slot.png");
    private final Texture slotSel = GameAssetManager.getGameAssetManager().getTexture("inventory/slot_selected.png");

    // layout
    private final float panelW = 460, panelH = 380;
    private float panelX, panelY;

    private static final int   COLS     = 6;      // 6 per row
    private static final float SLOT     = 48f;
    private static final float PAD      = 6f;     // space between slots
    private static final float LABEL_GAP= 22f;    // label above a row
    private static final float ROW_GAP  = 28f;    // space between rows

    private float row1TopY;   // favorites row top y
    private float row2TopY;   // others row top y
    private float rowLeftX;   // left x for both rows

    // data
    private final List<Inventory.Stack> flat = new ArrayList<>();
    private int favoritesCount = 0; // prefix length inside 'flat'
    private int selected = 0;

    private final BitmapFont big   = GameAssetManager.getGameAssetManager().getBigFont();
    private final BitmapFont small = GameAssetManager.getGameAssetManager().getSmallFont();

    public GiftPopupView(GameController gc, WorldController world, Inventory inventory) {
        this.gc = gc;
        this.world = world;
        this.inventory = inventory;
    }

    public boolean isVisible() { return visible; }

    public void open(NpcCharacter npc) {
        this.mode = Mode.NPC;          // <-- add this
        this.targetPlayer = null;      // <-- and this (clear previous player)
        this.npc = npc;
        rebuildList();
        visible = true;
    }

    public void close() {
        visible = false;
        npc = null;
        flat.clear();
        favoritesCount = 0;
        selected = 0;
        toastText = null;
        toastTimer = 0f;
    }

    /** Build favorites first, then others. Also pre-compute row anchors. */
    private void rebuildList() {
        flat.clear();
        favoritesCount = 0;

        float sw = Gdx.graphics.getWidth(), sh = Gdx.graphics.getHeight();
        panelX = (sw - panelW) / 2f;
        panelY = (sh - panelH) / 2f;

        rowLeftX = panelX + 30f;
        float titleH = 44f;
        row1TopY = panelY + panelH - titleH - 8f;
        row2TopY = row1TopY - (SLOT + ROW_GAP);

        if (mode == Mode.NPC && npc != null) {
            var favKeys = world.npc().favoriteKeys(npc.id);
            List<Inventory.Stack> fav  = new ArrayList<>();
            List<Inventory.Stack> rest = new ArrayList<>();
            for (int i = 0; i < inventory.size(); i++) {
                var s = inventory.peek(i);
                if (s == null) continue;
                if (s.getType() instanceof ToolType) continue;
                String key = io.github.StardewValley.controllers.NpcController.itemKey(s.getType());
                if (favKeys.contains(key)) fav.add(s); else rest.add(s);
            }
            flat.addAll(fav);
            favoritesCount = fav.size();
            flat.addAll(rest);
        } else {
            // PLAYER mode: simple list (no tools)
            for (int i = 0; i < inventory.size(); i++) {
                var s = inventory.peek(i);
                if (s == null) continue;
                if (s.getType() instanceof ToolType) continue;
                flat.add(s);
            }
            favoritesCount = 0;
        }
        selected = flat.isEmpty() ? -1 : 0;
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        // panel
        batch.draw(panelBg, panelX, panelY, panelW, panelH);

        // title
        String title;
        if (mode == Mode.NPC) {
            title = (npc != null) ? ("GIFT TO " + npc.name.toUpperCase()) : "GIFT";
        } else {
            String pid = (targetPlayer != null) ? world.playerIdOf(targetPlayer) : "PLAYER";
            title = "GIFT TO " + pid;
        }
        GlyphLayout tl = new GlyphLayout(big, title);
        big.draw(batch, tl, panelX + (panelW - tl.width)/2f, panelY + panelH - 14f);

        // visible counts (cap at one row each)
        int favVisible    = Math.min(favoritesCount, COLS);
        int othersTotal   = flat.size() - favoritesCount;
        int othersVisible = Math.min(othersTotal, COLS);

        // Favorites row
        if (favVisible > 0) {
            small.draw(batch, "Favorites", panelX + 30f, row1TopY + LABEL_GAP);
            drawRow(batch, rowLeftX, row1TopY, /*start*/0, /*count*/favVisible);
        }

        // Others row
        if (othersVisible > 0) {
            float y = (mode == Mode.NPC) ? row2TopY : row1TopY;  // <-- use top row in PLAYER mode
            small.draw(batch, "Your Items", panelX + 30f, y + LABEL_GAP);
            drawRow(batch, rowLeftX, y, /*start*/favoritesCount, /*count*/othersVisible);
        }

        if (favVisible == 0 && othersVisible == 0) {
            small.draw(batch, "No giftable items.", panelX + 30f, panelY + panelH/2f);
        }

        // footer
        String hint = "ENTER: GIVE   G: CANCEL   CLICK: SELECT";
        GlyphLayout hl = new GlyphLayout(small, hint);
        small.draw(batch, hl, panelX + (panelW - hl.width)/2f, panelY + 28f);

        // toast
        if (toastTimer > 0f) {
            toastTimer -= Gdx.graphics.getDeltaTime();
            if (toastTimer > 0f && toastText != null) {
                GlyphLayout gl = new GlyphLayout(small, toastText);
                float cx = panelX + (panelW - gl.width) * 0.5f;
                float cy = panelY + 54f;
                small.draw(batch, gl, cx, cy);
            } else {
                toastText = null;
                toastTimer = 0f;
            }
        }

        handleInput(favVisible, othersVisible);
    }

    /** draw a single row (no wrapping) */
    private void drawRow(SpriteBatch batch, float leftX, float topY, int startIndex, int count) {
        for (int i = 0; i < count; i++) {
            int idx  = startIndex + i;
            float x  = leftX + i * (SLOT + PAD);
            float yT = topY;

            // slot & selection
            batch.draw(slotBg, x, yT - SLOT, SLOT, SLOT);
            if (idx == selected) {
                batch.draw(slotSel, x - 6, yT - SLOT - 6, SLOT + 15, SLOT + 15);
            }

            // icon & qty
            Inventory.Stack s = flat.get(idx);
            if (s != null) {
                ItemType t = s.getType();
                String iconPath = (t instanceof CropType) ? ((CropType) t).cropTexture() : t.iconPath();
                Texture icon = GameAssetManager.getGameAssetManager().getTexture(iconPath);
                batch.draw(icon, x + 8, yT - SLOT + 8, 32, 32);

                if (s.getQty() > 1) {
                    GameAssetManager.getGameAssetManager().getSmallFont().draw(
                        batch, String.valueOf(s.getQty()),
                        x + SLOT - 4, yT - SLOT + 14, 0, Align.right, false);
                }
            }

            // mouse select
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mx = Gdx.input.getX();
                float my = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (mx >= x && mx <= x + SLOT &&
                    my >= yT - SLOT && my <= yT) {
                    selected = idx;
                }
            }
        }
    }

    /** keyboard for a strict 2-row layout */
    private void handleInput(int favCols, int otherCols) {
        if (flat.isEmpty()) return;

        // clamp selection to visible window
        int totalVisible = Math.min(favoritesCount, COLS) + Math.min(flat.size() - favoritesCount, COLS);
        if (selected < 0) selected = 0;
        if (selected >= favoritesCount + otherCols) {
            selected = Math.max(0, favoritesCount + otherCols - 1);
        }
        if (selected < favoritesCount - Math.max(0, favoritesCount - favCols)) {
            // if there were >COLS favorites, only first COLS are visible; keep selection in that window
            selected = Math.min(selected, favCols - 1);
        }

        // row/col
        boolean inFav = selected < favoritesCount;
        int col = inFav ? selected : selected - favoritesCount;
        int row = inFav ? 0 : 1;

        // LEFT
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (col > 0) col--;
            else if (row == 1 && favCols > 0) { row = 0; col = favCols - 1; }
        }
        // RIGHT
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            int limit = (row == 0 ? favCols : otherCols);
            if (col < limit - 1) col++;
            else if (row == 0 && otherCols > 0) { row = 1; col = 0; }
        }
        // UP
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && row == 1 && favCols > 0) {
            row = 0; col = Math.min(col, favCols - 1);
        }
        // DOWN
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && row == 0 && otherCols > 0) {
            row = 1; col = Math.min(col, otherCols - 1);
        }

        selected = (row == 0) ? col : favoritesCount + col;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            giveSelected();
            close();
        }
    }

    private void giveSelected() {
        if (selected < 0 || selected >= flat.size()) return;

        Inventory.Stack s = flat.get(selected);
        if (s == null) return;
        ItemType t = s.getType();
        if (t instanceof ToolType) { toast("Can't give tool!"); return; }

        if (mode == Mode.NPC) {
            if (npc == null) return;
            boolean isFavorite = world.npc().isFavorite(npc.id, t);
            int points = world.npc().gift(gc, npc, t, gc.getGameTime()); // once/day inside
            if (points <= 0) { toast("Already gifted today"); return; }
            if (isFavorite && points < 200) {
                world.npc().addFriendPoints(gc, npc.id, 200 - points);
                points = 200;
            }
            inventory.remove(t, 1);
            gc.spawnFloatingIcon("gift.png", gc.getPlayerX()+8, gc.getPlayerY()+GameController.TILE_SIZE, 1.0f);
            toast(npc.name + "  +" + points);
            int old = selected; rebuildList();
            if (flat.isEmpty()) close(); else selected = Math.min(old, flat.size()-1);
            return;
        }

        // PLAYER mode
        if (targetPlayer == null) return;
        if (targetPlayer.getMap() != gc.getMap()) { toast("Player not on this map"); return; }
        float dx = targetPlayer.getPlayerX() - gc.getPlayerX();
        float dy = targetPlayer.getPlayerY() - gc.getPlayerY();
        if (dx*dx + dy*dy > 64f*64f) { toast("Get closer to give"); return; }

        String a = world.playerIdOf(gc);
        String b = world.playerIdOf(targetPlayer);
        int day  = gc.getGameTime().getDay();
        int delta = world.playerFriends().giftOncePerDay(a, b, day, false);
        if (delta <= 0) { toast("Already gifted today"); return; }

        if (inventory.remove(t, 1) == 0) { toast("No item left"); return; }
        targetPlayer.getPlayer().getInventory().add(t, 1);

        gc.spawnFloatingIcon("gift.png", gc.getPlayerX()+8, gc.getPlayerY()+GameController.TILE_SIZE, 1.0f);
        targetPlayer.spawnFloatingIcon("gift.png", targetPlayer.getPlayerX()+8, targetPlayer.getPlayerY()+GameController.TILE_SIZE, 1.0f);

        long now = System.currentTimeMillis();
        world.pushNotification(b, new Notification(a, b, "sent you a gift", now), true);
        toast("Gave +" + delta + " friendship");
        close();
    }

    private void toast(String s) { toastText = s; toastTimer = 1.6f; }


    public void openForPlayer(GameController other) {
        this.mode = Mode.PLAYER;
        this.targetPlayer = other;
        this.npc = null;
        rebuildList();
        visible = true;
    }
}
