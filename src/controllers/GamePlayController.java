package controllers;

import models.*;
import models.Animals.Animal;
import models.Animals.AnimalLivingSpace;
import models.Animals.AnimalProduct;
import models.Artisan.*;
import models.Relationship.Friendship;
import models.Tools.*;
import models.enums.*;
import models.enums.Types.*;
import models.farming.*;
import models.inventory.Backpack;
import models.recipe.CookingRecipe;
import models.recipe.CraftingRecipe;

import models.Position;

import java.util.*;
import java.util.stream.Collectors;

public class GamePlayController {
    private final Game game;
    private List<Position> pendingPath;
    private float pendingEnergy;

    public GamePlayController(Game game) {
        this.game = game;
    }

    public Result exitGame() {
        return null;
    }

    public Result saveGame() {
        return null;
    }

    public Result deleteGame() {
        return null;
    }

    public Result nextTurn() {
        return null;
    }

    // Time and Date

    public Result showTime() {
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

    public Result cheatAdvanceHours(String hoursStr) {
        int hours;
        try {
            hours = Integer.parseInt(hoursStr);
            if (hours <= 0) {
                return new Result(false, "Hours must be a positive number.");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number format for hours.");
        }

        game.getTime().advance(hours);
        return new Result(true, "Time advanced by " + hours + " hours.");
    }

    public Result cheatAdvanceDays(String daysStr) {
        int days;
        try {
            days = Integer.parseInt(daysStr);
            if (days <= 0) {
                return new Result(false, "Days must be a positive number.");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number format for days.");
        }

        return cheatAdvanceHours(String.valueOf(days * 24));
    }

    // Season

    public Result showSeason() {
        String season = game.getTime().getCurrentSeason().name();
        season = season.charAt(0) + season.substring(1).toLowerCase();
        return new Result(true, "Current season: " + season);
    }

    // Thor

    public Result cheatThor(String positionStr) {
        String[] parts = positionStr.split(",");
        if (parts.length != 2) {
            return new Result(false, "Invalid position format. Use: x,y");
        }

        int x, y;
        try {
            x = Integer.parseInt(parts[0].trim());
            y = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid numbers in position.");
        }

        Position position = new Position(x, y);
        GameMap map = game.getCurrentPlayerMap();

        Tile tile = map.getTile(position);
        if (tile == null) {
            return new Result(false, "Tile does not exist at " + position);
        }

        if (tile.getTileType() == TileType.GREENHOUSE) {
            return new Result(false, "You can't strike the greenhouse with Thor.");
        }

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

    public Result cheatWeatherSet(String newWeatherStr) {
        if (newWeatherStr == null) {
            return new Result(false, "You must provide a weather type.");
        }

        try {
            Weather weather = Weather.valueOf(newWeatherStr.trim().toUpperCase());
            game.setTomorrowWeather(weather);
            return new Result(true, "Tomorrow's weather has been set to: " + weather);
        } catch (IllegalArgumentException e) {
            return new Result(false, "Invalid weather type. Valid options: SUNNY, RAINY, STORM, SNOW.");
        }
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

    // Print Map

    public Result printMap(String posStr, String sizeStr) {
        String[] p = posStr.split(",");
        int cx, cy, size;
        try {
            cx = Integer.parseInt(p[0].trim());
            cy = Integer.parseInt(p[1].trim());
            size = Integer.parseInt(sizeStr.trim());
            if (size < 1 || size % 2 == 0)
                return new Result(false, "Size must be an odd positive number.");
        } catch (Exception e) {
            return new Result(false, "Invalid – must be `print map -l x,y -s size`");
        }

        GameMap map = game.getCurrentPlayerMap();
        int half = size / 2;
        StringBuilder sb = new StringBuilder();

        for (int dy = -half; dy <= half; dy++) {
            for (int dx = -half; dx <= half; dx++) {
                Position pt = new Position(cx + dx, cy + dy);
                if (!map.isInsideMap(pt)) {
                    sb.append("  ");
                } else {
                    sb.append(getSymbol(map.getTile(pt))).append(" ");
                }
            }
            sb.append("\n");
        }

        return new Result(true, sb.toString());
    }

    public Result showHelpReadingMap() {
        return new Result(true, """
                 👤  Player
                 🟩  Grass (Regular ground)
                 🟫  Plowed ground
                 🟦  Water
                 ⬛️  Quarry
                 🟨  Greenhouse
                 🟥  Home
                 🌳  Tree
                 🌲  Forage tree
                 🔥  Burned tree
                 🌾  Crop
                 🥀  Dead crop
                 💎  Mineral
                 🪨  Stone
                 🪵  Branch
                 🏠 Barn / Big Barn / Deluxe Barn
                 🐔 Coop / Big Coop / Deluxe Coop
                 ⛲ Well
                 📦 Shipping Bin
                 🐄  Cow     🐐  Goat    🐑  Sheep    🐖  Pig
                 🐇  Rabbit  🦆  Duck    🐓  Chicken  🦖  Dinosaur
                """);
    }

    private String getSymbol(Tile tile) {
        Position p = tile.getPosition();
        Player current = game.getCurrentPlayer();

        if (p.equals(current.getPosition())) {
            return "👤";
        }

        for (AnimalLivingSpace space : game.getCurrentPlayerMap().getAnimalBuildings()) {
            for (Animal a : space.getAnimals()) {
                if (a.getPosition().equals(p)) {
                    return switch (a.getAnimalType()) {
                        case COW -> "🐄";
                        case GOAT -> "🐐";
                        case SHEEP -> "🐑";
                        case PIG -> "🐖";
                        case RABBIT -> "🐇";
                        case DUCK -> "🦆";
                        case CHICKEN -> "🐓";
                        case DINOSAUR -> "🦖";
                    };
                }
            }
        }

        Object c = tile.getContent();
        if (c instanceof FarmBuilding fb) {
            return switch (fb.getFarmBuildingType()) {
                case BARN, BIG_BARN, DELUXE_BARN -> "🏠";
                case COOP, BIG_COOP, DELUXE_COOP -> "🐔";
                case WELL -> "⛲";
                case SHIPPING_BIN -> "📦";
            };
        }

        if (c instanceof Crop crop) {
            return crop.isDead() ? "🥀" : "🌾";
        }

        if (c instanceof Branch) return "🪵";
        if (c instanceof ForagingMineral) return "💎";
        if (c instanceof Stone) return "🪨";
        if (c instanceof Tree t) {
            if (t.isBurnt()) return "🔥";
            return t.isForageTree() ? "🌲" : "🌳";
        }

        return switch (tile.getTileType()) {
            case REGULAR_GROUND -> "🟩";
            case PLOWED_GROUND -> "🟫";
            case WATER -> "🟦";
            case QUARRY -> "⬛️";
            case GREENHOUSE -> "🟨";
            case HOME -> "🟥";
            default -> "❓";
        };
    }

    // Walk

    private int countTurns(List<Position> path) {
        if (path.size() < 3) return 0;
        int turns = 0;
        int prevDx = normalize(path.get(1).getX() - path.get(0).getX());
        int prevDy = normalize(path.get(1).getY() - path.get(0).getY());

        for (int i = 2; i < path.size(); i++) {
            Position prev = path.get(i - 1);
            Position cur = path.get(i);
            int dx = normalize(cur.getX() - prev.getX());
            int dy = normalize(cur.getY() - prev.getY());
            if (dx != prevDx || dy != prevDy) turns++;
            prevDx = dx;
            prevDy = dy;
        }
        return turns;
    }

    private int normalize(int delta) {
        return delta > 0 ? 1 : (delta < 0 ? -1 : 0);
    }

    public Result respondForWalkRequest(String posStr) {
        String[] parts = posStr.trim().split(",");
        if (parts.length != 2) {
            return new Result(false, "Invalid position format. Use: x,y");
        }
        int tx, ty;
        try {
            tx = Integer.parseInt(parts[0].trim());
            ty = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return new Result(false, "Coordinates must be numbers.");
        }

        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();
        Position origin = player.getPosition();
        Position goal = new Position(tx, ty);

        if (!map.isInsideMap(goal) || !map.getTile(goal).isWalkable()) {
            return new Result(false, "You can't walk to (" + tx + "," + ty + ").");
        }

        List<Position> path = new PathFinder(map).findPath(origin, goal);
        if (path == null) {
            return new Result(false, "No path found to (" + tx + "," + ty + ").");
        }

        int distance = path.size() - 1;
        int turns = countTurns(path);
        float energy = (distance + 10 * turns) / 20f;

        pendingPath = path;
        pendingEnergy = energy;
        String msg = String.format(
                "Distance: %d  Turns: %d  Energy needed: %.2f\n" +
                        "Type `walk confirm y` to go or `walk confirm n` to cancel",
                distance, turns, energy);
        return new Result(true, msg);
    }

    public Result confirmWalk(String answer) {
        if (pendingPath == null) {
            return new Result(false, "No pending walk. First do `walk x,y`.");
        }
        boolean go = answer.trim().equalsIgnoreCase("y");
        if (!go) {
            pendingPath = null;
            return new Result(true, "Walk cancelled.");
        }

        Player player = game.getCurrentPlayer();
        int distance = pendingPath.size() - 1;
        float perStep = pendingEnergy / distance;

        for (int i = 1; i < pendingPath.size(); i++) {
            Position step = pendingPath.get(i);
            int cost = (int) Math.ceil(perStep);
            player.reduceEnergy(cost);
            player.setPosition(step);

            if (player.getEnergy() <= 0) {
                player.setEnergy(0);
                pendingPath = null;
                return new Result(false, "You fainted at " + step + ".");
            }
        }

        Position arrived = pendingPath.get(pendingPath.size() - 1);
        pendingPath = null;
        return new Result(true,
                "You arrived at " + arrived +
                        ". Energy remaining: " + player.getEnergy());
    }

    // Energy

    public Result showPlayerEnergy() {
        Player player = game.getCurrentPlayer();
        int energy = player.getEnergy();
        return new Result(true, "Current energy: " + energy);
    }

    public Result setPlayerEnergy(String energyAmount) {
        try {
            int value = Integer.parseInt(energyAmount.trim());

            if (value < 0 || value > 200) {
                return new Result(false, "Energy must be between 0 and 200.");
            }

            Player player = game.getCurrentPlayer();
            player.setEnergy(value);
            return new Result(true, "Energy set to " + value);

        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number format.");
        }
    }

    public Result setUnlimitedEnergy() {
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

    public Result throwItemToTrash(String itemName, String numberStr) {
        int quantity;
        try {
            quantity = Integer.parseInt(numberStr);
            if (quantity <= 0) {
                return new Result(false, "The quantity must be a positive number.");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number format: " + numberStr);
        }

        Player player = game.getCurrentPlayer();
        Item item = player.getBackpack().getItemByName(itemName);

        if (item == null) {
            return new Result(false, "Item '" + itemName + "' not found in your inventory.");
        }

        if (!player.getBackpack().hasItem(item, quantity)) {
            return new Result(false, "You don’t have that many of this item.");
        }

        Tool trashCan = player.getCurrentTool();
        if (!(trashCan instanceof TrashCan)) {
            return new Result(false, "You must equip a trash can to discard items.");
        }

        TrashCanQuality quality = ((TrashCan) trashCan).getTrashCanQuality();
        int refundPercent = switch (quality) {
            case INITIAL -> 0;
            case COPPER -> 15;
            case IRON -> 30;
            case GOLD -> 45;
            case IRIDIUM -> 60;
        };

        int refund = (item.getPrice() * refundPercent / 100) * quantity;
        player.getBackpack().removeFromInventory(item, quantity);
        player.addMoney(refund);

        return new Result(true, "You threw away " + quantity + "x " + item.getName()
                + " and got back " + refund + " gold.");
    }

    // Tools

    public Result equipTool(String toolName) {
        Player player = game.getCurrentPlayer();

        // Find the tool by name
        Item item = player.getBackpack().getItemByName(toolName);
        if (item == null || !(item instanceof Tool tool)) {
            return new Result(false, "Tool '" + toolName + "' not found or is not a valid tool.");
        }

        player.setCurrentTool(tool);
        return new Result(true, "Tool '" + tool.toString() + "' has been equipped.");
    }

    public Result showCurrentTool() {
        Player player = game.getCurrentPlayer();
        if (player.getCurrentTool() == null) {
            return new Result(false, "You have no current tool");
        }
        Tool currentTool = player.getCurrentTool();
        return new Result(true, "Your current tool is " + currentTool.toString());
    }

    public Result showAvailableTool() {
        Player player = game.getCurrentPlayer();
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

    public Result toolUpgrade(String toolName) {
        Player player = game.getCurrentPlayer();

        Item item = player.getBackpack().getItemByName(toolName);
        if (item == null || !(item instanceof Tool tool)) {
            return new Result(false, "Tool '" + toolName + "' not found or is not a valid tool.");
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

        Direction direction = Direction.getDirectionByDisplayName(directionString.trim().toLowerCase());
        if (direction == null) {
            return new Result(false, "Invalid direction. Valid options: up, down, left, right, up-left, up-right, down-left, down-right.");
        }

        return tool.useTool(game, direction);
    }

    public Result showCraftInfo(String craftName) {
        return null;
    }

    public Result craftingShowRecipes() {
        return null;
    }

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

    public Result cheatAddItem(String itemName, String countStr) {
        int count;
        try {
            count = Integer.parseInt(countStr);
            if (count <= 0) {
                return new Result(false, "You must add at least 1 item.");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number format for count.");
        }

        Item item = findItemByName(itemName);
        if (item == null) {
            return new Result(false, "No item found with name '" + itemName + "'.");
        }

        Player player = game.getCurrentPlayer();
        player.getBackpack().CheatAddToInventory(item, count);
        return new Result(true, "Added " + count + "x " + item.getName() + " to your inventory.");
    }

    private Item findItemByName(String name) {
        name = name.trim().toLowerCase();

        return switch (name) {
            case "axe" -> new Axe(ToolQuality.INITIAL);
            case "pickaxe" -> new Pickaxe(ToolQuality.INITIAL);
            case "watering can" -> new WateringCan(ToolQuality.INITIAL);
            case "apple" -> new Fruit(FruitType.APPLE);
            case "carrot" -> new Crop(CropType.CARROT);
            case "wood" -> new Wood();
            // Add more cases as needed
            default -> null;
        };
    }

    public Result build(String farmBuildingType, String positionTopLeft) {
        FarmBuildingType type;
        try {
            type = FarmBuildingType.valueOf(farmBuildingType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Result(false, "Invalid building type: " + farmBuildingType);
        }

        Position topLeft;
        try {
            String[] parts = positionTopLeft.trim().split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            topLeft = new Position(x, y);
        } catch (Exception e) {
            return new Result(false, "Invalid position format. Use format like '3,4'.");
        }

        GameMap map = game.getCurrentPlayerMap();

        if (!map.canPlaceBuilding(topLeft, type)) {
            return new Result(false, "Cannot place building here. Area is either occupied or out of bounds.");
        }

        for (int dx = 0; dx < type.getWidth(); dx++) {
            for (int dy = 0; dy < type.getLength(); dy++) {
                Position pos = new Position(topLeft.getX() + dx, topLeft.getY() + dy);
                Tile tile = map.getTile(pos);

                if (tile == null || tile.isOccupied() || tile.getTileType() != TileType.REGULAR_GROUND) {
                    return new Result(false, "You can only build on unoccupied regular ground.");
                }
            }
        }

        FarmBuilding building = new FarmBuilding(type, topLeft);
        map.placeBuilding(topLeft, type, building);

        if (type.getCapacity() > 0) {
            AnimalLivingSpace space = new AnimalLivingSpace(type, topLeft);
            map.addAnimalBuilding(space);
        }

        return new Result(true, "Built: " + type.name());
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

    public Result cheatSetFriendship(String animalName, String amount) {
        Animal animal = getAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "Animal not found.");
        }

        int value;
        try {
            value = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid amount. Please enter a number.");
        }

        value = Math.max(0, Math.min(value, 1000)); // Clamp between 0 and 1000
        animal.setFriendshipLevel(value);

        return new Result(true, "Friendship level of " + animal.getName() + " set to " + value + ".");
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


    public Result cookingShowRecipes() {
        return null;
    }

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

    public Result plant(String seedName, String directionStr) {
        Player player = game.getCurrentPlayer();
        Direction direction = Direction.getDirectionByDisplayName(directionStr);
        if (direction == null) {
            return new Result(false, "Invalid direction.");
        }

        Seed seed = player.getBackpack().getSeedByName(seedName);
        if (seed == null) {
            return new Result(false, "Seed '" + seedName + "' not found in your backpack.");
        }

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

    public Result tryStartArtisan(String machineName, String itemName) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();
        Time time = game.getTime();

        ArtisanMachine machine = findMachine(machineName);
        if (machine == null) {
            return new Result(false, "No machine named \"" + machineName + "\" found.");
        }
        if (machine.isBusy()) {
            return new Result(false, "That machine is busy right now.");
        }

        Item prototype;
        int required;
        if (machine instanceof CharcoalKiln) {
            if (!itemName.equalsIgnoreCase("Wood")) {
                return new Result(false, "Charcoal Kiln only accepts Wood.");
            }
            prototype = new Wood();
            required = 10;
        } else if (machine instanceof Keg) {
            if (!itemName.equalsIgnoreCase("Coffee Bean"))
                return new Result(false, "Keg only accepts Coffee Bean.");
            prototype = new HarvestedCrop(CropType.COFFEE_BEAN);
            required = 5;
        } else if (machine instanceof BeeHouse) {
            prototype = null;
            required = 0;
        } else {
            return new Result(false, "This machine does not accept any items.");
        }

        List<Item> inputs;
        if (required > 0) {
            int have = player.getBackpack().getQuantity(prototype);
            if (have < required) {
                return new Result(false,
                        "You need " + required + "× " + prototype.getName() +
                                " but only have " + have + ".");
            }
            player.getBackpack().removeFromInventory(prototype, required);
            inputs = List.of(prototype);
        } else {
            inputs = List.of();
        }

        boolean started = machine.startProcessing(inputs, time);
        if (!started) {
            if (required > 0) {
                player.getBackpack().addToInventory(prototype, required);
            }
            return new Result(false, "Failed to start processing.");
        }

        return new Result(true,
                "Processing started on your " + machineName +
                        (required > 0
                                ? " using " + required + "× " + prototype.getName()
                                : "") + "!");
    }

    public Result tryCollectArtisan(String machineName) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getCurrentPlayerMap();
        Time time = game.getTime();

        ArtisanMachine machine = findMachine(machineName);
        if (machine == null) {
            return new Result(false, "No machine named \"" + machineName + "\" found.");
        }

        if (!machine.isReady(time)) {
            return new Result(false,
                    "Your " + machineName + " is still working (" +
                            machine.getTimeRemaining(time) + "h left).");
        }

        machine.collectProduct(time);

        ArtisanProductType outType;
        if (machine instanceof CharcoalKiln) {
            outType = ArtisanProductType.COAL;
        } else if (machine instanceof Keg) {
            outType = ArtisanProductType.COFFEE;
        } else if (machine instanceof BeeHouse) {
            outType = ArtisanProductType.HONEY;
        } else {
            return new Result(false, "No known product for " + machineName + ".");
        }
        ArtisanProduct output = new ArtisanProduct(outType);

        if (!player.getBackpack().hasSpaceFor(output, 1)) {
            return new Result(false,
                    "Your inventory is full—cannot collect " + output.getName() + ".");
        }
        player.getBackpack().addToInventory(output, 1);

        return new Result(true,
                "Collected \"" + output.getName() + "\" from your " + machineName + "!");
    }

    private ArtisanMachine findMachine(String machineName) {
        for (ArtisanMachine m : game.getCurrentPlayerMap().getArtisanMachines()) {
            if (m.getClass().getSimpleName().equalsIgnoreCase(machineName)) {
                return m;
            }
        }
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
        return null;
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

    public Result tradeResponse(String respond, int id) {
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

    public Result shepherdAnimal(String animalName, String positionStr) {
        String[] parts = positionStr.split(",");
        if (parts.length != 2) {
            return new Result(false, "Invalid position format. Use x,y.");
        }

        int x, y;
        try {
            x = Integer.parseInt(parts[0].trim());
            y = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid numbers in position.");
        }

        Position position = new Position(x, y);
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
                    return new Result(false, "No product ready for " + animalName + ".");
                }

                if (!player.getBackpack().hasSpaceFor(product, 1)) {
                    return new Result(false, "Not enough space in your backpack to collect the product.");
                }

                player.getBackpack().addToInventory(product, 1);

                return new Result(true,
                        "Collected " + product.getQuality().name().toLowerCase() + " "
                                + product.getProductType().name().toLowerCase().replace("_", " ")
                                + " from " + animalName + ".");
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
        Player player = game.getCurrentPlayer();
        Tool tool = player.getCurrentTool();

        if (tool == null || !(tool instanceof FishingPole)) {
            return new Result(false, "You must equip a fishing pole first.");
        }

        if (!tool.getToolQuality().name().equalsIgnoreCase(fishingPoleName.trim())) {
            return new Result(false, "The equipped pole doesn't match the requested one.");
        }

        Direction fishingDirection = Direction.DOWN;
        return tool.useTool(game, fishingDirection);
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

    public Result cheatAddDollars(String amountStr) {
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                return new Result(false, "You must add a positive amount of dollars.");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number: " + amountStr);
        }

        Player player = game.getCurrentPlayer();
        player.addMoney(amount);

        return new Result(true, "Added " + amount + " gold to your balance.");
    }

    public Result sell(String productName, Integer count) {
        return null;
    }

//    public Result movePlayer(String newPosStr) {
//        String[] parts = newPosStr.split(",");
//        if (parts.length != 2) {
//            return new Result(false, "Invalid position format. Use: x,y");
//        }
//
//        int x, y;
//        try {
//            x = Integer.parseInt(parts[0].trim());
//            y = Integer.parseInt(parts[1].trim());
//        } catch (NumberFormatException e) {
//            return new Result(false, "Invalid number in position.");
//        }
//
//        Position newPos = new Position(x, y);
//        GameMap map = game.getCurrentPlayerMap();
//
//        if (!map.isInsideMap(newPos)) {
//            return new Result(false, "Target position is out of bounds.");
//        }
//
//        Tile tile = map.getTile(newPos);
//        if (tile == null || !tile.isWalkable()) {
//            return new Result(false, "You can't walk there.");
//        }
//
//        Player player = game.getCurrentPlayer();
//        player.setPosition(newPos);
//        return new Result(true, "Player moved to " + newPos);
//    }

//    public boolean canMove(Position position) {
//        GameMap map = game.getCurrentPlayerMap();
//        Tile tile = map.getTile(position);
//        return tile != null && tile.isWalkable();
//    }

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
//    public NPC getNearbyNPC(Player player, GameMap map) {
//        Position p = player.getPosition();
//        for (Position pos : map.getObjectsOnMap().keySet()) {
//            Object obj = map.getObjectAt(pos);
//            if (obj instanceof NPC && p.isNeighbor(pos)) {
//                return (NPC) obj;
//            }
//        }
//        return null;
//    }
//
//    public Result questsList() {
//        Player player = App.getLoggedInUser().getPlayer();
//        Game game = App.getLoggedInUser().getGame();
//        int today = game.getCurrentDay();
//        Map<String, Friendship> friendships = player.getFriendships();
//
//        StringBuilder sb = new StringBuilder();
//        int idx = 1;
//        for (String npcName : friendships.keySet()) {
//            NPC npc = App.getNPCRegistry().getNPCByName(npcName);
//            if (npc == null) continue;
//            Friendship f = friendships.get(npcName);
//            for (Quest q : npc.getQuests()) {
//                boolean available = q.isActive()
//                        && f.getLevel().ordinal() >= q.getUnlockFriendshipLevel()
//                        && today >= q.getUnlockDay();
//                if (available) {
//                    sb.append(idx)
//                            .append(". NPC: ").append(npcName)
//                            .append(" | Quest: ").append(q.getDescription())
//                            .append(" | Requirements: ").append(q.getRequirements())
//                            .append(" | Rewards: ").append(q.getRewards())
//                            .append("\n");
//                }
//                idx++;
//            }
//        }
//        if (sb.length() == 0)
//            return new Result(true, "No active quests found!");
//        return new Result(true, sb.toString());
//    }
//
//    public Result questsFinish(int questIndex) {
//        Player player = App.getLoggedInUser().getPlayer();
//        Game game = App.getLoggedInUser().getGame();
//        int today = game.getCurrentDay();        Map<String, Friendship> friendships = player.getFriendships();
//
//        // جمع‌آوری همه ماموریت‌های فعال با همان logic قدم قبل
//        List<QuestInfo> activeQuests = new ArrayList<>();
//        for (String npcName : friendships.keySet()) {
//            NPC npc = App.getNPCRegistry().getNPCByName(npcName);
//            if (npc == null) continue;
//            Friendship f = friendships.get(npcName);
//            for (Quest q : npc.getQuests()) {
//                boolean available = q.isActive()
//                        && f.getLevel().ordinal() >= q.getUnlockFriendshipLevel()
//                        && today >= q.getUnlockDay();
//                if (available)
//                    activeQuests.add(new QuestInfo(npc, q));
//            }
//        }
//
//        if (questIndex < 1 || questIndex > activeQuests.size())
//            return new Result(false, "Invalid quest index!");
//
//        QuestInfo questInfo = activeQuests.get(questIndex - 1);
//        Quest quest = questInfo.quest;
//        NPC npc = questInfo.npc;
//
//        // چک آیتم‌ها
//        for (String req : quest.getRequirements()) {
//            if (!player.getBackpack().hasItem(req, 1))
//                return new Result(false, "You don't have required item: " + req);
//        }
//        // چک موقعیت مکانی
//        if (!player.getPosition().isNeighbor(npc.getPosition()))
//            return new Result(false, "You must be next to the NPC!");
//
//        // حذف آیتم‌ها
//        for (String req : quest.getRequirements())
//            player.getBackpack().removeFromInventory(req, 1);
//
//        // دادن جوایز (ساده: اضافه به انونتوری یا xp)
//        for (String reward : quest.getRewards())
//            player.getBackpack().addToInventory(reward, 1);
//
//        quest.setActive(false);
//
//        return new Result(true, "Quest completed! Rewards: " + quest.getRewards());
//    }
//
//    private static class QuestInfo {
//        NPC npc;
//        Quest quest;
//        QuestInfo(NPC npc, Quest quest) {
//            this.npc = npc;
//            this.quest = quest;
//        }
//    }
//
//}
