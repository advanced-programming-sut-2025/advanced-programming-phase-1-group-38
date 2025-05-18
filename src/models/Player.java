package models;

import models.enums.Gender;
import models.enums.Skill;
import models.enums.SkillLevel;
import models.enums.Types.BackpackType;
import models.inventory.Backpack;

import java.util.*;

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
    private final Map<Player, Friendship> friendships = new HashMap<>();
    private final List<String> notifications = new ArrayList<>();

    public enum PlayerGender {MALE, FEMALE}

    private final PlayerGender gender;
    private Player marriedTo = null;
    private boolean isMarried = false;
    private int energyPenaltyUntilDay = -1;
    private Player pendingProposalFrom = null;
    private Item pendingProposalRing = null;

    public Player(User user) {
        this(user, new Position(0, 0), new Backpack(BackpackType.INITIAL), PlayerGender.MALE);
    }

    public Player(User user, Position position, Backpack backpack, PlayerGender gender) {
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
        this.gender = gender;
    }

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

    public Collection<Friendship> getAllFriendships() {
        return friendships.values();
    }

    public Friendship getFriendship(Player other) {
        String keyThis = this.getName().toLowerCase();
        String keyOther = other.getName().toLowerCase();
        Player key = keyThis.compareTo(keyOther) <= 0 ? other : this;

        friendships.computeIfAbsent(key, p -> new Friendship(this, other));
        return friendships.get(key);
    }

    public int interactWith(Player other, int xpValue, Time time) {
        int today = time.getDayOfYear();
        Friendship f = getFriendship(other);
        int gained = f.addXp(xpValue, today);
        if (gained > 0) {
            other.getFriendship(this).addXp(gained, today);
        }
        return gained;
    }

    public void receiveNotification(String message) {
        notifications.add(message);
    }

    public List<String> drainNotifications() {
        List<String> copy = new ArrayList<>(notifications);
        notifications.clear();
        return copy;
    }

    public PlayerGender getGender() { return gender; }

    public boolean isMarried() { return isMarried; }
    public Player  getMarriedTo() { return marriedTo; }

    public void marry(Player spouse) {
        this.isMarried  = true;
        this.marriedTo = spouse;
    }

    public void receiveMarriageProposal(Player from, Item ring) {
        this.pendingProposalFrom = from;
        this.pendingProposalRing = ring;
    }
    public void clearMarriageProposal() {
        this.pendingProposalFrom = null;
        this.pendingProposalRing = null;
    }
    public Player getPendingProposalFrom() { return pendingProposalFrom; }
    public Item   getPendingProposalRing() { return pendingProposalRing; }
    public void setRejectionPenaltyUntil(int dayOfYear) {
        this.energyPenaltyUntilDay = dayOfYear;
    }
    public boolean isUnderRejectionPenalty(int today) {
        return today <= energyPenaltyUntilDay;
    }
}
