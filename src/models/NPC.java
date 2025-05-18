package models;

import java.util.List;

public class NPC {
    private String name;
    private String job;
    private List<String> favoriteItems;
    private List<String> dialogLines;
    private List<Quest> quests;
    private List<String> giftItems;
    private Position position;

    public NPC(String name, String job, List<String> favoriteItems, List<String> dialogLines,
               List<Quest> quests, List<String> giftItems, Position position) {
        this.name = name;
        this.job = job;
        this.favoriteItems = favoriteItems;
        this.dialogLines = dialogLines;
        this.quests = quests;
        this.giftItems = giftItems;
        this.position = position;
    }


    public String getName() { return name; }
    public String getJob() { return job; }
    public List<String> getFavoriteItems() { return favoriteItems; }
    public List<String> getDialogLines() { return dialogLines; }
    public List<Quest> getQuests() { return quests; }
    public List<String> getGiftItems() { return giftItems; }
    public Position getPosition() { return position; }

    public void setName(String name) { this.name = name; }
    public void setJob(String job) { this.job = job; }
    public void setFavoriteItems(List<String> favoriteItems) { this.favoriteItems = favoriteItems; }
    public void setDialogLines(List<String> dialogLines) { this.dialogLines = dialogLines; }
    public void setQuests(List<Quest> quests) { this.quests = quests; }
    public void setGiftItems(List<String> giftItems) { this.giftItems = giftItems; }
    public void setPosition(Position position) { this.position = position; }
}
