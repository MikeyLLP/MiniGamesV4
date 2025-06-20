package de.mikeyllp.miniGamesV4.games.hideandseek.utils;

public class formatTimeUtils {
    // Just a simple method to format the timer in the format HH:MM:SS
    public static String formatTimer(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String formatTimerWithText(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours + "h", minutes + "min", seconds + "sek");
    }
}
