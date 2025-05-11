package controllers;

import models.*;
import models.Animals.Animal;
import models.enums.Types.AnimalType;
import models.enums.Direction;
import models.enums.Types.FarmBuildingType;
import models.enums.Weather;
import models.farming.FertilizerType;
import models.recipe.CookingRecipe;
import models.recipe.CraftingRecipe;

import javax.swing.text.Position;
import java.util.Map;

public class GamePlayController {
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

    public Result showTime(){
        return null;
    }
    public Result showDate(){
        return null;
    }
    public Result showDateTime(){
        return null;
    }
    public Result showDayOfTheWeek(){
        return null;
    }
    public Result cheatAdvanceTime(int howManyHours) {
        return null;
    }
    public Result cheatAdvanceDate(int howManyDays) {
        return null;
    }

    public Result showSeason(){
        return null;
    }
    public Result cheatThor(Position position) {
        return null;
    }
    public Result showWeather() {
        return null;
    }
    public Result showWeatherForecast() {
        return null;
    }
    public Result cheatWeatherSet(Weather newWeather) {
        return null;
    }

    public Result buildGreenhouse() {
        return null;
    }
    private boolean canBuildGreenhouse() {
        return false;
    }

    public Result enterNextDay(){
        return null;
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

    public Result showPlayerEnergy(GameMap gameMap) {
        Player player = gameMap.getCurrentPlayer();
        int energy = player.getEnergy();
        return new Result(true, "Current energy: " + energy);
    }
    public Result setPlayerEnergy(GameMap gameMap, int energyAmount) {
        Player player = gameMap.getCurrentPlayer();
        player.setEnergy(energyAmount);
        return new Result(true, "Energy set to " + energyAmount);
    }
    public Result setUnlimitedEnergy(GameMap gameMap) {
        Player player = gameMap.getCurrentPlayer();
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

    public Result plant(Item seed, Direction direction) {
        return null;
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
