package models;

import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.Types.BackpackType;
import models.inventory.Backpack;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private Position position;
    private Backpack backpack;
    private Tool currentTool;
    private Map<Skill, SkillLevel> skillLevels = new HashMap<>();
    private Map<Skill, Integer> skillXP = new HashMap<>();

    public Player(Position position, Backpack backpack) {
        this.position = position;
        this.backpack = new Backpack(BackpackType.INITIAL);
        this.currentTool = null;
        for (Skill skill : Skill.values()) {
            skillLevels.put(skill, SkillLevel.LEVEL_ZERO);
            skillXP.put(skill, 0);
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public Tool getCurrentTool() {
        return currentTool;
    }
    public void setCurrentTool(Tool currentTool) {
        this.currentTool = currentTool;
    }

    public SkillLevel getSkillLevel(Skill skill) {
        return skillLevels.getOrDefault(skill, SkillLevel.LEVEL_ZERO);
    }

    public int getRequiredXPForNextLevel(Skill skill) {
        SkillLevel currentLevel = getSkillLevel(skill);
        int i = currentLevel.ordinal();
        return 50 + 100 * i;
    }

    public void increaseSkill(Skill skill) {
        SkillLevel current = getSkillLevel(skill);
        int nextOrdinal = Math.min(current.ordinal() + 1, SkillLevel.values().length - 1);
        skillLevels.put(skill, SkillLevel.values()[nextOrdinal]);
    }

    public void gainXP(Skill skill, int amount) {
        int currentXP = skillXP.getOrDefault(skill, 0);
        currentXP += amount;
        skillXP.put(skill, currentXP);

        SkillLevel currentLevel = skillLevels.get(skill);
        int required = getRequiredXPForNextLevel(skill);

        if (currentXP >= required && currentLevel != SkillLevel.LEVEL_FOUR) {
            increaseSkill(skill);
            skillXP.put(skill, currentXP - required);
        }
    }
}
