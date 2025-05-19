package models.enums.Types;

import models.enums.Seasons;
import models.enums.Weather;

public enum NPCDialogs {
    dialog1(null, 0, null, "Hello there farmer!"),
    dialog2(null, 0, null, "You new around here?"),
    dialog3(null, 0, Weather.RAINY, "The rain is heavy today, isn't it?"),
    dialog4(null, 0, Weather.STORM, "I hope the storm doesn't cause any damage."),
    dialog5(null, 0, Weather.SNOW, "The snow is beautiful, but it makes it hard to get around."),
    dialog6(Seasons.SPRING, 0, null, "Spring is a time for new beginnings."),
    dialog7(Seasons.SUMMER, 0, null, "Summer is the best time for fishing."),
    dialog8(Seasons.FALL, 0, null, "Fall is a time for harvest."),
    dialog9(Seasons.WINTER, 0, null, "Winter is a time for rest."),
    dialog10(null, 1, null, "Oh hey, I didn't see you there."),
    dialog11(null, 1, null, "How's the farm doing?"),
    dialog12(null, 1, Weather.RAINY, "I love the sound of rain on the roof."),
    dialog13(null, 1, Weather.STORM, "The storm sometimes brings out the best in people."),
    dialog14(null, 1, Weather.SNOW, "I love the snow, it's so peaceful."),
    dialog15(Seasons.SPRING, 1, null, "Spring is so beautiful around here."),
    dialog16(Seasons.SUMMER, 1, null, "Ah it's so hot around this time of the year."),
    dialog17(Seasons.FALL, 1, null, "The leaves are so colorful in the fall."),
    dialog18(Seasons.WINTER, 1, null, "Winter is just snow and chill, ain't it?"),
    dialog19(null, 2, null, "Oh hi! Wanna hang out?"),
    dialog20(null, 2, null, "I love spending time with friends."),
    dialog21(null, 2, Weather.RAINY, "Rainy days are perfect for staying inside and reading."),
    dialog22(null, 2, Weather.STORM, "Stormy weather is perfect for cuddling up with a good book."),
    dialog23(null, 2, Weather.SNOW, "Snowy days are perfect for building snowmen."),
    dialog24(Seasons.SPRING, 2, null, "Nice weather we're having, huh?"),
    dialog25(Seasons.SUMMER, 2, null, "Up for a vacation?"),
    dialog26(Seasons.FALL, 2, null, "Fall is a great time for a hike."),
    dialog27(Seasons.WINTER, 2, null, "How about a snowball fight?"),
    dialog28(null, 3, null, "I'm so glad we met."),
    dialog29(null, 3, null, "I love spending time with you."),
    dialog30(null, 3, Weather.RAINY, "Rainy days are perfect for staying inside and watching movies."),
    dialog31(null, 3, Weather.STORM, "Maybe we should stay at my place during the storm."),
    dialog32(null, 3, Weather.SNOW, "Hot chocolate at your place?"),
    dialog33(Seasons.SPRING, 3, null, "Spring is a time for love."),
    dialog34(Seasons.SUMMER, 3, null, "Wanna go to the beach?"),
    dialog35(Seasons.FALL, 3, null, "It's Fall again, don't you feel romantic?"),
    dialog36(Seasons.WINTER, 3, null, "All you need is a warm blanket and a window during a snowy day.");

    private final Seasons season;
    private final int level;
    private final Weather weather;
    private final String dialog;

    NPCDialogs(Seasons season, int level, Weather weather, String dialog) {
        this.season = season;
        this.level = level;
        this.weather = weather;
        this.dialog = dialog;
    }

    public Seasons getSeason() {
        return season;
    }

    public int getLevel() {
        return level;
    }

    public Weather getWeather() {
        return weather;
    }

    public String getDialog() {
        return dialog;
    }
}
