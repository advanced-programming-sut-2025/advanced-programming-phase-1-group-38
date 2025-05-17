package controllers;

import models.*;
import models.Animals.Animal;
import models.Artisan.ArtisanMachineType;
import models.enums.Seasons;
import models.enums.Types.AnimalType;
import models.enums.Direction;
import models.enums.Types.FarmBuildingType;
import models.enums.Types.ItemType;
import models.enums.Types.TileType;
import models.enums.Weather;
import models.farming.*;
import models.recipe.CookingRecipe;
import models.recipe.CraftingRecipe;

import models.Position;

import java.util.List;
import java.util.Map;

public class GamePlayController {
    private final Game game;
    public GamePlayController(Game game) {
        this.game = game;
    }
    public Result exitGame(){
        return null;
    }
    public Result saveGame(){
        return null;
    }
    public Result deleteGame(){
        return null;
    }

    public Result nextTurn(){
        return null;
    }

    // Time and Date

    public Result showTime(){
        Time time = game.getTime();
        int hour = time.getHourOfDay();
        String formatted = (hour == 0 ? 12 : hour % 12) + (hour < 12 ? " AM" : " PM");
        return new Result(true, "Current time: " + formatted);
    }
    public Result showDate() {
        Time time = game.getTime();
        int day = time.getDayOfSeason();
        String season = time.getCurrentSeason().name().charAt(0) +
            time.getCurrentSeason().name().substring(1).toLowerCase();
        return new Result(true, "Day " + day + " in " + season);
    }

    public Result showDateTime() {
        Time time = game.getTime();

        int hour = time.getHourOfDay();
        String formattedTime = (hour == 0 ? 12 : hour % 12) + (hour < 12 ? " AM" : " PM");

        int day = time.getDayOfSeason();
        String season = time.getCurrentSeason().name().charAt(0) +
            time.getCurrentSeason().name().substring(1).toLowerCase();

        return new Result(true, "Day " + day + " in " + season + " - " + formattedTime);
    }

    public Result showDayOfWeek() {
        Time time = game.getTime();
        return new Result(true, "Today is: " + time.getCurrentDayOfWeek());
    }

    public Result cheatAdvanceHours(int hours) {
        game.getTime().advance(hours);
        return new Result(true, "Time advanced by " + hours + " hours.");
    }

    public Result cheatAdvanceDays(int days) {
        return cheatAdvanceHours(days * 24);
    }

    // Season

    public Result showSeason() {
        String season = game.getTime().getCurrentSeason().name();
        season = season.charAt(0) + season.substring(1).toLowerCase();
        return new Result(true, "Current season: " + season);
    }

    // Thor

    public Result cheatThor(Position position) {
        GameMap map = game.getCurrentPlayerMap();
        Tile tile = map.getTile(position);

        Object content = tile.getContent();
        if (content instanceof Crop crop) {
            crop.kill();
        } else if (content instanceof Tree tree) {
            tree.burn();
        }

        return new Result(true, "Thor's lightning struck at " + position + "!");
    }

    // Weather

    public Result showWeather() {
        return new Result(true, "Today's weather: " + game.getCurrentWeather());
    }
    public Result showWeatherForecast() {
        Weather forecast = game.getTomorrowWeather();
        if (forecast == null) {
            forecast = Weather.getRandom(game.getCurrentSeason());
        }
        return new Result(true, "Tomorrow's weather forecast: " + forecast);
    }
    public Result cheatWeatherSet(Weather newWeather) {
        game.setTomorrowWeather(newWeather);
        return new Result(true, "Tomorrow's weather has been set to: " + newWeather);
    }

    // Greenhouse

    public Result buildGreenhouse() {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();

        if (map.isGreenhouseBuilt()) {
            return new Result(false, "The greenhouse is already built.");
        }

        int requiredWood = 1000;
        int requiredGold = 1000;

        int woodCount = player.getBackpack().getItemCountByType(ItemType.WOOD);
        int gold = player.getMoney();

        if (woodCount < requiredWood || gold < requiredGold) {
            return new Result(false, "You need 1000 wood and 1000 gold to build the greenhouse.");
        }

        player.getBackpack().removeFromInventory(new Wood(), requiredWood);
        player.spendMoney(requiredGold);
        map.setGreenhouseBuilt(true);

        return new Result(true, "Greenhouse built successfully!");
    }

    public Result printMap(GameMap map) {
        return null;
    }
    public Result showHelpReadingMap() {
        return null;
    }

    public Result walk(Path path, boolean playerConfirmed) {
        return null;
    }

    private Path findValidPath(Position origin, Position destination) {
        return new Path();
    }

    private boolean isDestinationAllowed(Position destination) {
        return false;
    }

    public Result respondForWalkRequest(Position origin, Position destination) {
        return null;
    }

    // Energy

