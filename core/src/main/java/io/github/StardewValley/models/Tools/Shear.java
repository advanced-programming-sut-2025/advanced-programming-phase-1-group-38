//package io.github.StardewValley.models.Tools;
//
//import io.github.StardewValley.models.Animals.Animal;
//import io.github.StardewValley.models.Animals.AnimalLivingSpace;
//import io.github.StardewValley.models.Animals.AnimalProduct;
//import io.github.StardewValley.models.*;
//import io.github.StardewValley.models.enums.Direction;
//import io.github.StardewValley.models.enums.Types.AnimalProductType;
//import io.github.StardewValley.models.enums.Types.AnimalType;
//
//public class Shear extends Tool {
//    public Shear() {
//        super(ToolType.SHEAR, 4, null);
//    }
//
//    @Override
//    public int getEnergyCost(Player player) {
//        return 4;
//    }
//
//    @Override
//    public Result useTool(Game game, Direction direction) {
//        Player player = game.getCurrentPlayer();
//        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());
//
//        if (player.getEnergy() < cost) {
//            return new Result(false, "You don't have enough energy.");
//        }
//        if (player.getEnergyUsedThisTurn() + cost > 50) {
//            return new Result(false, "Not enough energy for this turn.");
//        }
//
//        Position target = player.getPosition().shift(direction);
//        GameMap map = game.getCurrentPlayerMap();
//
//        Animal sheep = null;
//        for (AnimalLivingSpace space : map.getAnimalBuildings()) {
//            for (Animal a : space.getAnimals()) {
//                if (a.getPosition().equals(target)) {
//                    sheep = a;
//                    break;
//                }
//            }
//            if (sheep != null) break;
//        }
//
//        if (sheep == null) {
//            return new Result(false, "There's no animal here to use that tool on.");
//        }
//        if (sheep.getAnimalType() != AnimalType.SHEEP) {
//            return new Result(false, "You can only use the Shear on sheep.");
//        }
//
//        AnimalProduct product = sheep.collectProduct();
//        if (product == null || product.getProductType() != AnimalProductType.WOOL) {
//            return new Result(false, "This sheep isn't ready to be sheared yet.");
//        }
//
//        player.reduceEnergy(cost);
//        player.addEnergyUsed(cost);
//        if (!player.getBackpack().hasSpaceFor(product, 1)) {
//            return new Result(false, "Your inventory is full.");
//        }
//        player.getBackpack().addToInventory(product, 1);
//
//        return new Result(true, "You sheared " + sheep.getName() +
//            " and collected " +
//            product.getQuality().name().toLowerCase() +
//            " wool.");
//    }
//
//    @Override
//    public String toString() {
//        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
//    }
//}
//
