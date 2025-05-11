package models.Tools;

import models.Game;
import models.Player;
import models.Result;
import models.Tool;
import models.enums.*;
import models.enums.Types.ToolType;

public class TrashCan extends Tool {
    public TrashCan(TrashCanQuality quality) {
        super(ToolType.TRASH_CAN, quality, null);
    }

    @Override
    public int getEnergyCost(Player player) {
        return 0;
    }

    @Override
    public Result useTool(Game game, Direction direction) {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
