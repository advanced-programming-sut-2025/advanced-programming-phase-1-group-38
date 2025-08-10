package io.github.StardewValley.controllers;

import com.badlogic.gdx.maps.tiled.TiledMap; // 👈 add this
import io.github.StardewValley.models.GameTime;
import io.github.StardewValley.models.Player;
import io.github.StardewValley.views.NpcWorldSlice;
import io.github.StardewValley.views.PlayerWorldSlice;

public class WorldController {
    private final GameTime sharedTime = new GameTime();
    private final Player[] players = new Player[4];
    private final GameController[] controllers = new GameController[4];
    private final PlayerWorldSlice npcSlice;

    private int currentPlayerIndex = 0;
    private int turnsIntoHour = 0;
    private final int playersPerHour = 4;

    public WorldController(String[] chosenMaps) {
        // 👇 build shared NPC slice FIRST and grab its map instance
        this.npcSlice = new NpcWorldSlice("maps/npcMap.tmx");
        TiledMap sharedNpcMap = npcSlice.getMap(); // one instance shared by everyone

        for (int i = 0; i < 4; i++) {
            players[i] = new Player(100, 100, 24);
            // 👇 pass the shared NPC map into each controller
            controllers[i] = new GameController(players[i], chosenMaps[i], sharedTime, sharedNpcMap);
        }
    }

    public void updateAll(float delta) {
        for (int i = 0; i < controllers.length; i++) {
            boolean canAct = (i == currentPlayerIndex);
            controllers[i].update(delta, canAct);
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
    public void dispose() {
        for (GameController gc : controllers) {
            if (gc != null) gc.dispose();
        }
        if (npcSlice instanceof NpcWorldSlice ns) {
            ns.dispose();
        }
    }
}
