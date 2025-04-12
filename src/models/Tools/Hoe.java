package models.Tools;

import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.ToolQuality;
import models.enums.ToolType;

public class Hoe extends Tool {
    public Hoe(ToolQuality quality) {
        super(ToolType.HOE, quality, null);
    }

    @Override
    public void useTool(Direction direction) {}
}
