package io.github.StardewValley.models;

public class Weather {
    private WeatherType weatherType;

    public Weather(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }
}
