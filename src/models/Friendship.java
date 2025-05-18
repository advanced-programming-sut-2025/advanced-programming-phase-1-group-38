package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Friendship {
    private final Player a, b;
    private int xp = 0;
    private int lastInteractionDay = -1;
    private int interactionsToday = 0;
    private int lastDecayDay = -1;
    private final List<String> messages = new ArrayList<>();
    private final List<GiftRecord> gifts = new ArrayList<>();

    public Friendship(Player a, Player b) {
        this.a = a;
        this.b = b;
    }

    public record GiftRecord(String item, int amount, int day, int rate) {}

    public void recordMessage(String from, String msg) {
        messages.add("[" + from + "]: " + msg);
    }

    public List<String> getMessages() { return messages; }

    public void recordGift(String from, String item, int amt, int day, int rate) {
        gifts.add(new GiftRecord(item, amt, day, rate));
    }

    public List<GiftRecord> getGifts() { return gifts; }

    public int getLevel() {
        int level = 0;
        int rem = xp;
        while (true) {
            int needed = (level + 1) * 100;
            if (rem < needed) break;
            rem -= needed;
            level++;
            if (level >= 4) break;
        }
        return level;
    }

    public List<Player> getPlayers() {
        return List.of(a, b);
    }

    public int addXp(int amount, int dayOfYear) {
        if (dayOfYear != lastInteractionDay) {
            lastInteractionDay = dayOfYear;
            interactionsToday = 0;
        }

        if (interactionsToday >= 1) {
            return 0;
        }

        if (getLevel() >= 4) {
            return 0;
        }

        xp += amount;
        interactionsToday++;
        return amount;
    }

    public int getXp() {
        return xp;
    }

    public void dailyDecay(int dayOfYear) {
        if (dayOfYear == lastDecayDay) return;
        lastDecayDay = dayOfYear;

        if (lastInteractionDay < dayOfYear && xp > 0) {
            xp = Math.max(0, xp - 10);
        }
    }

    public void resetXpAndLevel() {
        this.xp = 0;
        this.lastInteractionDay = -1;
        this.interactionsToday = 0;
        this.lastDecayDay = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Friendship other)) return false;
        return (Objects.equals(a, other.a) && Objects.equals(b, other.b)) ||
            (Objects.equals(a, other.b) && Objects.equals(b, other.a));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a) ^ Objects.hash(b);
    }
}
