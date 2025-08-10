//package io.github.StardewValley.models;
//
//import io.github.StardewValley.models.enums.Seasons;
//import io.github.StardewValley.models.enums.Types.TileType;
//import io.github.StardewValley.models.enums.Weather;
//import io.github.StardewValley.models.farming.Branch;
//import io.github.StardewValley.models.farming.Crop;
//import io.github.StardewValley.models.farming.Tree;
//
//import java.util.*;
//
//public class Game {
//    private ArrayList<Player> players;
//    private Player currentPlayer;
//    private List<Shop> shops;
//    private int currentPlayerIndex = 0;
//    private Weather currentWeather;
//    private Weather tomorrowWeather;
//    private Seasons currentSeason;
//    private List<GameMap> gameMaps;
//    private Map<Player, GameMap> playerGameMap;
//    private Time time = new Time();
//    private List<Position> lastThorHits = new ArrayList<>();
//    private final Random rng = new Random();
//    private final List<TradeRequest> pendingTrades = new ArrayList<>();
//    private final List<TradeRequest> completedTrades = new ArrayList<>();
//    private final ArrayList<NPC> npcs;
//    private final ArrayList<NPCHome> npcsHome;
//    private final ArrayList<NPCFriendship> npcFriendships;
//    private final List<Item> shippingBin = new ArrayList<>();
//
//
//    public Game(List<Shop> shops, ArrayList<Player> players, Weather startingWeather, Seasons currentSeason, List<GameMap> gameMaps) {
//        this.shops = shops;
//        this.players = players;
//        if (!players.isEmpty()) {
//            this.currentPlayer = players.get(0);
//        }
//        this.currentWeather = startingWeather;
//        this.currentSeason = currentSeason;
//        this.gameMaps = gameMaps;
//        this.playerGameMap = new HashMap<>();
//        for (int i = 0; i < players.size(); i++) {
//            playerGameMap.put(players.get(i), gameMaps.get(i % gameMaps.size()));
//        }
//        this.npcs = new ArrayList<>();
//
//        this.npcsHome = new ArrayList<>();
//
//        this.npcFriendships = new ArrayList<>();
//        for (NPC npc : this.npcs) {
//            for (Player p : this.players) {
//                npcFriendships.add(new NPCFriendship(npc, p));
//            }
//        }
//    }
//
//    public List<Shop> getShops() {
//        return shops;
//    }
//
//    public List<Player> getPlayers() {
//        return players;
//    }
//
//    public Player getCurrentPlayer() {
//        return currentPlayer;
//    }
//
//    public void setCurrentPlayer(Player player) {
//        this.currentPlayer = player;
//    }
//    public GameMap getCurrentPlayerMap() {
//        return playerGameMap.get(currentPlayer);
//    }
//
//    public Weather getCurrentWeather() {
//        return currentWeather;
//    }
//    public void setCurrentWeather(Weather weather) {
//        this.currentWeather = weather;
//    }
//
//    public Weather getTomorrowWeather() {
//        return tomorrowWeather;
//    }
//
//    public void setTomorrowWeather(Weather weather) {
//        this.tomorrowWeather = weather;
//    }
//
//    public Seasons getCurrentSeason() {
//        return currentSeason;
//    }
//
//    public void setCurrentSeason(Seasons currentSeason) {
//        this.currentSeason = currentSeason;
//    }
//
//    public List<TradeRequest> getPendingTrades() {
//        return Collections.unmodifiableList(pendingTrades);
//    }
//
//    public List<TradeRequest> getCompletedTrades() {
//        return Collections.unmodifiableList(completedTrades);
//    }
//
//    public void addTrade(TradeRequest t) {
//        pendingTrades.add(t);
//    }
//    public void removeTrade(TradeRequest t) {
//        pendingTrades.remove(t);
//    }
//
//    public void recordCompletedTrade(TradeRequest t) {
//        completedTrades.add(t);
//    }
//
//    public List<NPC> getNpcs() { return Collections.unmodifiableList(npcs); }
//    public List<NPCHome> getNpcHomes() {
//        return Collections.unmodifiableList(npcsHome);
//    }
//
//    public List<NPCFriendship> getNpcFriendships() {
//        return Collections.unmodifiableList(npcFriendships);
//    }
//
//    public Time getTime() {
//        return time;
//    }
//
//    public List<Position> applyStormLightning() {
//        List<Position> thorHits = new ArrayList<>();
//
//        for (GameMap map : gameMaps) {
//            List<Position> allCandidates = new ArrayList<>();
//            List<Position> emptyGround = new ArrayList<>();
//
//            for (int y = 0; y < map.getHeight(); y++) {
//                for (int x = 0; x < map.getWidth(); x++) {
//                    Position pos = new Position(x, y);
//                    Tile tile = map.getTile(pos);
//
//                    if (tile.getTileType() == TileType.GREENHOUSE) continue;
//
//                    Object content = tile.getContent();
//
//                    allCandidates.add(pos);
//
//                    if (content == null) {
//                        emptyGround.add(pos);
//                    }
//                }
//            }
//
//            Collections.shuffle(allCandidates);
//            List<Position> struck = allCandidates.subList(0, Math.min(3, allCandidates.size()));
//
//            for (Position pos : struck) {
//                Tile tile = map.getTile(pos);
//                Object content = tile.getContent();
//
//                if (content instanceof Crop crop) {
//                    crop.kill();
//                } else if (content instanceof Tree tree) {
//                    tree.burn();
//                }
//
//                thorHits.add(pos);
//            }
//
//            Collections.shuffle(emptyGround);
//            for (int i = 0; i < Math.min(10, emptyGround.size()); i++) {
//                Position branchPos = emptyGround.get(i);
//                Tile tile = map.getTile(branchPos);
//                if (!tile.isOccupied()) {
//                    tile.setContent(new Branch());
//                }
//            }
//        }
//
//        return thorHits;
//    }
//
//    public void nextTurn() {
//        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
//    }
//
//
//
//    public void switchTurn() {
//        if (players == null || players.isEmpty()) return;
//
//        int startingIndex = currentPlayerIndex;
//
//        do {
//            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
//            currentPlayer = players.get(currentPlayerIndex);
//        } while (currentPlayer.isFainted() && currentPlayerIndex != startingIndex);
//
//        if (!currentPlayer.isFainted()) {
//            currentPlayer.resetTurnEnergy();
//        }
//
//        if (currentPlayerIndex == 0) {
//            time.advance(1);
//
//            if (time.isBedTime()) {
//
//                for (Player p : players) {
//                    GameMap map = playerGameMap.get(p);
//                    Position homeTL = map.getHomeTopLeft();
//                    int hw = map.getHomeWidth(), hh = map.getHomeHeight();
//                    Position homePos = new Position(
//                        homeTL.getX() + hw/2,
//                        homeTL.getY() + hh/2
//                    );
//                    p.setPosition(homePos);
//                }
//
//                if (currentWeather == Weather.STORM) {
//                    lastThorHits = applyStormLightning();
//                } else {
//                    lastThorHits.clear();
//                }
//
//                time.skipToMorning();
//                enterNextDay();
//            }
//        }
//    }
//
//
//
//    public void assignMapToPlayer(Player player, GameMap map) {
//        this.playerGameMap.put(player, map);
//    }
//
//    public List<GameMap> getGameMaps() {
//        return gameMaps;
//    }
//
//    public void enterNextDay() {
//        int today = time.getDayOfYear();
//
//        for (Player p : players) {
//            for (PeopleFriendship f : p.getAllFriendships()) {
//                f.dailyDecay(today);
//            }
//        }
//
//        this.currentSeason = time.getCurrentSeason();
//
//        if (tomorrowWeather != null) {
//            this.currentWeather = tomorrowWeather;
//            tomorrowWeather = null;
//        } else {
//            this.currentWeather = Weather.getRandom(currentSeason);
//        }
//
//        List<Position> cropPositions = new ArrayList<>();
//        for (GameMap map : gameMaps) {
//            for (int y = 0; y < map.getHeight(); y++) {
//                for (int x = 0; x < map.getWidth(); x++) {
//                    Tile tile = map.getTile(new Position(x, y));
//                    Object content = tile.getContent();
//                    if (content instanceof Crop crop) {
//                        cropPositions.add(new Position(x, y));
//                    }
//                }
//            }
//        }
//        if (cropPositions.size() >= 16 && rng.nextDouble() < 0.25) {
//            int toDestroy = Math.max(1, cropPositions.size() / 4);
//            Collections.shuffle(cropPositions, rng);
//
//            for (int i = 0; i < toDestroy; i++) {
//                Position p = cropPositions.get(i);
//                GameMap map = getCurrentPlayerMap();
//                Tile tile  = map.getTile(p);
//                Crop crop  = (Crop) tile.getContent();
//                crop.kill();
//            }
//        }
//
//        for (GameMap map : gameMaps) {
//            Tile[][] tiles = map.getTiles();
//
//            for (int y = 0; y < map.getHeight(); y++) {
//                for (int x = 0; x < map.getWidth(); x++) {
//                    Tile tile = tiles[y][x];
//                    Object content = tile.getContent();
//
//                    if (content instanceof Crop crop) {
//                        boolean isGreenhouse = tile.getTileType() == TileType.GREENHOUSE;
//                        boolean isRainyOrStorm = currentWeather == Weather.RAINY || currentWeather == Weather.STORM;
//
//                        if (!isGreenhouse && isRainyOrStorm) {
//                            crop.setWatered(true);
//                        }
//
//                        if (!crop.getCropType().growsIn(currentSeason) &&
//                            tile.getTileType() != TileType.GREENHOUSE) {
//                            crop.kill();
//                        } else if (!crop.isDead()) {
//                            crop.cropNextDay();
//                        }
//                    }
//
//                    if (content instanceof Tree tree) {
//                        boolean isGreenhouse = tile.getTileType() == TileType.GREENHOUSE;
//                        tree.treeNextDay(currentSeason, isGreenhouse);
//                    }
//                }
//            }
//        }
//
//        for (Player player : players) {
//            if (player.isFainted()) {
//                player.setEnergy(150);
//                player.setFainted(false);
//            } else {
//                player.resetEnergy();
//            }
//            player.resetTurnEnergy();
//        }
//    }
//    public boolean isNearShippingBin(Position position) {
//        // TODO: ShippingBin class and fix position
//        Position binPosition = new Position(0, 0);
//        int dx = Math.abs(position.getX() - binPosition.getX());
//        int dy = Math.abs(position.getY() - binPosition.getY());
//        return dx <= 1 && dy <= 1;
//    }
//    public List<Item> getShippingBin() {
//        return shippingBin;
//    }
//
//}
