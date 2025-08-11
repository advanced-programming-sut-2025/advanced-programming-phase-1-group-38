package io.github.StardewValley.models;

// Notification.java
public class Notification {
    public final String fromId;
    public final String toId;       // <-- add this
    public final String text;
    public final long   timeMillis;

    public Notification(String fromId, String toId, String text, long timeMillis) {
        this.fromId = fromId;
        this.toId   = toId;
        this.text   = text;
        this.timeMillis = timeMillis;
    }
}
