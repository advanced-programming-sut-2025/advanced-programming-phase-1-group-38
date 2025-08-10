package io.github.StardewValley.models;

public class SkillState {
    private int level = 0;
    private int xp = 0;

    public int level() { return level; }
    public int xp() { return xp; }
    public int xpToNext() { return 100 + level * 50; } // ساده

    public boolean addXp(int amount) {
        xp += amount;
        boolean leveled = false;
        while (xp >= xpToNext()) {
            xp -= xpToNext();
            level++;
            leveled = true;
        }
        return leveled;
    }
}