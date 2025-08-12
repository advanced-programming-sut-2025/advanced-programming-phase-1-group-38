package io.github.StardewValley.models;

public class Notification {
    public final String fromId;
    public final String toId;
    public final String text;
    public final long   timeMillis;
    public final boolean system;   // ← NEW

    public Notification(String fromId, String toId, String text, long timeMillis) {
        this(fromId, toId, text, timeMillis, false);
    }
    public Notification(String fromId, String toId, String text, long timeMillis, boolean system) {
        this.fromId = fromId;
        this.toId   = toId;
        this.text   = text;
        this.timeMillis = timeMillis;
        this.system = system;
    }
}

