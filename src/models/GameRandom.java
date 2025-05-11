package models;

import java.util.List;
import java.util.Random;

public class GameRandom {
    private static final Random random = new Random();

    public static Random getInstance() {
        return random;
    }

    public static double nextDouble() {
        return random.nextDouble();
    }

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static boolean rollChance(double chance) {
        return nextDouble() < chance;
    }

    public static <T> T pickRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        return list.get(nextInt(list.size()));
    }
}
