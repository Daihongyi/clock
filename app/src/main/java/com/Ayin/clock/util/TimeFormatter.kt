package com.Ayin.clock.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeFormatter {
    fun formatTimer(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
    }

    fun formatStopwatch(nanos: Long): String {
        val totalMillis = nanos / 1_000_000
        val millis = totalMillis % 1000
        val seconds = (totalMillis / 1000) % 60
        val minutes = (totalMillis / 60_000) % 60
        val hours = totalMillis / 3_600_000

        return if (hours > 0) {
            String.format(Locale.US, "%02d:%02d:%02d.%03d", hours, minutes, seconds, millis)
        } else {
            String.format(Locale.US, "%02d:%02d.%03d", minutes, seconds, millis)
        }
    }

    fun currentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
    }

    fun currentTime(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
}