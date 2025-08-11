// models/QuestDef.java
package io.github.StardewValley.models;

import java.util.*;

public class QuestDef {
    public final int index;                      // 1,2,3 → maps to your NpcQuestService Q1/Q2/Q3
    public final String title;                   // e.g., "Deliver 80 Wood"
    public final Map<ItemType,Integer> requirements;   // items to turn in
    public final int minFriendLevel;             // default 0 (for Q2 etc.)
    public final Integer rewardGold;             // nullable
    public final Integer rewardFriendPoints;     // nullable
    public final Map<ItemType,Integer> rewardItems; // nullable

    private QuestDef(Builder b){
        this.index = b.index;
        this.title = b.title;
        this.requirements = Collections.unmodifiableMap(new LinkedHashMap<>(b.requirements));
        this.minFriendLevel = b.minFriendLevel;
        this.rewardGold = b.rewardGold;
        this.rewardFriendPoints = b.rewardFriendPoints;
        this.rewardItems = b.rewardItems == null ? null :
            Collections.unmodifiableMap(new LinkedHashMap<>(b.rewardItems));
    }

    public static Builder b(int index, String title){ return new Builder(index, title); }

    public static class Builder {
        private final int index;
        private final String title;
        private final Map<ItemType,Integer> requirements = new LinkedHashMap<>();
        private int minFriendLevel = 0;
        private Integer rewardGold = null;
        private Integer rewardFriendPoints = null;
        private Map<ItemType,Integer> rewardItems = null;

        public Builder(int index, String title){ this.index=index; this.title=title; }
        public Builder req(ItemType type, int qty){ requirements.merge(type, qty, Integer::sum); return this; }
        public Builder minLv(int lv){ this.minFriendLevel = lv; return this; }
        public Builder gold(int g){ this.rewardGold = g; return this; }
        public Builder friendPts(int p){ this.rewardFriendPoints = p; return this; }
        public Builder reward(ItemType type, int qty){
            if (rewardItems==null) rewardItems = new LinkedHashMap<>();
            rewardItems.merge(type, qty, Integer::sum);
            return this;
        }
        public QuestDef build(){ return new QuestDef(this); }
    }
}
