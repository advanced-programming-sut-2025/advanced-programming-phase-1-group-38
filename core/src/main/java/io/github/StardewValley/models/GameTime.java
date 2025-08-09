package io.github.StardewValley.models;

public class GameTime {
    private int hour = 9;
    private int day = 1;
    private int season = 0;

    public void advanceOneHour() {
        hour++;
        if (hour > 22) {
            startNextDay();
        }
    }

    private void startNextDay() {
        hour = 9;
        day++;
        if (day > 28) {
            day = 1;
            season++;
            if (season > 3) {
                season = 0;
            }
        }
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public int getSeason() {
        return season;
    }

    public String getSeasonName() {
        switch (season) {
            case 0: return "Spring";
            case 1: return "Summer";
            case 2: return "Fall";
            case 3: return "Winter";
            default: return "Unknown";
        }
    }

    public String getFormattedDayAndHour() {
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String dayName = dayNames[(day - 1) % 7];

        int h = hour % 24;                // keep it in [0,23] even if it drifts
        String suffix = (h < 12) ? "AM" : "PM";

        int displayHour = h % 12;
        if (displayHour == 0) displayHour = 12;

        return String.format("%s %d, %d %s", dayName, day, displayHour, suffix);
    }


}
