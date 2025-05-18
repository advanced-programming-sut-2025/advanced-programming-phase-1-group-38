package models.Tools;

import models.*;
import models.Animals.Fish;
import models.enums.*;
import models.enums.Types.FishType;
import models.enums.Types.FishingPoleType;
import models.enums.Types.TileType;
import models.enums.Types.ToolType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FishingPole extends Tool {
    public FishingPole(FishingPoleType fishingPoleType) {
        super(ToolType.FISHING_POLE, fishingPoleType, Skill.FISHING);
    }

    @Override
    public int getEnergyCost(Player player) {
        int baseCost = getToolQuality().getEnergyCost();
        if (player.getSkillLevel(Skill.FISHING) == SkillLevel.LEVEL_FOUR) {
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
        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());

        if (player.getEnergy() < cost) {
            return new Result(false, "You don't have enough energy to use fishing pole.");
        }

        if (player.getEnergyUsedThisTurn() + cost > 50) {
            return new Result(false, "Not enough energy for this turn.");
        }

        if (target.getX() < 0 || target.getY() < 0 ||
            target.getX() >= currentGameMap.getWidth() || target.getY() >= currentGameMap.getHeight()) {
            return new Result(false, "Target tile is out of bounds.");
        }

        Tile tile = tiles[target.getY()][target.getX()];
        boolean isWaterTile = tile.getTileType() == TileType.WATER;

        player.reduceEnergy(cost);
        player.addEnergyUsed(cost);

        if (isWaterTile) {
            Seasons currentSeason = game.getCurrentSeason();
            Weather currentWeather = game.getCurrentWeather();
            double M = getWeatherModifier(currentWeather);

            List<Fish> fishCaught = attemptCatchFish(
                player.getSkillLevel(Skill.FISHING),
                getFishingPoleType(),
                M,
                currentSeason
            );

            if (fishCaught.isEmpty()) {
                return new Result(false, "No fish bit the hook.");
            }

            int addedCount = 0;
            for (Fish fish : fishCaught) {
                if (player.getBackpack().hasSpaceFor(fish, 1)) {
                    player.getBackpack().addToInventory(fish, 1);
                    addedCount++;
                } else {
                    break;
                }
            }

            if (addedCount == 0) {
                return new Result(false, "Your inventory is full. You couldn't keep any fish.");
            }

            player.gainXP(Skill.FISHING, Skill.FISHING.getIncreasePerAction());

            if (addedCount < fishCaught.size()) {
                return new Result(true, "You caught " + fishCaught.size() +
                    " fish, but only had room for " + addedCount + ".");
            }

            return new Result(true, "You caught " + addedCount + " fish!");
        }

        player.gainXP(Skill.FARMING, Skill.FARMING.getIncreasePerAction());
        return new Result(true, "Watered crop at " + target);
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getFishingPoleType()) + ")";
    }

    private double getWeatherModifier(Weather weather) {
        return switch (weather) {
            case SUNNY -> 1.5;
            case RAINY -> 1.2;
            case STORM -> 0.5;
            default -> 1.0;
        };
    }

    private List<Fish> attemptCatchFish(SkillLevel skill, FishingPoleType poleType, double weatherMultiplier, Seasons season) {
        List<Fish> result = new ArrayList<>();
        List<FishType> seasonFish = getBySeason(season, skill);

        if (seasonFish.isEmpty()) return result;

        double R = GameRandom.nextDouble();
        int skillLevel = skill.toInt();
        double M = weatherMultiplier;
        double poleValue = getPoleValue(poleType);
        int count = (int) Math.floor(R * M * (skillLevel + 2));

        for (int i = 0; i < count; i++) {
            FishType randomType = GameRandom.pickRandom(seasonFish);
            double qualityScore = (R * (skillLevel + 2) * poleValue) / (7 - M);
            FishQuality quality = FishQuality.fromScore(qualityScore);
            result.add(new Fish(randomType, quality));
        }

        return result;
    }

    private double getPoleValue(FishingPoleType type) {
        return switch (type) {
            case TRAINING_POLE -> 0.1;
            case BAMBOO_POLE -> 0.5;
            case FIBERGLASS_POLE -> 0.9;
            case IRIDIUM_POLE -> 1.2;
        };
    }

    public static List<FishType> getBySeason(Seasons season, SkillLevel skill) {
        return Arrays.stream(FishType.values())
            .filter(f -> f.getSeason() == season)
            .filter(f -> {
                if (!f.isLegendary()) return true;
                if (skill.toInt() < 4) return false;
                return GameRandom.rollChance(0.1);
            })
            .collect(Collectors.toList());
    }
}
