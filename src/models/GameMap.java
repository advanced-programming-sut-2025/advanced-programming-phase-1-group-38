package models;

import java.util.List;

public class GameMap {
    private Tile[][] tiles;
    private List<Shop> shops;
    private List<Player> players;
    int width;
    int height;
    private Player currentPlayer;
    private int currentPlayerIndex = 0;

    public GameMap(Tile[][] tiles, int width, int height , List<Shop> shops, List<Player> players) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        this.shops = shops;
        this.players = players;
        if (!players.isEmpty()) {
            this.currentPlayer = players.get(0);
        }
    }
    public Tile[][] getTiles() {
        return tiles;
    }
    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }
    public List<Shop> getShops() {
        return shops;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
    public void switchTurn() {
        if (players == null || players.isEmpty()) return;

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.resetTurnEnergy();
    }
}
