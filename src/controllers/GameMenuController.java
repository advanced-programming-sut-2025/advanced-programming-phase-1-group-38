package controllers;

import models.*;
import models.enums.Menu;
import models.enums.Seasons;
import models.enums.Weather;

import java.util.*;

public class GameMenuController {

    private final List<Player> players = new ArrayList<>();
    private Game currentGame;

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
            Player player = new Player(user); // فرض بر اینکه Constructor دارد
            players.add(player);
        }

        List<GameMap> maps = List.of(new GameMap(), new GameMap(), new GameMap());

        List<Shop> shops = new ArrayList<>();

        currentGame = new Game(shops, players, Weather.SUNNY, Seasons.SPRING, maps);
        App.addGame(currentGame);
        App.setCurrentGame(currentGame);

        return new Result(true, "New game created. Players joined. Game started.");
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