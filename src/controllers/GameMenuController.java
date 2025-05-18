package controllers;

import models.*;
import models.enums.Menu;
import models.enums.Seasons;
import models.enums.Types.TileType;
import models.enums.Weather;
import views.GamePlay;

import java.util.*;

public class GameMenuController {

    private final ArrayList<Player> players = new ArrayList<>();
    private Game currentGame;
    private GamePlayController gameplay;
    private int mapSelectionIndex = 0;
    private boolean waitingForMapSelection = false;

    public Result newGame(String usernamesInput) {
        String[] usernames = usernamesInput.trim().split("\\s+");
        if (usernames.length < 1 || usernames.length > 3) {
            return new Result(false, "You must provide 1 to 3 usernames.");
        }
        players.clear();
        Set<String> seen = new HashSet<>();
        for (String u : usernames) {
            if (!App.usernameExists(u)) {
                return new Result(false, "Username '" + u + "' does not exist.");
            }
            if (!seen.add(u)) {
                return new Result(false, "Duplicate username: " + u);
            }
            players.add(new Player(App.getUserByUsername(u)));
        }

        List<Shop> shops = new ArrayList<>();

        final int W = 40, H = 50;
        Tile[][] tiles1 = buildBaseTiles(W, H);
        Tile[][] tiles2 = buildBaseTiles(W, H);

        List<GameMap> maps = new ArrayList<>();

        // big lake, small quarry
        GameMap lakeHeavy = new GameMap(tiles1, W, H, shops, players, Weather.SUNNY);
        lakeHeavy.markLakeArea(new Position(12, 6), 8, 6);
        lakeHeavy.markQuarryArea(new Position(20, 10), 4, 3);
        maps.add(lakeHeavy);

        // small lake, big quarry
        GameMap quarryHeavy = new GameMap(tiles2, W, H, shops, players, Weather.SUNNY);
        quarryHeavy.markLakeArea(new Position(12, 6), 4, 3);
        quarryHeavy.markQuarryArea(new Position(20, 10), 8, 6);
        maps.add(quarryHeavy);

        currentGame = new Game(shops,
                players,
                Weather.SUNNY,
                Seasons.SPRING,
                maps);
        currentGame.getTime().setHour(9);

        App.addGame(currentGame);
        App.setCurrentGame(currentGame);

        gameplay = new GamePlayController(currentGame);
        mapSelectionIndex = 0;
        waitingForMapSelection = true;

        return new Result(true,
                "New game created! Please choose your farm map:\n" +
                        "  1) Lake-heavy layout\n" +
                        "  2) Quarry-heavy layout\n" +
                        "Use ‘map 1’ or ‘map 2’ to select.");
    }

    private Tile[][] buildBaseTiles(int w, int h) {
        Tile[][] t = new Tile[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                t[x][y] = new Tile(TileType.REGULAR_GROUND, true, new Position(x, y));
            }
        }
        return t;
    }

    public Result chooseMap(int mapNumber) {
        if (currentGame == null || !waitingForMapSelection) {
            return new Result(false, "No active map selection.");
        }

        List<GameMap> gm = currentGame.getGameMaps();
        if (mapNumber < 1 || mapNumber > gm.size()) {
            return new Result(false, "Invalid map number. Choose 1 or " + gm.size());
        }

        GameMap chosen = gm.get(mapNumber - 1);

        if (mapSelectionIndex >= players.size()) {
            return new Result(false, "All players have already selected maps.");
        }

        Player p = players.get(mapSelectionIndex);
        currentGame.assignMapToPlayer(p, chosen);
        String result = "Map " + mapNumber + " assigned to player " + p.getName();
        mapSelectionIndex++;

        if (mapSelectionIndex == players.size()) {
            waitingForMapSelection = false;
            App.setCurrentGame(currentGame);
            App.setCurrentMenu(Menu.GAMEPLAY_MENU);
            Menu.GAMEPLAY_MENU.setMenuInstance(new GamePlay());
            currentGame.setCurrentPlayer(players.get(0));
            result += "\nAll maps selected. Game is ready to play!";
        }
        return new Result(true, result);
    }
    public Result loadGame() {
        Game saved = App.getLatestSavedGame();
        if (saved == null) {
            return new Result(false, "No saved game found.");
        }
        App.setCurrentGame(saved);
        return new Result(true, "Game loaded successfully.");
    }

    public Result enterMenu() {
        App.setCurrentMenu(Menu.GAME_MENU);
        return new Result(true, "Entered Game Menu.");
    }

    public Result exitMenu() {
        App.setCurrentMenu(Menu.MAIN_MENU);
        return new Result(true, "Exited to Main Menu.");
    }

    public Result showCurrentMenu() {
        return new Result(true, "Current Menu: " + App.getCurrentMenu().getDisplayName());
    }
}
