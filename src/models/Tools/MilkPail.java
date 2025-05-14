package models.Tools;

import models.Game;
import models.Player;
import models.Result;
import models.Tool;
import models.enums.Direction;
import models.enums.Types.ToolType;

public class MilkPail extends Tool {
    public MilkPail() {
        super(ToolType.MILK_PAIL, 4 , null);
    }


    @Override
    public int getEnergyCost(Player player) {
        return 4;
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        Player player = game.getCurrentPlayer();
        int cost = getModifiedEnergyCost(player, game.getCurrentWeather());
        return null;
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

