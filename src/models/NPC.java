package models;

import java.util.*;

public class NPC {
    private final String name;
    private final List<String> favoriteItems;
    private final List<Quest> quests;
    private final Shop shop;
    private int friendshipPoints;
    private List<String> dialog;


    public NPC(String name, List<String> favoriteItems, List<Quest> quests, Shop shop , List<String> dialog) {
        this.name = name;
        this.favoriteItems = favoriteItems;
        this.friendshipPoints = 0;
        this.quests = quests;
        this.shop = shop;
        this.dialog = dialog;

    }

    public String getName() {
        return name;
    }

    public List<String> getFavoriteItems() {
        return favoriteItems;
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }

    public List<String> getDialog() {
        return dialog;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public Shop getShop() {
        return shop;
    }

    public void increaseFriendship(int points) {
    }

    public void addQuest(Quest quest) {
    }

    public void receiveGift(Item item) {
    }

}
