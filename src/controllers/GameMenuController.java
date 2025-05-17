package controllers;

import models.*;
import models.enums.Menu;
import models.enums.Seasons;
import models.enums.Types.TileType;
import models.enums.Weather;

import java.util.*;

public class GameMenuController {

    private final List<Player> players = new ArrayList<>();
    private Game currentGame;
    private GamePlayController gameplay;

    public Result newGame(String usernamesInput) {
        String[] usernames = usernamesInput.trim().split("\\s+");
        if (usernames.length < 1 || usernames.length > 3) {
            return new Result(false, "You must provide 1 to 3 usernames.");
        }

        Set<String> seen = new HashSet<>();
        players.clear();
        for (String username : usernames) {
            if (!App.usernameExists(username)) {
                return new Result(false, "Username '" + username + "' does not exist.");
            }
            if (!seen.add(username)) {
                return new Result(false, "Duplicate username: " + username);
            }
            User user = App.getUserByUsername(username);
            players.add(new Player(user));
        }

        List<Shop> shops = new ArrayList<>();
        int width  = 40, height = 50;
        List<GameMap> maps = new ArrayList<>();

        for (Player p : players) {
            Tile[][] tiles = new Tile[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    tiles[x][y] = new Tile(TileType.REGULAR_GROUND, true, new Position(x, y));
                }
            }
            maps.add(new GameMap(tiles, width, height, shops, players, Weather.SUNNY));
        }

        currentGame = new Game(shops, players, Weather.SUNNY, Seasons.SPRING, maps);
        currentGame.getTime().setHour(9);

        App.addGame(currentGame);
        App.setCurrentGame(currentGame);

        gameplay = new GamePlayController(currentGame);

        return new Result(true, "New game created! It's 9:00 AM on Spring Day 1. Let’s play!");
    }

    public Result loadGame() {
        Game saved = App.getLatestSavedGame();
        if (saved == null) {
            return new Result(false, "No saved game found.");
        }
        App.setCurrentGame(saved);
        return new Result(true, "Game loaded successfully.");
    }

    public Result chooseMap(int mapNumber) {
        if (currentGame == null) {
            return new Result(false, "No active game. Please start a new game first.");
        }

        List<GameMap> availableMaps = currentGame.getGameMaps();
        if (mapNumber < 1 || mapNumber > availableMaps.size()) {
            return new Result(false, "Invalid map number. Please choose between 1 and " + availableMaps.size() + ".");
        }

        GameMap selectedMap = availableMaps.get(mapNumber - 1);
        Player currentPlayer = currentGame.getCurrentPlayer();

        currentGame.assignMapToPlayer(currentPlayer, selectedMap);
        return new Result(true, "Map " + mapNumber + " assigned to player " + currentPlayer.getName());
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
