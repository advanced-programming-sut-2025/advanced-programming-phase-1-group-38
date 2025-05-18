package models;

import java.util.List;

public class Quest {
    private String description;
    private List<String> requirements;
    private List<String> rewards;
    private boolean isActive;
    private boolean isCompleted;
    private int unlockFriendshipLevel;
    private int unlockDay;



    public Quest(String description, List<String> requirements, List<String> rewards,
                 boolean isActive, int unlockFriendshipLevel, int unlockDay) {
        this.description = description;
        this.requirements = requirements;
        this.rewards = rewards;
        this.isActive = isActive;
        this.isCompleted = false;
        this.unlockFriendshipLevel = unlockFriendshipLevel;
        this.unlockDay = unlockDay;
    }


    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getRequirements() { return requirements; }
    public void setRequirements(List<String> requirements) { this.requirements = requirements; }

    public List<String> getRewards() { return rewards; }
    public void setRewards(List<String> rewards) { this.rewards = rewards; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public int getUnlockFriendshipLevel() { return unlockFriendshipLevel; }
    public void setUnlockFriendshipLevel(int unlockFriendshipLevel) {
        this.unlockFriendshipLevel = unlockFriendshipLevel;
    }

    public int getUnlockDay() { return unlockDay; }
    public void setUnlockDay(int unlockDay) { this.unlockDay = unlockDay; }

}
