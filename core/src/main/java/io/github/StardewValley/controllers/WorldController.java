package io.github.StardewValley.controllers;

// WorldController.java
import com.badlogic.gdx.maps.tiled.TiledMap;
import io.github.StardewValley.models.*;
import io.github.StardewValley.models.Artisan.PlacedMachine;
import io.github.StardewValley.models.Artisan.MachineType;
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

    private int currentPlayerIndex = 0;
    private int turnsIntoHour = 0;
    private final int playersPerHour = 4;
    private int lastGiftDay = -1;

    public WorldController(String[] chosenMaps) {
        // one shared NPC map instance
        this.npcSlice = new NpcWorldSlice("maps/npcMap.tmx");
        TiledMap sharedNpcMap = npcSlice.getMap();

        // services + player-id provider
        NpcSocialService social = new NpcSocialService();
        NpcQuestService  quests = new NpcQuestService();
        NpcController.PlayerIdProvider pid = (gc) -> playerIds.get(gc);

        this.npcController = new NpcController(social, quests, pid);
        npcController.bootstrapDefaults("maps/npcMap.tmx");

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
            sharedTime.advanceOneHour();
            // ⬇️ مهم: ماشین‌های همهٔ پلیرها را یک ساعت جلو ببر
            for (GameController gc : controllers) {
                gc.advanceMachinesGameHours(1f);
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




    private final java.util.Map<String, java.util.List<PlacedMachine>> machinesByMap = new java.util.HashMap<>();

    private java.util.List<PlacedMachine> listFor(String mapId){
        return machinesByMap.computeIfAbsent(mapId, k -> new java.util.ArrayList<>());
    }

    public boolean hasMachineAt(String mapId, int tx, int ty) {
        for (PlacedMachine pm : listFor(mapId)) {
            if (pm.tx() == tx && pm.ty() == ty) return true;
        }
        return false;
    }

    public PlacedMachine findMachineAt(String mapId, int tx, int ty) {
        for (PlacedMachine pm : listFor(mapId)) {
            if (pm.tx() == tx && pm.ty() == ty) return pm;
        }
        return null;
    }

    public PlacedMachine placeMachine(String mapId, int tx, int ty, MachineType type) {
        PlacedMachine pm = new PlacedMachine(mapId, tx, ty, type);
        listFor(mapId).add(pm);
        return pm;
    }

    public boolean canPlaceMachineAt(String mapId, int tx, int ty, Tile[][] tiles) {
        if (tx < 0 || ty < 0 || tx >= tiles.length || ty >= tiles[0].length) return false;
        if (hasMachineAt(mapId, tx, ty)) return false;
        Tile t = tiles[tx][ty];
        if (t.hasCrop()) return false;                // روی محصول نذار
        // اگر لایه‌ی بلاک/کلاژن داری، همین‌جا چک کن.
        return true;
    }

    public void updateMachines(float dt) {
        for (var list : machinesByMap.values())
            for (var m : list) m.update(dt);
    }




    public void dispose() {
        for (GameController gc : controllers) if (gc != null) gc.dispose();
        if (npcSlice instanceof NpcWorldSlice ns) ns.dispose();
    }
}
