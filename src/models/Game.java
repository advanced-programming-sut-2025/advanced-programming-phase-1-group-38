package models;

import models.enums.Weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private List<Shop> shops;
    private int currentPlayerIndex = 0;
    private Weather currentWeather;
    private List<GameMap> gameMaps;
    private Map<Player, GameMap> playerGameMap;

    public Game(List<Shop> shops, List<Player> players, Weather startingWeather, List<GameMap> gameMaps) {
        this.shops = shops;
        this.players = players;
        if (!players.isEmpty()) {
            this.currentPlayer = players.get(0);
        }
        this.currentWeather = startingWeather;
        this.gameMaps = gameMaps;
        this.playerGameMap = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            playerGameMap.put(players.get(i), gameMaps.get(i % gameMaps.size()));
        }
    }

    public List<Shop> getShops() {
        return shops;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
    public GameMap getCurrentPlayerMap() {
        return playerGameMap.get(currentPlayer);
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }
    public void setCurrentWeather(Weather weather) {
        this.currentWeather = weather;
    }

    public void switchTurn() {
        if (players == null || players.isEmpty()) return;

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.resetTurnEnergy();
    }
}
