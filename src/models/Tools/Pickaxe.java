package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;

public class Pickaxe extends Tool {
    public Pickaxe(ToolQuality quality) {
        super(ToolType.PICKAXE, quality, Skill.MINING);
    }

    @Override
    public void useTool(Direction direction) {}
}

