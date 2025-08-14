package io.github.StardewValley.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.controllers.GameController;
import io.github.StardewValley.controllers.WorldController;
import io.github.StardewValley.models.Inventory;
import io.github.StardewValley.models.ItemType;
import io.github.StardewValley.models.NpcCharacter;
import io.github.StardewValley.models.QuestBook;
import io.github.StardewValley.models.QuestDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Journal overlay (F) that lists unlocked, not-yet-completed NPC quests + progress. */
public class JournalOverlay {
    private boolean visible = false;

    private final WorldController world;
    private final GameController  playerCtrl;

    public JournalOverlay(WorldController world, GameController playerCtrl) {
        this.world = world;
        this.playerCtrl = playerCtrl;
    }

    public void toggle() { visible = !visible; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean v) { visible = v; }

    public void render(SpriteBatch batch, BitmapFont big, BitmapFont small, float screenW, float screenH) {
        if (!visible) return;

        // --- Title ---
        String title = "Journal";
        GlyphLayout t = new GlyphLayout(big, title);
        big.setColor(Color.WHITE);
        big.draw(batch, t, (screenW - t.width) / 2f, screenH - 40);

        // Gather open quests
        List<JournalEntry> open = collectOpenQuests();

        float startY = screenH - 90f;
        float lineGap = 6f;

        if (open.isEmpty()) {
            GlyphLayout b = new GlyphLayout(small, "- No active NPC quests\n- Daily tasks coming soon");
            small.setColor(Color.WHITE);
            small.draw(batch, b, (screenW - b.width) / 2f, startY);
            return;
        }

        float y = startY;
        for (JournalEntry e : open) {
            // header (centered)
            String headerText = e.npcName + " — " + e.title;
            GlyphLayout headerLayout = new GlyphLayout(small, headerText);
            small.setColor(Color.valueOf("CFE7FF"));
            small.draw(batch, headerLayout, (screenW - headerLayout.width) / 2f, y);
            y -= (small.getCapHeight() + lineGap);

            // requirements (center each line individually)
            if (e.requirements.isEmpty()) {
                GlyphLayout reqLayout = new GlyphLayout(small, "(No items required)");
                small.setColor(Color.WHITE);
                small.draw(batch, reqLayout, (screenW - reqLayout.width) / 2f, y);
                y -= (small.getCapHeight() + lineGap);
            } else {
                for (ReqProgress rp : e.requirements) {
                    String reqText = rp.label + " x" + rp.need + "  (" + rp.have + "/" + rp.need + ")";
                    GlyphLayout reqLayout = new GlyphLayout(small, reqText);
                    if (rp.have >= rp.need) {
                        small.setColor(Color.WHITE);
                    } else {
                        small.setColor(1f, 0.6f, 0.6f, 1f); // reddish if missing items
                    }
                    small.draw(batch, reqLayout, (screenW - reqLayout.width) / 2f, y);
                    small.setColor(Color.WHITE);
                    y -= (small.getCapHeight() + lineGap);
                }
            }

            y -= (small.getCapHeight()); // extra gap between quests

            if (y < 80f) break;
        }
    }

    // --- data containers ---
    private static final class JournalEntry {
        String npcId;
        String npcName;
        String title;
        List<ReqProgress> requirements = new ArrayList<>();
    }
    private static final class ReqProgress {
        String label;
        int have, need;
    }

    private List<JournalEntry> collectOpenQuests() {
        List<JournalEntry> out = new ArrayList<>();
        if (world == null || playerCtrl == null) return out;

        var npcList = world.npc().all();
        int day = world.getSharedTime().getDay();

        for (NpcCharacter npc : npcList) {
            List<QuestDef> quests = QuestBook.getForNpc(npc.id);
            if (quests == null || quests.isEmpty()) continue;

            for (QuestDef q : quests) {
                if (isCompleted(npc.id, q)) continue;
                if (isLocked(npc.id, q, day)) continue;

                JournalEntry je = new JournalEntry();
                je.npcId   = npc.id;
                je.npcName = npc.name;
                je.title   = q.title;

                // Build requirement progress
                Inventory bag = playerCtrl.getPlayer().getInventory();
                for (Map.Entry<ItemType,Integer> e : q.requirements.entrySet()) {
                    ReqProgress rp = new ReqProgress();
                    rp.label = labelFor(e.getKey());
                    rp.need  = e.getValue();
                    rp.have  = bag.getTotalQty(e.getKey());
                    je.requirements.add(rp);
                }

                out.add(je);
            }
        }
        return out;
    }

    private boolean isCompleted(String npcId, QuestDef q) {
        return switch (q.index) {
            case 1 -> world.npc().isQ1Done(npcId);
            case 2 -> world.npc().isQ2Done(npcId);
            case 3 -> world.npc().isQ3Done(npcId);
            default -> true; // unknown index: treat as not showable
        };
    }

    private boolean isLocked(String npcId, QuestDef q, int day) {
        // Match NpcQuestPopupView logic:
        int lv = world.npc().level(playerCtrl, npcId);
        if (q.index == 2) {
            // Q2 requires at least Lv1 friendship
            return lv < Math.max(1, q.minFriendLevel);
        }
        if (q.index == 3) {
            // Q3 requires Q2 done + possibly day gating handled by service
            return !world.npc().isQ3Unlocked(npcId, day);
        }
        // Q1 generally available
        return false;
    }

    // A short label for a requirement line; you can customize per type if you like.
    private String labelFor(ItemType t) {
        // Prefer readable id/title if you have one; fallback to class name
        String id = t.id();
        if (id != null && !id.isEmpty()) return id;
        String cn = t.getClass().getSimpleName();
        return (cn == null || cn.isEmpty()) ? "Item" : cn;
    }
}
