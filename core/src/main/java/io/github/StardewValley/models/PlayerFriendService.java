package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Map;

public class PlayerFriendService {
    public static final int POINTS_PER_LEVEL = 100;

    private static final class Rel {
        int points = 300;
        int lastTalkDay = Integer.MIN_VALUE; // pairwise daily talk gate
        int lastGiftDay = Integer.MIN_VALUE; // pairwise daily gift gate
    }

    private final Map<String, Integer> lastInteractionDayPair = new HashMap<>();
    private final Map<String, Integer> lastHugDayPair = new HashMap<>();   // pair-symmetric (A|B)
    private final Map<String, Boolean> level3Unlocked = new HashMap<>();

    private final Map<String, String> spouseOf = new HashMap<>();          // playerId -> spouseId
    private final Map<String, String> pendingProposalFrom = new HashMap<>();// proposee -> proposer
    private final Map<String, Boolean> level4Unlocked = new HashMap<>();    // A|B -> unlocked



    private final Map<String, Rel> bag = new HashMap<>();

    private String key(String a, String b) {
        return (a.compareTo(b) < 0) ? (a + "|" + b) : (b + "|" + a);
    }
    private String[] splitKey(String k) {
        int i = k.indexOf('|');
        return new String[]{ k.substring(0, i), k.substring(i+1) };
    }
    private void markInteraction(String a, String b, int day) {
        lastInteractionDayPair.put(key(a,b), day);
    }
    private Rel rel(String a, String b) { return bag.computeIfAbsent(key(a,b), k -> new Rel()); }

    public boolean markFirstTalkToday(String a, String b, int day) {
        Rel r = rel(a,b);
        if (r.lastTalkDay == day) return false;
        r.lastTalkDay = day;
        markInteraction(a,b,day);                   // ← mark any interaction
        return true;
    }
    public boolean markFirstGiftToday(String a, String b, int day) {
        Rel r = rel(a,b);
        if (r.lastGiftDay == day) return false;
        r.lastGiftDay = day;
        markInteraction(a,b,day);                   // ← mark any interaction
        return true;
    }
    public boolean markFirstHugToday(String a, String b, int day) {
        String k = key(a,b);
        Integer d = lastHugDayPair.get(k);
        if (d != null && d == day) return false;
        lastHugDayPair.put(k, day);
        markInteraction(a,b,day);                   // ← mark any interaction
        return true;
    }

    // Nightly decay: call once when the day flips
    public void endOfDayDecay(int endedDay) {
        for (String k : bag.keySet()) {
            int last = lastInteractionDayPair.getOrDefault(k, Integer.MIN_VALUE);
            if (last != Integer.MIN_VALUE && last != endedDay) {
                String[] ab = splitKey(k);
                addPoints(ab[0], ab[1], -10);
            }
        }
    }

    public boolean hasSpouse(String p) { return spouseOf.containsKey(p); }
    public String spouse(String p)      { return spouseOf.get(p); }
    public boolean isMarried(String a, String b) {
        return b.equals(spouseOf.get(a));
    }
    public boolean isLevel4Unlocked(String a, String b) {
        return level4Unlocked.getOrDefault(key(a,b), false);
    }
    private void unlockLevel4(String a, String b) {
        level4Unlocked.put(key(a,b), true);
    }

    // keep your Lv3 gate, add Lv4 gate:
    public int level(String a, String b) {
        int raw = Math.min(4, rel(a,b).points / POINTS_PER_LEVEL);
        if (raw >= 3 && !isLevel3Unlocked(a,b)) return 2; // flower needed
        if (raw >= 4 && !isLevel4Unlocked(a,b)) return 3; // marriage acceptance needed
        return raw;
    }

    public int points(String a, String b) {
        return rel(a,b).points;
    }

    public void addPoints(String a, String b, int delta) {
        Rel r = rel(a, b);
        if (delta == 0) return;

        int cap = pointsCap(a, b);
        int before = r.points;
        long raw = (long) before + delta;

        int after;
        if (delta > 0) {
            if (before >= cap) return;
            after = (int) Math.min(raw, cap);
        } else {
            after = (int) Math.max(0, raw);
        }

        if (after != before) {
            r.points = after;
        }
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

    // lock/unlock for level 3
    public boolean isLevel3Unlocked(String a, String b) {
        return level3Unlocked.getOrDefault(key(a, b), false);
    }
    public void unlockLevel3(String a, String b) {
        level3Unlocked.put(key(a, b), true);
    }

    /** Call after a "flower" gift. Only unlocks if Lv2 points are filled (>= 300). */
    public boolean tryUnlockLevel3WithFlower(String a, String b) {
        if (!isLevel3Unlocked(a, b) && points(a, b) >= 3 * POINTS_PER_LEVEL) {
            unlockLevel3(a, b);
            return true;
        }
        return false;
    }

    private int pointsCap(String a, String b) {
        return isLevel3Unlocked(a, b) ? 4 * POINTS_PER_LEVEL : 3 * POINTS_PER_LEVEL;
    }

    public boolean canPropose(String a, String b) {
        if (a.equals(b)) return false;
        if (hasSpouse(a) || hasSpouse(b)) return false;              // ← one spouse only
        if (!isLevel3Unlocked(a,b)) return false;                    // must have unlocked Lv3
        return points(a,b) >= 4 * POINTS_PER_LEVEL && !isLevel4Unlocked(a,b);
    }

    public String pendingProposer(String proposeId) {
        return pendingProposalFrom.get(proposeId);
    }

    // Call after you verify & remove a ring from 'a' in the UI
    public boolean proposeMarriage(String a, String b) {
        if (!canPropose(a,b)) return false;
        if (pendingProposalFrom.containsKey(b)) return false;        // one pending per proposee
        pendingProposalFrom.put(b, a);
        return true;
    }

    public boolean acceptMarriage(String b, String a) { // proposee b accepts proposer a
        String from = pendingProposalFrom.get(b);
        if (from == null || !from.equals(a)) return false;
        if (hasSpouse(a) || hasSpouse(b)) { pendingProposalFrom.remove(b); return false; }

        spouseOf.put(a, b);
        spouseOf.put(b, a);
        unlockLevel4(a, b);

        Rel r = rel(a,b);
        r.points = Math.max(r.points, 4 * POINTS_PER_LEVEL); // snap to 400 if needed
        pendingProposalFrom.remove(b);
        return true;
    }

    public boolean declineMarriage(String b) { // proposee declines; reset to Lv0
        String a = pendingProposalFrom.remove(b);
        if (a == null) return false;
        rel(a,b).points = 0;
        level3Unlocked.remove(key(a,b));
        level4Unlocked.remove(key(a,b));
        return true;
    }


}
