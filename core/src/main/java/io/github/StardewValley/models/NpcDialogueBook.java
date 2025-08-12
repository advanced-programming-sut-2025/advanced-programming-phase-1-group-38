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
        // Robin
        // Lv0
        add("robin", new Line("Morning! Got any projects today?", 9, 12, 0));
        add("robin", new Line("Afternoon’s perfect for building.", 12, 18, 0));
        add("robin", new Line("Drop by the shop if you need upgrades.", 9, 17, 0));
        // Lv1
        add("robin", new Line("Bring me wood and iron bars; I can make anything sturdy.", 9, 17, 1));
        add("robin", new Line("Spaghetti night fuels long blueprint sessions.", 12, 22, 1));
        // Lv2
        add("robin", new Line("Your farm layout is improving—nice eye for space.", 9, 22, 2));
        add("robin", new Line("If you stockpile 80 wood, we can start something big.", 9, 17, 2));
        // Lv3
        add("robin", new Line("I’ve drafted plans for a deluxe project—need lots of lumber.", 9, 17, 3));
        add("robin", new Line("Thanks for all the help lately. We make a great build crew.", 12, 22, 3));

        // Abigail
        // Lv0
        add("abigail", new Line("Hey! Have you checked the mines today?", 9, 12, 0));
        add("abigail", new Line("Coffee keeps me going on long days.", 9, 22, 0));
        // Lv1
        add("abigail", new Line("Iron ore is perfect for the little projects I’m trying.", 12, 18, 1));
        add("abigail", new Line("If you’re heading underground, I might tag along.", 12, 18, 1));
        // Lv2
        add("abigail", new Line("I’ve been practicing with my sword. Feeling braver lately.", 9, 22, 2));
        add("abigail", new Line("The caves are quieter with a friend around.", 18, 22, 2));
        // Lv3
        add("abigail", new Line("Someday let’s push past floor fifty together.", 18, 22, 3));
        add("abigail", new Line("Bring coffee, I’ll bring the courage. Deal?", 9, 12, 3));
    }

    private static void add(String npcId, Line line) {
        LINES.computeIfAbsent(npcId, k -> new ArrayList<>()).add(line);
    }

    public static List<Line> getFor(String npcId) {
        return LINES.getOrDefault(npcId, Collections.emptyList());
    }
}
