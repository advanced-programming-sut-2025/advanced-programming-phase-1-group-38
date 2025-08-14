package io.github.StardewValley.controllers;

// WorldController.java
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.enums.Types.ShopType;
import io.github.StardewValley.views.NpcWorldSlice;
import io.github.StardewValley.views.PlayerWorldSlice;

import java.util.*;

public class WorldController {
    private final GameTime sharedTime = new GameTime();
    private final Player[] players = new Player[4];
    private final GameController[] controllers = new GameController[4];
    private final PlayerWorldSlice npcSlice;

    private final java.util.Map<String, java.util.ArrayDeque<Notification>> inbox = new java.util.HashMap<>();
    private final java.util.Map<String, Integer> unreadByPlayer = new java.util.HashMap<>();

    private final NpcController npcController;
    private final Map<GameController, String> playerIds = new HashMap<>();
    private final PlayerFriendService playerFriends = new PlayerFriendService();
    private final Map<ShopType, Shop> liveShops = new HashMap<>();

    private int currentPlayerIndex = 0;
    private int turnsIntoHour = 0;
    private final int playersPerHour = 4;
    private int lastGiftDay = -1;

    private final Weather weather = new Weather(WeatherType.SUNNY);
    private final java.util.Random rng = new java.util.Random();

    public WorldController(String[] chosenMaps) {
        // one shared NPC map instance
        this.npcSlice = new NpcWorldSlice("maps/npcMap.tmx");
        TiledMap sharedNpcMap = npcSlice.getMap();

        weather.setWeatherType(rollWeatherForDay(sharedTime.getDay()));

        // services + player-id provider
        NpcSocialService social = new NpcSocialService();
        NpcQuestService  quests = new NpcQuestService();
        NpcController.PlayerIdProvider pid = (gc) -> playerIds.get(gc);

        this.npcController = new NpcController(social, quests, pid);
        npcController.bootstrapDefaults("maps/npcMap.tmx");

        npcController.addSellBin("maps/npcMap.tmx", 14, 20, 1, 1, "shops/sell_bucket.png");

        String npcMapPath = npcSlice.getMapId(); // "maps/npcMap.tmx"

//// Add more as you add catalogs:
//        npcController.addManualShop(npcMapPath,
//            io.github.StardewValley.models.enums.Types.ShopType.PIERRE_STORE,
//            20, 12, 2, 2, "shops/pierre.png");

        // players + controllers
        for (int i = 0; i < 4; i++) {
            players[i] = new Player(560, 380, 24);
            controllers[i] = new GameController(players[i], chosenMaps[i], sharedTime, sharedNpcMap);
            controllers[i].setWorldController(this);
            playerIds.put(controllers[i], "P" + i);   // stable per-player id
        }
    }

    public void updateAll(float delta) {
        for (int i = 0; i < controllers.length; i++) {
            boolean canAct = (i == currentPlayerIndex);
            controllers[i].update(delta, canAct);
        }
        npcController.update(delta);

        // daily gift roll (once per day at day start)
        if (sharedTime.getDay() != lastGiftDay) {
            npcController.onNewDay(sharedTime.getDay(), Arrays.asList(controllers));
            lastGiftDay = sharedTime.getDay();
        }
    }

    public void endTurnAndAdvanceIfRoundDone() {
        currentPlayerIndex = (currentPlayerIndex + 1) % controllers.length;
        turnsIntoHour++;
        if (turnsIntoHour >= playersPerHour) {
            int beforeDay = sharedTime.getDay();
            sharedTime.advanceOneHour();
            int afterDay = sharedTime.getDay();
            if (afterDay != beforeDay) {
                // reset shops & decay friendships (your code)
                for (io.github.StardewValley.models.Shop shop : liveShops.values()) shop.resetStock();
                playerFriends().endOfDayDecay(beforeDay);

                // NEW: roll shared weather for the new day
                weather.setWeatherType(rollWeatherForDay(afterDay));
            }
            turnsIntoHour = 0;
        }
    }

    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public GameController[] getAllControllers() { return controllers; }
    public GameTime getSharedTime() { return sharedTime; }
    public PlayerWorldSlice getNpcSlice() { return npcSlice; }
    public NpcController npc() { return npcController; }

    public void pushNotification(String toPlayerId, Notification n) {
        pushNotification(toPlayerId, n, /*countUnread*/ true);
    }

    public void pushNotification(String toPlayerId, Notification n, boolean countUnread) {
        inbox.computeIfAbsent(toPlayerId, k -> new java.util.ArrayDeque<>()).addLast(n);
        var q = inbox.get(toPlayerId);
        while (q.size() > 50) q.removeFirst();
        if (countUnread) {
            unreadByPlayer.put(toPlayerId, getUnreadCount(toPlayerId) + 1);
        }
    }

