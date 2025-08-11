package io.github.StardewValley.models;

import java.util.*;

public class NpcSocialService {
    public static final int POINTS_PER_HEART = 200; // you said level = points/200

    public static final class Personal {
        public int points = 0;
        public int lastTalkDay = Integer.MIN_VALUE;
        public int lastGiftDay = Integer.MIN_VALUE;
    }

    // npcId -> (playerId -> Personal)
    private final Map<String, Map<String, Personal>> bag = new HashMap<>();

    // Favorites & gift pool per NPC
    private final Map<String, Set<ItemType>> favorites = new HashMap<>();
    private final Map<String, List<ItemType>> sendableGifts = new HashMap<>();

    private Personal rel(String npcId, String playerId) {
        return bag.computeIfAbsent(npcId, n -> new HashMap<>())
            .computeIfAbsent(playerId, p -> new Personal());
    }

    // ---- Daily actions ------------------------------------------------------
    public int talkOncePerDay(String npcId, String playerId, int day) {
        Personal p = rel(npcId, playerId);
        if (p.lastTalkDay == day) return 0;
        p.lastTalkDay = day;
        p.points += 20;            // first talk of the day
        return 20;
    }

    public int giftOncePerDay(String npcId, String playerId, ItemType item, int day) {
        Personal p = rel(npcId, playerId);
        if (p.lastGiftDay == day) return 0;
        p.lastGiftDay = day;

        boolean fav = favorites.getOrDefault(npcId, Set.of()).contains(item);
        int delta = fav ? 200 : 50;
        p.points += delta;
        return delta;
    }

    // ---- View --------------------------------------------------------------
    public int points(String npcId, String playerId) {
        return rel(npcId, playerId).points;
    }

    public int level(String npcId, String playerId) {
        int lv = points(npcId, playerId) / POINTS_PER_HEART;
        return Math.max(0, Math.min(3, lv));
    }

    // ---- Config ------------------------------------------------------------
    public void setFavorites(String npcId, Set<ItemType> favs) {
        favorites.put(npcId, new HashSet<>(favs));
    }

    public void setSendableGifts(String npcId, List<ItemType> gifts) {
        sendableGifts.put(npcId, new ArrayList<>(gifts));
    }

    public List<ItemType> getSendable(String npcId) {
        return sendableGifts.getOrDefault(npcId, List.of());
    }

    public void addPoints(String npcId, String playerId, int delta) {
        rel(npcId, playerId).points += delta;
    }

}

