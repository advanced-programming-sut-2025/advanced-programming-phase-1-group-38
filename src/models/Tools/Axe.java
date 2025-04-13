package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;

public class Axe extends Tool {
    public Axe(ToolQuality quality) {
        super(ToolType.AXE, quality, Skill.MINING);
    }

    @Override
    public void useTool(Direction direction) {}
}