    public int getUnreadCount(String playerId) {
        return unreadByPlayer.getOrDefault(playerId, 0);
    }

    public void clearUnread(String playerId) {
        unreadByPlayer.put(playerId, 0);
    }

    public java.util.Deque<Notification> getInbox(String playerId) {
        return inbox.getOrDefault(playerId, new java.util.ArrayDeque<>());
    }

    public PlayerFriendService playerFriends() { return playerFriends; }
    public String playerIdOf(GameController gc) { return playerIds.get(gc); }

    // WorldController.java (helper)
    public GameController nearestOtherPlayer(GameController me, float maxDist) {
        GameController best = null;
        float bestD2 = maxDist * maxDist;

        TiledMap map = me.getMap();
        float mx = me.getPlayerX(), my = me.getPlayerY();

        for (GameController gc : controllers) {
            if (gc == me) continue;
            if (gc.getMap() != map) continue; // same *instance* matters

            float dx = gc.getPlayerX() - mx, dy = gc.getPlayerY() - my;
            float d2 = dx*dx + dy*dy;
            if (d2 <= bestD2) { bestD2 = d2; best = gc; }
        }
        return best;
    }

    // WorldController.java
    public java.util.List<GameController> nearbyPlayers(GameController me, float radius) {
        java.util.ArrayList<GameController> out = new java.util.ArrayList<>();
        float r2 = radius * radius;
        TiledMap map = me.getMap();
        float mx = me.getPlayerX(), my = me.getPlayerY();

        for (GameController gc : controllers) {
            if (gc == me) continue;
            if (gc.getMap() != map) continue;
            float dx = gc.getPlayerX() - mx, dy = gc.getPlayerY() - my;
            float d2 = dx*dx + dy*dy;
            if (d2 <= r2) out.add(gc);
        }
        // optional: sort by distance or stable playerId
        out.sort((a,b) -> playerIdOf(a).compareTo(playerIdOf(b)));
        return out;
    }

    public static class GiftReceipt {
        public final String fromId, toId, itemId, itemName;
        public final long ts;
        public Integer rating; // null until rated
        public GiftReceipt(String fromId, String toId, String itemId, String itemName, long ts) {
            this.fromId = fromId; this.toId = toId;
            this.itemId = itemId; this.itemName = itemName; this.ts = ts;
        }
    }

    private final java.util.Map<String, java.util.Deque<GiftReceipt>> giftInbox = new java.util.HashMap<>();

    public void recordGift(String fromId, String toId, String itemId, String itemName, long ts) {
        giftInbox.computeIfAbsent(toId, k -> new java.util.ArrayDeque<>())
            .add(new GiftReceipt(fromId, toId, itemId, itemName, ts));
    }

    // gifts between selected → me (only incoming to me)
    public java.util.List<GiftReceipt> giftsBetween(String myId, String otherId) {
        java.util.Deque<GiftReceipt> deq = giftInbox.getOrDefault(myId, new java.util.ArrayDeque<>());
        java.util.ArrayList<GiftReceipt> out = new java.util.ArrayList<>();
        for (GiftReceipt r : deq) if (r.fromId.equals(otherId)) out.add(r);
        return out;
    }

    public GiftReceipt lastUnratedFromTo(String fromId, String toId) {
        java.util.Deque<GiftReceipt> deq = giftInbox.getOrDefault(toId, new java.util.ArrayDeque<>());
        java.util.Iterator<GiftReceipt> it = deq.descendingIterator();
        while (it.hasNext()) {
            GiftReceipt r = it.next();
            if (r.fromId.equals(fromId) && r.rating == null) return r;
        }
        return null;
    }

    // apply rating & friendship formula if first gift today
    public void rateGift(GiftReceipt r, int rating) {
        if (r == null || rating < 1 || rating > 5) return;
        r.rating = rating;

        int day = getSharedTime().getDay();
        // only first gift of the day from A -> B gets points
        if (playerFriends().markFirstGiftToday(r.fromId, r.toId, day)) {
            int points = (rating - 3) * 30 + 15;
            playerFriends().add(r.fromId, r.toId, points);
        }
    }

    public Shop getLiveShop(ShopType t) {
        return liveShops.computeIfAbsent(t, Shop::new);
    }

    private WeatherType rollWeatherForDay(int day) {
        int r = rng.nextInt(100);
        if (r < 60) return WeatherType.SUNNY;
        if (r < 80) return WeatherType.RAINY;
        if (r < 90) return WeatherType.STORMY;
        return WeatherType.SNOWY;
    }

    public Weather getWeather() { return weather; }


    public void dispose() {
        for (GameController gc : controllers) if (gc != null) gc.dispose();
        if (npcSlice instanceof NpcWorldSlice ns) ns.dispose();
    }
}
