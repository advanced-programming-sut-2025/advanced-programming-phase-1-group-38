package io.github.StardewValley.models;

import java.util.HashMap;
import java.util.Map;

public class PlayerFriendService {
    public static final int POINTS_PER_LEVEL = 200; // 0..4 → 0..800

    private static final class Rel {
        int points = 0;
        int lastGiftDay    = Integer.MIN_VALUE;
    }

    // normalized "A|B" key so A↔B is the same record
    private final Map<String, Rel> bag = new HashMap<>();

    private String key(String a, String b) {
        return (a.compareTo(b) < 0) ? (a + "|" + b) : (b + "|" + a);
    }
    private Rel rel(String a, String b) {
        return bag.computeIfAbsent(key(a,b), k -> new Rel());
    }

    public int level(String a, String b) {
        return Math.min(4, rel(a,b).points / POINTS_PER_LEVEL);
    }
    public int points(String a, String b) {
        return rel(a,b).points;
    }
    public void addPoints(String a, String b, int delta) {
        rel(a,b).points += delta;
        if (rel(a,b).points < 0) rel(a,b).points = 0;
        if (rel(a,b).points > 4 * POINTS_PER_LEVEL) rel(a,b).points = 4 * POINTS_PER_LEVEL;
    }

    /** First talk per minute → +10 (tune as you like). */
    public int talkAnytime(String a, String b, int delta) {
        addPoints(a, b, delta);
        return delta;
    }

    /** First gift per day → +50; if already level≥2 maybe +100; tune freely. */
    public int giftOncePerDay(String a, String b, int day, boolean isFavorite) {
        Rel r = rel(a,b);
        if (r.lastGiftDay == day) return 0;
        r.lastGiftDay = day;
        int delta = isFavorite ? 200 : 50;
        r.points += delta;
        return delta;
    }
}
