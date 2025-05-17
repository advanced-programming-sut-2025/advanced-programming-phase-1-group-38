package models;

import models.enums.Seasons;

public class Time {
    private int hour;

    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_SEASON = 28;
    private static final int SEASONS_IN_YEAR = 4;
    private static final int DAYS_IN_YEAR = DAYS_IN_SEASON * SEASONS_IN_YEAR;

    public Time() {
        this.hour = 0;
    }
    public Time(int hour) {
        this.hour = hour;
    }

    public Time nextHour() {
        return new Time(hour + 1);
    }

    public Time nextDay() {
        return new Time(hour + HOURS_IN_DAY);
    }

    public Time nextSeason() {
        return new Time(hour + HOURS_IN_DAY * DAYS_IN_SEASON);
    }

    public int getHourOfDay() {
        return hour % HOURS_IN_DAY;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDayOfYear() {
        return (hour / HOURS_IN_DAY) % DAYS_IN_YEAR + 1;
    }

    public int getDayOfSeason() {
        return (getDayOfYear() - 1) % DAYS_IN_SEASON + 1;
    }

    public int getCurrentYear() {
        return hour / (HOURS_IN_DAY * DAYS_IN_YEAR) + 1;
    }

    public Seasons getCurrentSeason() {
        int day = getDayOfYear();
        if (day <= DAYS_IN_SEASON) {
            return Seasons.SPRING;
        } else if (day <= DAYS_IN_SEASON * 2) {
            return Seasons.SUMMER;
        } else if (day <= DAYS_IN_SEASON * 3) {
            return Seasons.FALL;
        } else {
            return Seasons.WINTER;
        }
    }

    public long getHour() {
        return hour;
    }

    public void advance(int hours) {
        hour += hours;
    }

    public String getCurrentDayOfWeek() {
        int dayOfWeek = (hour / HOURS_IN_DAY) % 7 + 1;
        return switch (dayOfWeek) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Unknown";
        };
    }

    public boolean isBedTime() {
        return getHourOfDay() >= 22;
    }

    public void skipToMorning() {
        int hoursToAdvance = (24 - getHourOfDay()) + 9;
        advance(hoursToAdvance);
    }

    @Override
    public String toString() {
        return "Day " + getDayOfYear() +
            " (" + getDayOfSeason() + " of " + getCurrentSeason() + "), " +
            "Hour: " + getHourOfDay();
    }
}
