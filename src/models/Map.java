package models;

import java.util.List;

public class Map {
    private Tile[][] tiles;
    private List<Shop> shops;
    private List<User> players;
    int width;
    int height;

    public Map(Tile[][] tiles, int width, int height , List<Shop> shops, List<User> players) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        this.shops = shops;
        this.players = players;
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
    public List<User> getPlayers() {
        return players;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }


}
