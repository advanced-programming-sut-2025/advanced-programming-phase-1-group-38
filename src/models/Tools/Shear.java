package models.Tools;

import models.Game;
import models.Player;
import models.Result;
import models.Tool;
import models.enums.Direction;
import models.enums.Types.ToolType;

public class Shear extends Tool {
    public Shear() {
        super(ToolType.SHEAR, 4, null);
    }

    @Override
    public int getEnergyCost(Player player) {
        return 4;
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        return null;
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

