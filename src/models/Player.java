package models;

import models.Relationship.Friendship;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.Types.BackpackType;
import models.inventory.Backpack;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private final User user;
    private Position position;
    private Backpack backpack;
    private Tool currentTool;
    private final Map<Skill, SkillLevel> skillLevels = new HashMap<>();
    private final Map<Skill, Integer> skillXP = new HashMap<>();
    private int energy;
    private boolean unlimitedEnergy = false;
    private int energyUsedThisTurn = 0;
    private int money;
    private boolean isFainted;
    private final Map<String, Friendship> friendships = new HashMap<>();



    public Player(User user) {
        this(user, new Position(0, 0), new Backpack(BackpackType.INITIAL));
    }

    public Player(User user, Position position, Backpack backpack) {
        this.user = user;
        this.position = position;
        this.backpack = backpack;
        this.currentTool = null;
        for (Skill skill : Skill.values()) {
            skillLevels.put(skill, SkillLevel.LEVEL_ZERO);
            skillXP.put(skill, 0);
        }
        this.energy = 200;
        this.energyUsedThisTurn = 0;
        this.money = 100;
        this.isFainted = false;
    }

    public Map<String, Friendship> getFriendships() { return friendships; }

    public String getName() {
        return user.getUsername();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }

    public void addMoney(int amount) {
        this.money += amount;
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

    private int getRequiredXPForNextLevel(Skill skill) {
        SkillLevel currentLevel = getSkillLevel(skill);
        return 50 + 100 * currentLevel.ordinal();
    }

    private void increaseSkill(Skill skill) {
        SkillLevel current = getSkillLevel(skill);
        int nextOrdinal = Math.min(current.ordinal() + 1, SkillLevel.values().length - 1);
        skillLevels.put(skill, SkillLevel.values()[nextOrdinal]);
    }

    public void gainXP(Skill skill, int amount) {
        int currentXP = skillXP.getOrDefault(skill, 0) + amount;
        skillXP.put(skill, currentXP);

        SkillLevel currentLevel = getSkillLevel(skill);
        int required = getRequiredXPForNextLevel(skill);

        if (currentXP >= required && currentLevel != SkillLevel.LEVEL_FOUR) {
            increaseSkill(skill);
            skillXP.put(skill, currentXP - required);
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void reduceEnergy(int amount) {
        if (!unlimitedEnergy) {
            energy = Math.max(0, energy - amount);
            if (energy == 0) {
                isFainted = true;
            }
        }
    }

    public void setEnergy(int value) {
        this.energy = Math.min(value, 200);
    }

    public void enableUnlimitedEnergy() {
        this.unlimitedEnergy = true;
    }

    public boolean isFainted() {
        return isFainted;
    }

    public void setFainted(boolean fainted) {
        this.isFainted = fainted;
    }

    public int getEnergyUsedThisTurn() {
        return energyUsedThisTurn;
    }

    public void addEnergyUsed(int amount) {
        if (!unlimitedEnergy) {
            energyUsedThisTurn += amount;
        }
    }

    public void resetTurnEnergy() {
        energyUsedThisTurn = 0;
    }

    public void resetEnergy() {
        this.energy = 200;
    }
}