    public Result showPlayerEnergy(Game game) {
        Player player = game.getCurrentPlayer();
        int energy = player.getEnergy();
        return new Result(true, "Current energy: " + energy);
    }
    public Result setPlayerEnergy(Game game, int energyAmount) {
        Player player = game.getCurrentPlayer();
        player.setEnergy(energyAmount);
        return new Result(true, "Energy set to " + energyAmount);
    }
    public Result setUnlimitedEnergy(Game game) {
        Player player = game.getCurrentPlayer();
        player.enableUnlimitedEnergy();
        return new Result(true, "Unlimited energy mode enabled.");
    }

    public Result faint(GameMap gameMap) {
        return null;
    }

    public Result inventoryShow() {
        return null;
    }
    public Result throwItemToTrash(Item item, int number) {
        return null;
    }

    public Result equipTool(Tool tool, Player player) {
        if (!(player.getBackpack().containsItem(tool))) {
            return new Result(false, "Tool does not exist");
        }
        player.setCurrentTool(tool);
        return new Result(true, "Tool has been equipped");
    }
    public Result showCurrentTool(Player player) {
        if (player.getCurrentTool() == null) {
            return new Result(false, "You have no current tool");
        }
        Tool currentTool = player.getCurrentTool();
        return new Result(true, "Your current tool is " + currentTool.toString());
    }
    public Result showAvailableTool(Player player) {
        Map<Tool, Integer> tools = player.getBackpack().getTools();

        if (tools.isEmpty()) {
            return new Result(false, "You have no tools");
        }

        StringBuilder message = new StringBuilder("Available Tools: ");
        for (Map.Entry<Tool, Integer> entry : tools.entrySet()) {
            Tool tool = entry.getKey();
            int quantity = entry.getValue();

            message.append("- ")
                    .append(tool.toString())
                    .append(" x")
                    .append(quantity)
                    .append("\n");
        }
        return null;
    }
    public Result toolUpgrade(Tool tool) {
        return null;
    }
    public Result useTool(String directionString) {
        return null;
    }
    private boolean canToolBeUsedHere(Position position, Tool tool) {
        return false;
    }

    public Result showCraftInfo(String craftName) {
        return null;
    }

    public Result craftingShowRecipes() { return null; }
    public Result craft(CraftingRecipe item) {
        return null;
    }
    private boolean canCraft(CraftingRecipe item) {
        return false;
    }

    public Result placeItem(Item item, Direction direction) {
        return null;
    }
    private Position neighborTile(Direction direction) {
        return null;
    }
    private Tile getTileByPosition(Position position) {
        return null;
    }
    private boolean canItemBePlacedHere(Position position, Item item) {
        return false;
    }
    public Result cheatAddItem(Item item, int count) {
        return null;
    }

    public Result build(FarmBuildingType farmBuildingType, models.Position position) {
        return new Result(true, "");
    }

    public Result buyAnimal(AnimalType animalType, String name) {
        Animal animal = new Animal(name, animalType);
        return null;
    }

    public Result pet(String animalName) {
        Animal animal = getAnimalByName(animalName);
        return null;
    }

    public Result cheatSetFriendship(String animalName, int amount) {
        Animal animal = getAnimalByName(animalName);
        return null;
    }

    public Result cookingShowRecipes() { return null; }
    public Result cookingPrepare(Item cookingRecipe) {
        return null;
    }
    private boolean canCook(CookingRecipe cookingRecipe) {
        return false;
    }
    public Result eat(Item food) {
        return null;
    }

    public Result plant(Seed seed, Direction direction) {
        Player player = game.getCurrentPlayer();
        Position target = player.getPosition().shift(direction);
        Result canPlantResult = canPlant(target);

        if (!player.getBackpack().hasItem(seed, 1)) {
            return new Result(false, "You don't have that seed.");
        }

        if (!canPlantResult.success()) {
            return canPlantResult;
        }

        Tile tile = game.getCurrentPlayerMap().getTile(target);
        Seasons currentSeason = game.getCurrentSeason();

        if (seed.isCropSeed()) {
            CropType cropType = seed.getCropType();
            if (!cropType.growsIn(currentSeason)) {
                return new Result(false, cropType.getName() + " can't be planted in " + currentSeason.name().toLowerCase() + ".");
            }

            tile.setContent(new Crop(cropType));
        }
        else if (seed.isTreeSeed()) {
            TreeType treeType = seed.getTreeType();
            if (treeType.producesFruit() && !treeType.getSeasons().contains(currentSeason)) {
                return new Result(false, treeType.getName() + " can't grow fruit in " + currentSeason.name().toLowerCase() + ".");
            }

            tile.setContent(new Tree(treeType));
        } else {
            return new Result(false, "Invalid seed type.");
        }

        player.getBackpack().removeFromInventory(seed, 1);

        return new Result(true, "Planted " + seed.getName() + " at " + target);
    }
    public Result canPlant(Position position) {
        if (!game.getCurrentPlayerMap().isInsideMap(position)) {
            return new Result(false, "Position is out of bounds.");
        }

        Player player = game.getCurrentPlayer();
        Position playerPos = player.getPosition();

        int dx = Math.abs(playerPos.getX() - position.getX());
        int dy = Math.abs(playerPos.getY() - position.getY());

        if (dx > 1 || dy > 1 || (dx == 0 && dy == 0)) {
            return new Result(false, "You can only plant in the 8 tiles around you.");
        }

        Tile tile = game.getCurrentPlayerMap().getTile(position);

        if (tile.isOccupied()) {
            return new Result(false, "This tile already has something planted.");
        }

        if (tile.getTileType() != TileType.PLOWED_GROUND) {
            return new Result(false, "You can only plant on plowed ground.");
        }

        return new Result(true, "You can plant here.");
    }
    public Result showPlant(Position position) {
        return null;
    }
    public Result fertilize(FertilizerType fertilizer, Direction direction) {
        return null;
    }
    public Result howMuchWater() {
        return null;
    }
    public Result resetCropWatering() { return null; }

