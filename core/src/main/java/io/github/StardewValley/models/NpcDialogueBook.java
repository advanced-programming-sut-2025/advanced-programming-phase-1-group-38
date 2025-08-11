package io.github.StardewValley.models;

import java.util.*;

public class NpcDialogueBook {

    public static final class Line {
        public final String text;
        public final int minHour;        // inclusive
        public final int maxHour;        // exclusive
        public final int minFriendLevel; // 0..3

        public Line(String text, int minHour, int maxHour, int minFriendLevel) {
            this.text = text;
            this.minHour = minHour;
            this.maxHour = maxHour;
            this.minFriendLevel = minFriendLevel;
        }

        public boolean matches(GameTime t, int level) {
            int h = t.getHour();
            boolean hourOk = (h >= minHour && h < maxHour);
            boolean lvlOk  = (level >= minFriendLevel);
            return hourOk && lvlOk;
        }
    }

    private static final Map<String, List<Line>> LINES = new HashMap<>();

    static {
        // ROBIN sample lines — tweak/add freely
        add("robin", new Line("Morning! Got any projects today?", 9, 12, 0));
        add("robin", new Line("Afternoon’s perfect for building.", 12, 18, 0));
        add("robin", new Line("You’ve been helpful lately. Thanks!", 9, 22, 1));
        add("robin", new Line("We make a good team, you and I.", 9, 22, 2));
        add("robin", new Line("Drop by the shop if you need upgrades.", 9, 17, 0));

        // Add more NPCs:
        // add("gus", new Line("Grab a drink at the saloon!", 16, 24, 0));
    }

    private static void add(String npcId, Line line) {
        LINES.computeIfAbsent(npcId, k -> new ArrayList<>()).add(line);
    }

    public static List<Line> getFor(String npcId) {
        return LINES.getOrDefault(npcId, Collections.emptyList());
    }
}
