package models.Tools;

import models.Player;
import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;

public class Hoe extends Tool {
    public Hoe(ToolQuality quality) {
        super(ToolType.HOE, quality, Skill.FARMING);
    }

    @Override
    public int getEnergyCost(Player player) {
        return 0;
    }

    @Override
    public void useTool(Direction direction) {}

    @Override
    public String toString() {
        return "";
    }
}
