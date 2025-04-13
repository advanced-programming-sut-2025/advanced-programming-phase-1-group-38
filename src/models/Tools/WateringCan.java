package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;

public class WateringCan extends Tool {
    public WateringCan(ToolQuality quality) {
        super(ToolType.WATERING_CAN, quality, Skill.FARMING);
    }

    @Override
    public void useTool(Direction direction) {}
}

