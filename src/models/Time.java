package models;

import models.enums.Seasons;

public class Time {
    private long hour;

    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_SEASON = 28;
    private static final int SEASONS_IN_YEAR = 4;
    private static final int DAYS_IN_YEAR = DAYS_IN_SEASON * SEASONS_IN_YEAR;

    public Time(long hour) {
        this.hour = hour;
    }

    public Time nextHour() {
        return new Time(hour + 1);
    }
    public Time nextDay() {return null;}
    public Time nextSeason() {return null;}

    public long getCurrentDay() {
        return (hour / HOURS_IN_DAY) % DAYS_IN_YEAR + 1;
    }

    public int getCurrentMonth() {
        return (int)(getCurrentDay() / (DAYS_IN_SEASON / 3)) % 12 + 1;
    }

    public Seasons getCurrentSeason() {
        long day = getCurrentDay();
        if (day < DAYS_IN_SEASON) {
            return Seasons.SPRING;
        } else if (day < DAYS_IN_SEASON * 2) {
            return Seasons.SUMMER;
        } else if (day < DAYS_IN_SEASON * 3) {
            return Seasons.FALL;
        } else {
            return Seasons.WINTER;
        }
    }

    public long getHour() {
        return hour;
    }

    public void advance(long hours) {
        hour += hours;
    }

    public void advance(long days, long hours) {
        hour += days * HOURS_IN_DAY + hours;
    }

    public String getCurrentDayOfWeek() {
        long dayOfWeek = ((hour / HOURS_IN_DAY) % 7) + 1;
        switch ((int)dayOfWeek) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
            default: return "Unknown";
        }
    }
}