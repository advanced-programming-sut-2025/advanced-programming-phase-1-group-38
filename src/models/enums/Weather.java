package models.enums;

import models.GameRandom;
import java.util.List;

public enum Weather {
    SUNNY,
    RAINY,
    STORM,
    SNOW;

    public static Weather getRandom(Seasons season) {
        return switch (season) {
            case SPRING, FALL -> GameRandom.pickRandom(List.of(SUNNY, RAINY, STORM));
            case SUMMER -> GameRandom.pickRandom(List.of(SUNNY, SUNNY, RAINY, STORM));
            case WINTER -> GameRandom.pickRandom(List.of(SNOW, SUNNY));
            case SPECIAL -> GameRandom.pickRandom(List.of(SUNNY, RAINY, STORM, SNOW));
        };
    }
}
