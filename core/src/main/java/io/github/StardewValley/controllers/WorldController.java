package io.github.StardewValley.controllers;

// WorldController.java
import com.badlogic.gdx.maps.tiled.TiledMap;
import io.github.StardewValley.models.GameTime;
import io.github.StardewValley.models.Player;
import io.github.StardewValley.models.NpcSocialService;
import io.github.StardewValley.models.NpcQuestService;
import io.github.StardewValley.views.NpcWorldSlice;
import io.github.StardewValley.views.PlayerWorldSlice;

import java.util.*;

public class WorldController {
    private final GameTime sharedTime = new GameTime();
    private final Player[] players = new Player[4];
    private final GameController[] controllers = new GameController[4];
    private final PlayerWorldSlice npcSlice;

    private final NpcController npcController;
    private final Map<GameController, String> playerIds = new HashMap<>();

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
            players[i] = new Player(100, 100, 24);
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
            turnsIntoHour = 0;
        }
    }

    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public GameController[] getAllControllers() { return controllers; }
    public GameTime getSharedTime() { return sharedTime; }
    public PlayerWorldSlice getNpcSlice() { return npcSlice; }
    public NpcController npc() { return npcController; }

    public void dispose() {
        for (GameController gc : controllers) if (gc != null) gc.dispose();
        if (npcSlice instanceof NpcWorldSlice ns) ns.dispose();
    }
}
