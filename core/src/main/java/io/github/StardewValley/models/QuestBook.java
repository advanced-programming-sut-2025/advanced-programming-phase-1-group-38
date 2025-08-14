// models/QuestBook.java
package io.github.StardewValley.models;

import io.github.StardewValley.models.Artisan.MachineType;
import io.github.StardewValley.models.enums.Types.CraftingMachineType;
import io.github.StardewValley.models.enums.Types.MaterialType;

import java.util.*;

public final class QuestBook {
    private static final Map<String, List<QuestDef>> NPC_QUESTS = new HashMap<>();

    static {
        // ROBIN
        List<QuestDef> robin = new ArrayList<>();
        robin.add(QuestDef.b(1, "Deliver 80 Wood")
            .req(MaterialType.Wood, 80)
            .gold(1000)
            .build());

        robin.add(QuestDef.b(2, "Deliver 10 Iron Bar")
            .minLv(1) // requires Lv1 friendship
            .req(MaterialType.IronBar, 10)
            .reward(MachineType.BEE_HOUSE, 3)
            .build());

        robin.add(QuestDef.b(3, "Deliver 1000 Wood")
            .req(MaterialType.Wood, 1000)
            .gold(25000)
            .build());

        NPC_QUESTS.put("robin", Collections.unmodifiableList(robin));

        // Add more NPCs here the same way:
        // NPC_QUESTS.put("gus", List.of(...));
        // NPC_QUESTS.put("leah", List.of(...));

        // ABIGAIL
        List<QuestDef> abigail = new ArrayList<>();
        abigail.add(QuestDef.b(1, "Deliver 1 Gold Bar")
                .req(MaterialType.GoldBar, 1)
                .friendPts(1)
                .build());

        abigail.add(QuestDef.b(2, "Deliver 1 Pumpkin")
                .minLv(1) // requires Lv1 friendship
                .req(CropType.PUMPKIN, 1)
                .gold(500)
                .build());

        abigail.add(QuestDef.b(3, "Deliver 50 Wheat")
                .req(CropType.WHEAT, 50)
                .reward(CraftingMachineType.IRIDIUM_SPRINKLER, 1)
                .build());

        NPC_QUESTS.put("abigail", Collections.unmodifiableList(abigail));
    }

    public static List<QuestDef> getForNpc(String npcId) {
        return NPC_QUESTS.getOrDefault(npcId, Collections.emptyList());
    }
}
