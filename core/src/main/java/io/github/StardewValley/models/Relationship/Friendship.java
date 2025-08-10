package io.github.StardewValley.models.Relationship;

public class Friendship {
    private String npcName;
    private int xp;
    private FriendshipLevel level;

    public Friendship(String npcName) {
        this.npcName = npcName;
        this.xp = 0;
        updateLevel();
    }

    public void gainXP(int amount) {
        xp += amount;
        updateLevel();
    }

    public void reduceXP(int amount) {
        xp = Math.max(0, xp - amount);
        updateLevel();
    }

    private void updateLevel() {

    }

    public String getNpcName() { return npcName; }
    public int getXp() { return xp; }
    public FriendshipLevel getLevel() { return level; }

}
