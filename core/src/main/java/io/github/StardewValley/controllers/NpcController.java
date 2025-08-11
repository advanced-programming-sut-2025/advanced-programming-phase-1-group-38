package io.github.StardewValley.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.NpcQuestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NpcController {
    public interface PlayerIdProvider {
        String getPlayerId(GameController gc);
    }

    private final List<NpcCharacter> npcs = new ArrayList<>();
    private final NpcSocialService social;
    private final NpcQuestService quests;
    private final PlayerIdProvider pid;

    public NpcController(NpcSocialService social, NpcQuestService quests, PlayerIdProvider pid) {
        this.social = social;
        this.quests = quests;
        this.pid = pid;
    }

    public void addNpc(NpcCharacter npc) {
        npcs.add(npc);
    }

    public void update(float dt) {
        for (var n : npcs) n.update(dt);
    }

    public List<NpcCharacter> getNpcsOn(String mapPath) {
        List<NpcCharacter> out = new ArrayList<>();
        for (var n : npcs) if (mapPath != null && mapPath.equals(n.getMapPath())) out.add(n);
        return out;
    }

    public void renderOn(SpriteBatch b, String mapPath) {
        for (var n : getNpcsOn(mapPath)) n.render(b);
    }

    // ---- Daily level-3 gift roll (call at start of day) --------------------
    public void onNewDay(int day, List<GameController> players) {
        for (var n : npcs) {
            List<ItemType> pool = social.getSendable(n.id);
            if (pool.isEmpty()) continue;
            for (var gc : players) {
                String playerId = pid.getPlayerId(gc);
                int lv = social.level(n.id, playerId);
                if (lv >= 3 && MathUtils.randomBoolean(0.5f)) {
                    ItemType gift = pool.get(MathUtils.random(pool.size() - 1));
                    gc.getPlayer().getInventory().add(gift, 1);
                    // (optional) toast: n.name + " sent you " + gift
                }
            }
        }
    }

    // ---- Interactions ------------------------------------------------------
    public int talk(GameController gc, NpcCharacter npc, GameTime time) {
        return social.talkOncePerDay(npc.id, pid.getPlayerId(gc), time.getDay());
    }

    public int gift(GameController gc, NpcCharacter npc, ItemType item, GameTime time) {
        return social.giftOncePerDay(npc.id, pid.getPlayerId(gc), item, time.getDay());
    }

    public int level(GameController gc, String npcId) {
        return social.level(npcId, pid.getPlayerId(gc));
    }

    // ---- Quests (global winner) -------------------------------------------
    public boolean completeQ1(GameController gc, String npcId, GameTime t) {
        return quests.tryCompleteQ1(npcId);
    }

    public boolean completeQ2(GameController gc, String npcId, GameTime t) {
        int lv = level(gc, npcId);
        return quests.tryCompleteQ2(npcId, t.getDay(), lv >= 1);
    }

    public boolean completeQ3(GameController gc, String npcId, GameTime t) {
        return quests.tryCompleteQ3(npcId, t.getDay());
    }

    // ---- Config pass-through ----------------------------------------------
    public void setFavorites(String npcId, Set<ItemType> favs) {
        social.setFavorites(npcId, favs);
    }

    public void setSendableGifts(String npcId, List<ItemType> gifts) {
        social.setSendableGifts(npcId, gifts);
    }

    public void bootstrapDefaults(String npcMapPath) {
        final float TILE = 16f;
        java.util.function.BiFunction<Integer,Integer,float[]> px =
            (tx, ty) -> new float[]{ tx * TILE, ty * TILE };

//        // Leah -> assets/npc/leah/1.png..4.png
//        {
//            float[] p = px.apply(20, 15);
//            NpcCharacter leah = new NpcCharacter("leah", "Leah", npcMapPath, p[0], p[1]);
//            leah.setAnimation("idle", "npc/leah/", 4, 0.18f, true);
//            addNpc(leah);
//            social.setFavorites("leah", setOf(CropType.CARROT, CropType.CORN));
//            social.setSendableGifts("leah", java.util.Arrays.asList(CropType.CARROT, CropType.CORN));
//        }

        // Gus -> assets/npc/gus/1.png..4.png
//        {
//            float[] p = px.apply(34, 9);
//            NpcCharacter gus = new NpcCharacter("gus", "Gus", npcMapPath, p[0], p[1]);
//            gus.setAnimation("idle", "npc/gus/", 4, 0.18f, true);
//            addNpc(gus);
//            social.setFavorites("gus", setOf(CropType.CORN));
//            social.setSendableGifts("gus", java.util.Arrays.asList(CropType.CORN));
//        }

        // Robin -> assets/npc/robin/1.png..4.png
        {
            float[] p = px.apply(12, 22);
            NpcCharacter robin = new NpcCharacter("robin", "Robin", npcMapPath, p[0], p[1]);
            robin.setAnimation("idle", "npc/robin/", 4, 0.18f, true);
            addNpc(robin);
            social.setFavorites("robin", setOf(FoodType.SPAGHETTI, MaterialType.Wood, MaterialType.IronBar));
            social.setSendableGifts("robin", java.util.Arrays.asList(MaterialType.Wood, MaterialType.IronBar));
        }
    }

    // --- expose list for the UI
    public java.util.List<NpcCharacter> all() {
        return java.util.Collections.unmodifiableList(npcs);
    }

    // --- quest state pass-throughs
    public boolean isQ1Done(String npcId){ return quests.isQ1Done(npcId); }
    public boolean isQ2Done(String npcId){ return quests.isQ2Done(npcId); }
    public boolean isQ3Done(String npcId){ return quests.isQ3Done(npcId); }
    public boolean isQ3Unlocked(String npcId, int day){ return quests.isQ3Unlocked(npcId, day); }
    public int daysUntilQ3(String npcId, int day){ return quests.daysUntilQ3(npcId, day); }
    public void addFriendPoints(GameController gc, String npcId, int delta){
        social.addPoints(npcId, pid.getPlayerId(gc), delta);
    }
    // --- social helpers
    public int points(GameController gc, String npcId){
        return social.points(npcId, pid.getPlayerId(gc));
    }

    public NpcCharacter closestOn(String mapPath, float x, float y, float maxDist) {
        NpcCharacter best = null;
        float bestD2 = maxDist * maxDist;
        for (var n : npcs) {
            if (!mapPath.equals(n.getMapPath())) continue;
            float dx = n.getX() - x, dy = n.getY() - y;
            float d2 = dx * dx + dy * dy;
            if (d2 <= bestD2) {
                bestD2 = d2;
                best = n;
            }
        }
        return best;
    }

    private static java.util.Set<ItemType> setOf(ItemType... items) {
        return new java.util.HashSet<>(java.util.Arrays.asList(items));
    }

}
