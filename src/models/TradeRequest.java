package models;

import java.util.Objects;

public class TradeRequest {
    public enum Type { OFFER, REQUEST }

    private static int NEXT_ID = 1;

    private final int id;
    private final Type type;
    private final Player from;
    private final Player to;
    private final Item item;
    private final int amount;
    private final Item targetItem;
    private final int targetAmount;
    private final int dayOfYear;

    public TradeRequest(Type type, Player from, Player to, Item item, int amount, Item targetItem, int targetAmount, int dayOfYear) {
        this.id = NEXT_ID++;
        this.type = type;
        this.from = from;
        this.to = to;
        this.item = item;
        this.amount = amount;
        this.targetItem = targetItem;
        this.targetAmount = targetAmount;
        this.dayOfYear = dayOfYear;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Player getFrom() {
        return from;
    }

    public Player getTo() {
        return to;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public Item getTargetItem() {
        return targetItem;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trade#").append(id)
            .append(" [").append(type).append("] from ")
            .append(from.getName()).append(" to ")
            .append(to.getName()).append(": ")
            .append(amount).append("×").append(item.getName());
        if (targetItem != null) {
            sb.append(" ↔ ")
                .append(targetAmount).append("×").append(targetItem.getName());
        }
        sb.append(" (day ").append(dayOfYear).append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeRequest)) return false;
        TradeRequest that = (TradeRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
