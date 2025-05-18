package models;

import java.util.*;

public class NPCRegistry {
    private static final Map<String, NPC> npcs = createNPCs();

    private static Map<String, NPC> createNPCs() {
        Map<String, NPC> npcs = new HashMap<>();

        npcs.put("Sebastian", new NPC(
                "Sebastian",
                "Programmer",
                List.of("Pumpkin Pie", "Pizza", "Wool"),
                List.of(
                        "Hey, what's up here?",
                        "I really love pizza!",
                        "Do you have any wool?"
                ),
                List.of(
                        new Quest("Deliver 50 Iron Ore", List.of("50 Iron Ore"), List.of("5 Diamonds"), true, 0, 0),
                        new Quest("Deliver Pumpkin Pie", List.of("1 Pumpkin Pie"), List.of("5000 Gold"), false, 1, 0),
                        new Quest("Deliver 150 Stone", List.of("150 Stone"), List.of("50 Quartz"), false, 2, 8)
                ),
                List.of("Coffee", "Pumpkin Pie", "Gold Ore", "Iron Bar"),
                new Position(5, 2)
        ));

        npcs.put("Abigail", new NPC(
                "Abigail",
                "Miner",
                List.of("Stone", "Iron Ore", "Coffee"),
                List.of(
                        "What a beautiful day!",
                        "I'm searching for rare stones.",
                        "Coffee gives me energy!"
                ),
                List.of(
                        new Quest("Deliver 1 Gold Bar", List.of("1 Gold Bar"), List.of("1 Friendship Level"), true, 0, 0),
                        new Quest("Deliver 1 Cobalt Ore", List.of("1 Cobalt Ore"), List.of("5 Iridium Water"), false, 1, 0),
                        new Quest("Deliver 50 Codium", List.of("50 Codium"), List.of("5000 Gold"), false, 2, 12)
                ),
                List.of("Cake", "Stone", "Coffee", "Iron Bar"),
                new Position(8, 2)
        ));

        npcs.put("Harvey", new NPC(
                "Harvey",
                "Doctor",
                List.of("Coffee", "Wine"),
                List.of(
                        "Would you like some coffee?",
                        "I'm always here to help!",
                        "Enjoying a glass of wine is nice."
                ),
                List.of(
                        new Quest("Deliver 12 Herbs", List.of("12 Herb"), List.of("750 Gold"), true, 0, 0),
                        new Quest("Deliver 1 Salmon", List.of("1 Salmon"), List.of("1 Friendship Level"), false, 1, 0),
                        new Quest("Deliver 5 Salad", List.of("5 Salad"), List.of("5 Salad"), false, 2, 10)
                ),
                List.of("Wine", "Coffee", "Herb", "Salad"),
                new Position(10, 3)
        ));

        npcs.put("Leah", new NPC(
                "Leah",
                "Artist",
                List.of("Salad", "Grape", "Wine"),
                List.of(
                        "I love painting and fresh salads!",
                        "Have you brought me any wine?",
                        "Grapes are so sweet."
                ),
                List.of(
                        new Quest("Deliver 10 Hardwood", List.of("10 Hardwood"), List.of("Cooking Recipe: Salmon Dinner"), true, 0, 0),
                        new Quest("Deliver 1 Salmon", List.of("1 Salmon"), List.of("1 Deluxe Scarecrow"), false, 1, 0),
                        new Quest("Deliver 200 Wood", List.of("200 Wood"), List.of("1500 Gold"), false, 2, 14)
                ),
                List.of("Wine", "Salad", "Grape", "Hardwood"),
                new Position(12, 5)
        ));

        // Robin
        npcs.put("Robin", new NPC(
                "Robin",
                "Carpenter",
                List.of("Spaghetti", "Wood", "Gold Bar"),
                List.of(
                        "I can build anything for you!",
                        "Wood is always useful.",
                        "Do you like spaghetti?"
                ),
                List.of(
                        new Quest("Deliver 80 Wood", List.of("80 Wood"), List.of("500 Gold"), true, 0, 0),
                        new Quest("Deliver 100 Iron Ore", List.of("100 Iron Ore"), List.of("3 Bee House"), false, 1, 0),
                        new Quest("Deliver 200 Wood", List.of("200 Wood"), List.of("2500 Gold"), false, 2, 16)
                ),
                List.of("Wood", "Bee House", "Spaghetti", "Stone"),
                new Position(15, 3)
        ));

        return Collections.unmodifiableMap(npcs);
    }

    public static NPC getNPCByName(String name) {
        return npcs.get(name);
    }

    public static Collection<NPC> getAllNPCs() {
        return npcs.values();
    }
}
