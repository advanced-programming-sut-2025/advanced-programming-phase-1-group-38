package models;

import models.enums.Types.ShopType;

import java.util.List;
import java.util.Map;

public class NPCRegistry  {
    public static final Map<String, NPC> NPCS = Map.ofEntries(
            Map.entry("Sebastian", new NPC(
                    "Sebastian",
                    List.of("Pumpkin Soup", "Pizza"),
                    List.of(
                            new Quest(
                                    List.of("50 Iron", "1 Caved Carrot", "150 Stone"),
                                    List.of("2 Diamonds", "5000g", "50 Quartz")
                            )
                    ),
                    null,
                    null
            )),
            Map.entry("Abigail", new NPC(
                    "Abigail",
                    List.of("Amethyst", "Pumpkin", "Quartz"),
                    List.of(
                            new Quest(
                                    List.of("6 Gold Bars", "1 Cobalt Ore", "50 Weeds"),
                                    List.of("1 Friendship Level", "500g", "1 Iridium Sprinkler")
                            )
                    ),
                    null,
                    null
            )),
            Map.entry("Harvey", new NPC(
                    "Harvey",
                    List.of("Coffee", "Pickles", "Wine"),
                    List.of(
                            new Quest(
                                    List.of("12 of Any Herb", "1 Salmon", "1 Bottle of Wine"),
                                    List.of("750g", "1 Friendship Level", "5 Salad")
                            )
                    ),
                    null,
                    null
            )),
            Map.entry("Leah", new NPC(
                    "Leah",
                    List.of("Salad", "Grape", "Wine"),
                    List.of(
                            new Quest(
                                    List.of("10 Hardwood", "1 Salmon"),
                                    List.of("500g", "1 Cooking Recipe (Salmon Dinner)", "3 Deluxe Scarecrow")
                            )
                    ),
                    null,
                    null
            )),
            Map.entry("Robin", new NPC(
                    "Robin",
                    List.of("Spaghetti", "Wood", "Gold Bar"),
                    List.of(
                            new Quest(
                                    List.of("80 Wood", "10 Iron", "1000 Wood"),
                                    List.of("1000g", "3 Bee House", "2500g")
                            )
                    ),
                    null,
                    null
            ))
    );
}
