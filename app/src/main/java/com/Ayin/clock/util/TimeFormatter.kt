package com.ayin.clock.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TimeFormatter {
    fun formatTimer(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
    }

    companion object {
        fun formatStopwatch(timeInMillis: Long): String {
            val minutes = timeInMillis / 60000
            val seconds = (timeInMillis % 60000) / 1000
            val milliseconds = timeInMillis % 1000
            return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds)
        }
    }

    fun currentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
    }

    fun currentTime(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
}