package models;

import java.util.List;

public class Quest {
    private final List<String> requiredItems;
    private final List<String> rewards;

    public Quest(List<String> requiredItems, List<String> rewards) {
        this.requiredItems = requiredItems;
        this.rewards = rewards;
    }

    public List<String> getRequiredItems() { return requiredItems; }
    public List<String> getRewards() { return rewards; }
}
