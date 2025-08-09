//package io.github.StardewValley.models.Tools;
//
//import io.github.StardewValley.models.*;
//import io.github.StardewValley.models.enums.Direction;
//import io.github.StardewValley.models.enums.Skill;
//import io.github.StardewValley.models.enums.SkillLevel;
//import io.github.StardewValley.models.enums.ToolQuality;
//import io.github.StardewValley.models.enums.Types.TileType;
//
//public class Hoe extends Tool {
//    public Hoe(ToolQuality quality) {
//        super(ToolType.HOE, quality, Skill.FARMING);
//    }
//
//    @Override
//    public int getEnergyCost(Player player) {
//        int baseCost = getToolQuality().getEnergyCost();
//        if (player.getSkillLevel(Skill.FARMING) == SkillLevel.LEVEL_FOUR) {
//            baseCost--;
//        }
//        return Math.max(baseCost, 0);
//    }
//
//    @Override
//    public Result useTool(Game game, Direction direction) {
//        Player player = game.getCurrentPlayer();
//        Position target = player.getPosition().shift(direction);
//        GameMap currentGameMap = game.getCurrentPlayerMap();
//        Tile[][] tiles = currentGameMap.getTiles();
//
//        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());
//
//        if (player.getEnergy() < cost) {
//            return new Result(false, "You don't have enough energy to use hoe.");
//        }
//
//        if (player.getEnergyUsedThisTurn() + cost > 50) {
//            return new Result(false, "Not enough energy for this turn.");
//        }
//
//        if (target.getX() < 0 || target.getY() < 0 ||
//            target.getX() >= currentGameMap.getWidth() ||
//            target.getY() >= currentGameMap.getHeight()) {
//            return new Result(false, "Target tile is out of bounds.");
//        }
//
//        Tile tile = tiles[target.getY()][target.getX()];
//
//        boolean isTillable = tile.getTileType() == TileType.REGULAR_GROUND;
//        boolean isOccupied = tile.isOccupied();
//
//        player.reduceEnergy(cost);
//        player.addEnergyUsed(cost);
//
//        if (isTillable && !isOccupied) {
//            tile.setTileType(TileType.PLOWED_GROUND);
//            return new Result(true, "Soil tilled at " + target);
//        } else {
//            return new Result(false, "Cannot till this tile.");
//        }
//    }
//
//    @Override
//    public String toString() {
//        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
//    }
//}
