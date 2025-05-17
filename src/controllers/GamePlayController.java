package controllers;

import models.*;
import models.Animals.Animal;
import models.Animals.AnimalLivingSpace;
import models.Animals.AnimalProduct;
import models.Artisan.ArtisanMachineType;
import models.Tools.*;
import models.enums.*;
import models.enums.Types.*;
import models.farming.*;
import models.inventory.Backpack;
import models.recipe.CookingRecipe;
import models.recipe.CraftingRecipe;

import models.Position;

import java.util.List;
import java.util.Map;

import static models.enums.Types.ToolType.*;

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

    public Result inventoryShow() {
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getBackpack();

        if (backpack == null || backpack.getItems().isEmpty()) {
            return new Result(true, "Your inventory is empty.");
        }

        StringBuilder sb = new StringBuilder("Inventory contents:\n");

        for (Map.Entry<Item, Integer> entry : backpack.getItems().entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            sb.append("- ").append(item.getName()).append(": ").append(quantity).append("\n");
        }

        return new Result(true, sb.toString().trim());
    }

    public Result throwItemToTrash(Item item, int number) {
        Player player = game.getCurrentPlayer();

        if (!player.getBackpack().hasItem(item, number)) {
            return new Result(false, "You don’t have that many of this item.");
        }

        player.getBackpack().removeFromInventory(item, number);

        Tool trashCan = player.getCurrentTool();
        if (!(trashCan instanceof TrashCan)) {
            return new Result(false, "You must equip a trash can to discard items.");
        }

        TrashCanQuality quality = ((TrashCan) trashCan).getTrashCanQuality();
        int percentage = switch (quality) {
            case INITIAL -> 0;
            case COPPER -> 15;
            case IRON -> 30;
            case GOLD -> 45;
            case IRIDIUM -> 60;
        };

        int refund = (item.getPrice() * percentage / 100) * number;
        player.addMoney(refund);

        return new Result(true, "You threw away " + number + "x " + item.getName() +
            " and got back " + refund + " gold.");
    }

    // Tools

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

        StringBuilder message = new StringBuilder("Available Tools:\n");
        for (Map.Entry<Tool, Integer> entry : tools.entrySet()) {
            Tool tool = entry.getKey();
            int quantity = entry.getValue();

            message.append("- ")
                .append(tool.toString())
                .append(" x")
                .append(quantity)
                .append("\n");
        }

        return new Result(true, message.toString().trim());
    }

    public Result toolUpgrade(Tool tool) {
        Player player = game.getCurrentPlayer();

        if (!player.getBackpack().containsItem(tool)) {
            return new Result(false, "You don't have this tool in your backpack.");
        }

        ToolType type = tool.getToolType();
        if (type == ToolType.MILK_PAIL || type == ToolType.SHEAR || type == ToolType.SCYTHE) {
            return new Result(false, "This tool cannot be upgraded.");
        }

        ToolQuality current = tool.getToolQuality();
        if (current == null) {
            return new Result(false, "This tool has no quality level defined.");
        }

        ToolQuality next = current.getNext();
        if (next == null) {
            return new Result(false, "This tool is already at maximum quality.");
        }

        player.getBackpack().removeFromInventory(tool, 1);

        Tool upgradedTool = createUpgradedTool(tool, next);
        player.getBackpack().addToInventory(upgradedTool, 1);

        return new Result(true, "Tool upgraded to " + next.name() + " quality.");
    }



    private Tool createUpgradedTool(Tool oldTool, ToolQuality newQuality) {
        return switch (oldTool.getToolType()) {
            case AXE -> new Axe(newQuality);
            case HOE -> new Hoe(newQuality);
            case PICKAXE -> new Pickaxe(newQuality);
            case WATERING_CAN -> new WateringCan(newQuality);
            default -> throw new IllegalArgumentException("Unsupported tool type");
        };
    }

    public Result useTool(String directionString) {
        Player player = game.getCurrentPlayer();
        Tool tool = player.getCurrentTool();

        if (tool == null) {
            return new Result(false, "You have no tool equipped.");
        }

        Direction direction = Direction.getDirectionByDisplayName(directionString);
        if (direction == null) {
            return new Result(false, "Invalid direction. Try up, down, left, right, or diagonals.");
        }

        return tool.useTool(game, direction);
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
        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();

        AnimalLivingSpace space = map.getAvailableLivingSpace(animalType.getLivingSpaceTypes());
        if (space == null) {
            return new Result(false, "No valid living space available for " + animalType.name() + ".");
        }

        if (space.getAnimalByName(name) != null) {
            return new Result(false, "You already have an animal named " + name + ".");
        }

        if (player.getMoney() < animalType.getPrice()) {
            return new Result(false, "You don't have enough money to buy a " + animalType.name() + ".");
        }

        Animal animal = new Animal(name, animalType);
        space.addAnimal(animal);
        player.spendMoney(animalType.getPrice());

        return new Result(true, "You bought a " + animalType.name() + " named " + name + ".");
    }

    public Result pet(String animalName) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();

        Animal animal = null;
        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            animal = space.getAnimalByName(animalName);
            if (animal != null) break;
        }

        if (animal == null) {
            return new Result(false, "No animal named " + animalName + " found.");
        }

        if (animal.isPetToday()) {
            return new Result(false, animal.getName() + " has already been pet today.");
        }

        animal.pet();

        return new Result(true, "You pet " + animal.getName() + ". Friendship level is now " + animal.getFriendshipLevel() + ".");
    }
    public Result cheatSetFriendship(String animalName, int amount) {
        Animal animal = getAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "Animal not found.");
        }

        amount = Math.max(0, Math.min(amount, 1000));
        animal.setFriendshipLevel(amount);

        return new Result(true, "Friendship level of " + animal.getName() + " set to " + amount + ".");
    }

    private Animal getAnimalByName(String name) {
        for (AnimalLivingSpace space : game.getCurrentPlayerMap().getAnimalBuildings()) {
            for (Animal animal : space.getAnimals()) {
                if (animal.getName().equalsIgnoreCase(name)) {
                    return animal;
                }
            }
        }
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

    // Farming

    public Result plant(Seed seed, Direction direction) {
        Player player = game.getCurrentPlayer();
        Position target = player.getPosition().shift(direction);
        Tile tile = game.getCurrentPlayerMap().getTile(target);

        Result canPlantResult = canPlant(target);
        if (!canPlantResult.success()) {
            return canPlantResult;
        }

        if (!player.getBackpack().hasItem(seed, 1)) {
            return new Result(false, "You don't have that seed.");
        }

        Seasons currentSeason = game.getCurrentSeason();
        boolean isGreenhouse = tile.getTileType() == TileType.GREENHOUSE;

        if (seed.isCropSeed()) {
            CropType cropType = seed.getCropType();
            if (!isGreenhouse && !cropType.growsIn(currentSeason)) {
                return new Result(false, cropType.getName() + " can't be planted in " + currentSeason.name().toLowerCase() + ".");
            }
            tile.setContent(new Crop(cropType));
        } else if (seed.isTreeSeed()) {
            TreeType treeType = seed.getTreeType();
            if (!isGreenhouse && treeType.producesFruit() && !treeType.getSeasons().contains(currentSeason)) {
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

        if (tile.getTileType() == TileType.GREENHOUSE) {
            return new Result(true, "You can plant here.");
        }

        if (tile.getTileType() != TileType.PLOWED_GROUND) {
            return new Result(false, "You can only plant on plowed ground.");
        }

        return new Result(true, "You can plant here.");
    }

    public Result showPlant(String plantName) {
        plantName = plantName.trim().toLowerCase();

        for (CropType crop : CropType.values()) {
            if (crop.getName().toLowerCase().equals(plantName)) {
                String name = crop.getName();
                String source = (crop.getSource() != null) ? crop.getSource().name() : "Unknown";
                int totalTime = crop.getGrowthStages().stream().mapToInt(Integer::intValue).sum();

                return new Result(true,
                    "Name: " + name + "\n" +
                        "Source: " + source + "\n" +
                        "Total Growth Time: " + totalTime);
            }
        }

        for (TreeType tree : TreeType.values()) {
            if (tree.getName().toLowerCase().equals(plantName)) {
                String name = tree.getName();
                String source = (tree.getSource() != null) ? tree.getSource().toString() : "Unknown";
                int totalTime = tree.getStages().stream().mapToInt(Integer::intValue).sum();

                return new Result(true,
                    "Name: " + name + "\n" +
                        "Source: " + source + "\n" +
                        "Total Growth Time: " + totalTime);
            }
        }

        return new Result(false, "No crop or tree found with the name: " + plantName);
    }

    public Result fertilize(FertilizerType fertilizer, Direction direction) {
        return null;
    }

    public Result howMuchWater() {
        Player player = game.getCurrentPlayer();
        Tool currentTool = player.getCurrentTool();

        if (!(currentTool instanceof WateringCan wateringCan)) {
            return new Result(false, "You must equip a watering can to check water level.");
        }

        int waterLeft = wateringCan.getCurrentWaterAmount();
        int capacity = wateringCan.getCapacity();

        return new Result(true, "Water remaining: " + waterLeft + " units" +
            (capacity > 0 ? " / " + capacity + " max." : ""));
    }

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
        GameMap map = game.getCurrentPlayerMap();
        StringBuilder message = new StringBuilder("Your animals:\n");

        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            for (Animal animal : space.getAnimals()) {
                message.append("- ").append(animal.getName()).append(" (")
                        .append(animal.getAnimalType().name()).append(") | ")
                        .append("Friendship: ").append(animal.getFriendshipLevel()).append(" | ")
                        .append("Fed: ").append(animal.isFedToday() ? "Yes" : "No").append(" | ")
                        .append("Pet: ").append(animal.isPetToday() ? "Yes" : "No").append("\n");
            }
        }
        return new Result(true, message.toString());
    }

    public Result shepherdAnimal(String animalName, Position position) {
        GameMap map = game.getCurrentPlayerMap();
        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            Animal animal = space.getAnimalByName(animalName);
            if (animal != null) {
                animal.setPosition(position);
                animal.setOutside(true);
                return new Result(true, animalName + " was taken outside to " + position);
            }
        }
        return new Result(false, "Animal not found.");
    }

    public Result feedHayToAnimal(String animalName) {
        GameMap map = game.getCurrentPlayerMap();
        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            Animal animal = space.getAnimalByName(animalName);
            if (animal != null) {
                animal.feed();
                return new Result(true, animalName + " has been fed with hay.");
            }
        }
        return new Result(false, "Animal not found.");
    }

    public Result showProducedProducts() {
        GameMap map = game.getCurrentPlayerMap();
        StringBuilder message = new StringBuilder("Produced Products:\n");
        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            for (Animal animal : space.getAnimals()) {
                if (animal.isProductReady()) {
                    message.append("- ").append(animal.getName()).append(" has a product ready!\n");
                }
            }
        }
        return new Result(true, message.toString());
    }

    public Result collectProducts(String animalName) {
        GameMap map = game.getCurrentPlayerMap();
        Player player = game.getCurrentPlayer();
        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            Animal animal = space.getAnimalByName(animalName);
            if (animal != null) {
                AnimalProduct product = animal.collectProduct();
                if (product == null) {
                    return new Result(false, "No product ready for " + animalName);
                }
                player.getBackpack().addToInventory(product, 1);
                return new Result(true, "Collected product from " + animalName);
            }
        }
        return new Result(false, "Animal not found.");
    }

    public Result sellAnimal(String animalName) {
        GameMap map = game.getCurrentPlayerMap();
        Player player = game.getCurrentPlayer();
        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
            Animal animal = space.getAnimalByName(animalName);
            if (animal != null) {
                int price = animal.getSellPrice();
                player.addMoney(price);
                space.removeAnimal(animal);
                return new Result(true, animalName + " sold for " + price + " coins.");
            }
        }
        return new Result(false, "Animal not found.");
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

    public Result movePlayer(Position newPos) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();

        if (!map.isInsideMap(newPos)) {
            return new Result(false, "Target position is out of bounds.");
        }

        Tile tile = map.getTile(newPos);
        if (tile == null || !tile.isWalkable()) {
            return new Result(false, "You can't walk there.");
        }

        player.setPosition(newPos);
        return new Result(true, "Player moved to " + newPos);
    }

    public boolean canMove(Position position) {
        GameMap map = game.getCurrentPlayerMap();
        Tile tile = map.getTile(position);
        return tile != null && tile.isWalkable();
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
