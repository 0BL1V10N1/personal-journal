package com.oblivion.personaljournal.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatDate(date: Date): String = dateFormat.format(date)

    fun formatDateWithEmoji(date: Date): String = "📅 ${formatDate(date)}"
}
