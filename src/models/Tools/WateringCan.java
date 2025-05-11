package models.Tools;

import models.*;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;
import models.farming.Crop;

public class WateringCan extends Tool {
    private int capacity;
    private int waterLeft;
    public WateringCan(ToolQuality quality) {
        super(ToolType.WATERING_CAN, quality, Skill.FARMING);
        this.capacity = switch (quality) {
            case INITIAL -> 40;
            case COPPER -> 55;
            case IRON -> 70;
            case GOLD -> 85;
            case IRIDIUM -> 100;
        };
        this.waterLeft = capacity;
    }

    @Override
    public int getEnergyCost(Player player) {
        int baseCost = getToolQuality().getEnergyCost();
        if (player.getSkillLevel(Skill.FARMING) == SkillLevel.LEVEL_FOUR) {
            baseCost--;
        }
        return Math.max(baseCost, 0);
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        Player player = game.getCurrentPlayer();
        Position target = player.getPosition().shift(direction);
        GameMap currentGameMap = game.getCurrentPlayerMap();
        Tile[][] tiles = currentGameMap.getTiles();
        int cost = getEnergyCost(player);

        if (player.getEnergy() < cost) {
            return new Result(false, "You don't have enough energy to use watering can.");
        }

        if (player.getEnergyUsedThisTurn() + cost > 50) {
            return new Result(false, "Not enough energy for this turn.");
        }

        if (target.getX() < 0 || target.getY() < 0 ||
            target.getX() >= currentGameMap.getWidth() || target.getY() >= currentGameMap.getHeight()) {
            return new Result(false, "Target tile is out of bounds.");
        }

        if (waterLeft <= 0) {
            return new Result(false, "Watering can is empty.");
        }

        Tile tile = currentGameMap.getTiles()[target.getY()][target.getX()];
        Object content = tile.getContent();

        if (!(content instanceof Crop crop)) {
            return new Result(false, "There is nothing to water here.");
        }

        if (crop.isWatered()) {
            return new Result(false, "This crop is already watered.");
        }

        crop.setWatered(true);
        waterLeft--;
        player.reduceEnergy(cost);
        player.addEnergyUsed(cost);
        player.gainXP(Skill.FARMING, Skill.FARMING.getIncreasePerAction());

        return new Result(true, "Watered crop at " + target);
    }


    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

