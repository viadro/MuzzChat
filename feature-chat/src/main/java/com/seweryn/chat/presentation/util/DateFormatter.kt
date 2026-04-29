package com.seweryn.chat.presentation.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class DateFormatter {

    fun toSectionLabel(timeStamp: Long): String {
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeStamp),
            ZoneId.systemDefault()
        )
        return dateTime.format(
            DateTimeFormatter.ofPattern(PATTERN, Locale.getDefault())
        )
    }

    private companion object {

        const val PATTERN = "EEEE HH:mm"
    }
}
