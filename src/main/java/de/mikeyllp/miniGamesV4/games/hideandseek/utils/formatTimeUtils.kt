package de.mikeyllp.miniGamesV4.games.hideandseek.utils

object formatTimeUtils {
    // Just a simple method to format the timer in the format HH:MM:SS
    fun formatTimer(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatTimerWithText(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(
            "%02d:%02d:%02d",
            hours.toString() + "h",
            minutes.toString() + "min",
            seconds.toString() + "sek"
        )
    }
}
