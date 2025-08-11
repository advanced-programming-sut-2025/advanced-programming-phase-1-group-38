package io.github.StardewValley.models;

import java.util.HashMap;
import java.util.Map;

public class NpcQuestService {
    public static final class NpcQuestState {
        public boolean q1Completed = false;
        public boolean q2Completed = false;
        public int     q2CompletedDay = Integer.MIN_VALUE; // for Q3 timer
        public boolean q3Completed = false;
    }

    // npcId -> shared quest state
    private final Map<String, NpcQuestState> bag = new HashMap<>();

    private NpcQuestState s(String npcId) {
        return bag.computeIfAbsent(npcId, k -> new NpcQuestState());
    }

    /** Q1 available day 0 until someone completes it; first finisher globally. */
    public boolean tryCompleteQ1(String npcId) {
        NpcQuestState st = s(npcId);
        if (st.q1Completed) return false;
        st.q1Completed = true;
        return true;
    }

    /** Q2 is available to players with level>=1; first finisher globally. */
    public boolean tryCompleteQ2(String npcId, int day, boolean playerHasLevel1) {
        if (!playerHasLevel1) return false;
        NpcQuestState st = s(npcId);
        if (st.q2Completed) return false;
        st.q2Completed = true;
        st.q2CompletedDay = day;
        return true;
    }

    /** Q3 unlocks 28 days after Q2 completion; first finisher globally. */
    public boolean tryCompleteQ3(String npcId, int day) {
        NpcQuestState st = s(npcId);
        if (!st.q2Completed) return false;
        if (day < st.q2CompletedDay + 28) return false;  // not unlocked yet
        if (st.q3Completed) return false;
        st.q3Completed = true;
        return true;
    }

    public boolean isQ1Done(String npcId){ return s(npcId).q1Completed; }
    public boolean isQ2Done(String npcId){ return s(npcId).q2Completed; }
    public boolean isQ3Done(String npcId){ return s(npcId).q3Completed; }
    public boolean isQ3Unlocked(String npcId, int day){
        NpcQuestState st = s(npcId);
        return st.q2Completed && day >= st.q2CompletedDay + 28;
    }

    public int daysUntilQ3(String npcId, int day) {
        NpcQuestState st = s(npcId);
        if (!st.q2Completed) return -1;           // Q2 not done yet
        int unlock = st.q2CompletedDay + 28;
        return Math.max(0, unlock - day);
    }
}
