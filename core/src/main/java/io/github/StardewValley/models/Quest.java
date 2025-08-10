//package io.github.StardewValley.models;
//
//import io.github.StardewValley.models.enums.Types.MaterialTypes;
//import io.github.StardewValley.models.enums.Types.NPCType;
//
//
//public class Quest {
//    public static int questCounter = 1;
//    private final NPCType questGiver;
//    private final MaterialTypes demandingItem;
//    private final int demandingAmount;
//    private final MaterialTypes rewardItem;
//    private final int rewardAmount;
//    private final int id;
//
//    public Quest(NPCType questGiver, MaterialTypes demand, int demandAmount, MaterialTypes reward, Integer rewardAmount) {
//        this.questGiver = questGiver;
//        this.demandingItem = demand;
//        this.demandingAmount = demandAmount;
//        this.rewardItem = reward;
//        this.rewardAmount = rewardAmount;
//        this.id = questCounter++;
//    }
//
//    public NPCType getQuestGiver() {
//        return this.questGiver;
//    }
//
//    public int getId() {
//        return this.id;
//    }
//
//    public MaterialTypes getDemand() {
//        return this.demandingItem;
//    }
//
//    public int getDemandAmount() {
//        return this.demandingAmount;
//    }
//
//    public MaterialTypes getReward() {
//        return this.rewardItem;
//    }
//
//    public int getRewardAmount() {
//        return this.rewardAmount;
//    }
//
//    @Override
//    public String toString() {
//        return this.questGiver.name() + "\'s quest: " +
//            this.demandingItem.toString() + " x" + this.demandingAmount +
//            " for " + this.rewardItem.toString() + " x" + this.rewardAmount +
//            "\nId: " + this.id + "\n---------------------------\n";
//    }
//}
