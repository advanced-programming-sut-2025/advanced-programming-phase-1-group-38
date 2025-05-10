package models.Tools;

import models.Player;
import models.Tool;
import models.enums.Direction;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.ToolQuality;
import models.enums.Types.ToolType;

public class Axe extends Tool {
    public Axe(ToolQuality quality) {
        super(ToolType.AXE, quality, Skill.FORAGING);
    }

    @Override
    public int getEnergyCost(Player player) {
        if (player.getSkillLevel(getRelatedSkill()) == SkillLevel.LEVEL_FOUR) {
            return getToolQuality().getEnergyCost() - 1;
        }
        return getToolQuality().getEnergyCost();
    }

    @Override
    public void useTool(Direction direction) {
        // TODO
    }

    @Override
    public String toString() {
        return formatEnumName(type) + " (" + formatEnumName(getToolQuality()) + ")";
    }
}

