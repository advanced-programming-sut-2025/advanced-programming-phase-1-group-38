package controllers;

import models.Item;
import models.Path;
import models.Result;
import models.Tool;
import models.enums.Weather;

import javax.swing.text.Position;

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

    public Result printMap() {
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

    public Result showPlayerEnergy() {
        return null;
    }
    public Result setPlayerEnergy(int energyAmount) {
        return null;
    }
    public Result setUnlimitedEnergy() {
        return null;
    }

    public Result faint() {
        return null;
    }

    public Result inventoryShow() {
        return null;
    }
    public Result throwItemToTrash(Item item, int number) {
        return null;
    }

    public Result equipTool(String toolName) {
        return null;
    }
    public Result showCurrentTool() {
        return null;
    }
    public Result showAvailableTool() {
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

}
