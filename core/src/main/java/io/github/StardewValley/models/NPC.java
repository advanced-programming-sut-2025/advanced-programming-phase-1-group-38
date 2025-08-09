//package io.github.StardewValley.models;
//
//import io.github.StardewValley.models.enums.Types.NPCType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class NPC {
//    private int x;
//    private int y;
//    private final NPCType type;
//    private final HashMap<Quest, Boolean> quests;
//    private final ArrayList<NPCFriendship> friendships = new ArrayList<>();
//
//    public NPC(NPCType type, ArrayList<Player> players) {
//        this.type = type;
//        this.quests = new HashMap<>();
//        for (Quest quest : this.type.getQuests()) {
//            quests.put(quest, false);
//        }
//
//        this.x = type.getHouseCornerX() + 1;
//        this.y = type.getHouseCornerY() + 1;
//
//        friendships.add(new NPCFriendship(this, players.get(0)));
//        friendships.add(new NPCFriendship(this, players.get(1)));
//        friendships.add(new NPCFriendship(this, players.get(2)));
//        friendships.add(new NPCFriendship(this, players.get(3)));
//    }
//
//    public NPCType getType() {
//        return type;
//    }
//
//    public int getX() {
//        return x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public HashMap<Quest, Boolean> getQuests() {
//        return quests;
//    }
//
//    public ArrayList<NPCFriendship> getFriendships() {
//        return friendships;
//    }
//
//    public Integer getFriendShipLevelWith(Player player) {
//        for (NPCFriendship friendship : this.friendships) {
//            if (friendship.getPlayer().equals(player)) return friendship.getFriendshipLevel();
//        }
//
//        return null;
//    }
//
//    public NPCFriendship getFriendShipWith(Player player) {
//        for (NPCFriendship friendship : this.friendships) {
//            if (friendship.getPlayer().equals(player)) return friendship;
//        }
//
//        return null;
//    }
//
//    public void finishQuest(Quest quest) {
//        this.quests.put(quest, true);
//    }
//}
