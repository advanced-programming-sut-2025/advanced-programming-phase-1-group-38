package models.Animals;
import models.enums.Weather;
import models.enums.Seasons;
import models.enums.Types.FishType;

public class Fish {
    FishType name;
    int basePrice;
    Seasons season;
    boolean isLegendary;
    Weather weather;

    public Fish(FishType name, int basePrice, Seasons season,Weather weather, boolean isLegendary) {
        this.name = name;
        this.basePrice = basePrice;
        this.season = season;
        this.isLegendary = isLegendary;
        this.weather = weather;
    }

    public FishType getName() {
        return name;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Seasons getSeason() {
        return season;
    }

    public Weather getWeather() {
        return weather;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public void setName(FishType name) {
        this.name = name;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setSeason(Seasons season) {
        this.season = season;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void setLegendary(boolean legendary) {
        isLegendary = legendary;
    }
}
