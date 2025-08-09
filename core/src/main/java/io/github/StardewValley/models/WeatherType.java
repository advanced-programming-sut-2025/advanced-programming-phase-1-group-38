package io.github.StardewValley.models;

public enum WeatherType {
    SUNNY("weather/sunny.png"),
    RAINY("weather/rainy.png"),
    STORMY("weather/stormy.png"),
    SNOWY("weather/snowy.png");

    private final String iconPath;

    WeatherType(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }
}