    public Result artisanUse(String artisanName, String itemName) {
        return null;
    }

//    public String tryStartProcessing(String machine, List<Item> inputItems, Time time) {
//        // 1. Null or empty input check
//        ArtisanMachineType machineType = ArtisanMachineType.valueOf(machine);
//        if (inputItems == null || inputItems.isEmpty()) {
//            return "You must add an item to process.";
//        }
//
//        // 2. Machine already busy
//        if (machine.isBusy()) {
//            return "The machine is currently processing. Please wait.";
//        }
//
//        // 3. Try to match a recipe
//        ArtisanRecipe recipe = machine.findMatchingRecipe(inputItems);
//        if (recipe == null) {
//            return "These items don't match any recipe.";
//        }
//
//        // 4. Is it complete? (You can customize this per recipe if needed)
//        RecipeOption matched = recipe.getMatchingOption(inputItems);
//        if (matched == null || !matched.getIngredient().matches(inputItems)) {
//            return "You don't have all required ingredients.";
//        }
//
//        // 5. All good — start processing
//        boolean started = machine.startProcessing(inputItems, time);
//        if (!started) {
//            return "Failed to start processing for unknown reason.";
//        }
//
//        return "Processing started: " + recipe.getName();
//    }

    public Result artisanGet(String artisanName) {
        return null;
    }

    public Result friendships() {
        return null;
    }
    public Result talk(String username, String message) {
        return null;
    }
    public Result talkHistoryWithUser(String username) {
        return null;
    }
    public Result giveGift(String username, String itemName, int amount) {
        return null;
    }
    public Result giftList() {
        return null;
    }
    public Result giftRate(int giftNumber, int rate) {
        return null;
    }
    public Result giveHistory(String username) {
        return null;
    }
    public Result hug(String username) {
        return null;
    }
    public Result giveFlowerToUser(String username) {
        return null;
    }
    public Result askMarriage(String username, Item ring) {
        return null;
    }
    public Result respondToMarriageRequest(String respond, String username) {
        return  null;
    }

    public Result startTrade() {
        return null;
    }
    public Result tradeWithMoney(String targetUsername, String type, String itemName, int amount, int price) {
        return null;
    }
    public Result tradeWithItem(String targetUsername, String type, String itemName, int amount, String targetItemName, int targetAmount) {
        return null;
    }
    public Result showTradeList(String targetUsername, String type, String itemName, int amount, int price) {
        return null;
    }
    public Result tradeResponse(String respond,int id) {
        return null;
    }
    public Result showTradeHistory() {
        return null;
    }

    public Result showMyAnimalsInfo() {
        return null;
    }

    public Result shepherdAnimal(String animalName, models.Position position) {
        return null;
    }

    public Result feedHayToAnimal(String animalName) {
        return null;
    }

    public Result showProducedProducts() {
        return null;
    }
    public Result collectProducts(String animalName) {
        return null;
    }

    public Result sellAnimal(String animalName) {
        return null;
    }
    private Animal getAnimalByName(String name) {
        return null;
    }
    public Result fishing(String fishingPoleName) {
        return null;
    }

    public int numberOfCaughtFish() {
        return 0;
    }
    public int qualityOfCaughtFish() {
        return 0;
    }
    private Tool getFishingPoleByName(String name) {
        return null;
    }

    public Result showAllProducts() {
        return null;
    }
    public Result showAvailableProducts() {
        return null;
    }
    public Result purchase(String productName, Integer count) {
        return null;
    }
    public Result cheatAddDollars(int amount) {
        return null;
    }
    public Result sell(String productName, Integer count) {
        return null;
    }

    public void movePlayer(Position position) {
    }
    public boolean canMove(Position position) {
        return false;
    }

    public Result meetNPC(String NCPName) {
        return null;
    }
    public Result giftNPC(String NCPName, String itemName) {
        return null;
    }
    public Result showFriendshipNPCList() {
        return null;
    }
    public Result showQuestsList() {
        return null;
    }
    public Result finishQuest(int index) {
        return null;
    }
    private NPC geNPCByName(String NPCName) {
        return null;
    }
}
