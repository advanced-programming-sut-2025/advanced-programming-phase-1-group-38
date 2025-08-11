package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Map;

public class PlayerFriendService {
    public static final int POINTS_PER_LEVEL = 100;

    private static final class Rel {
        int points = 0;
        int lastTalkDay = Integer.MIN_VALUE; // pairwise daily talk gate
        int lastGiftDay = Integer.MIN_VALUE; // pairwise daily gift gate
    }

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
        Rel r = rel(a,b);
        int before = r.points;
        r.points += delta;
        if (r.points < 0) r.points = 0;
        if (r.points > 4 * POINTS_PER_LEVEL) r.points = 4 * POINTS_PER_LEVEL;
    }

    // Kept for compatibility with existing calls
    public void add(String a, String b, int delta) { addPoints(a, b, delta); }

    /** Legacy helper: immediate add (no gating). */
    public int talkAnytime(String a, String b, int delta) {
        addPoints(a, b, delta);
        return delta;
    }

    /** Legacy helper: pairwise first gift per day. */
    public int giftOncePerDay(String a, String b, int day, boolean isFavorite) {
        Rel r = rel(a,b);
        if (r.lastGiftDay == day) return 0;
        r.lastGiftDay = day;
        int delta = isFavorite ? 200 : 50;
        addPoints(a, b, delta);
        return delta;
    }

    /** New: pairwise (A↔B) first talk of the day. */
    public boolean markFirstTalkToday(String a, String b, int day) {
        Rel r = rel(a,b);
        if (r.lastTalkDay == day) return false;
        r.lastTalkDay = day;
        return true;
    }

    /** New: pairwise (A↔B) first gift of the day (used when rating a gift). */
    public boolean markFirstGiftToday(String a, String b, int day) {
        Rel r = rel(a,b);
        if (r.lastGiftDay == day) return false;
        r.lastGiftDay = day;
        return true;
    }
}
