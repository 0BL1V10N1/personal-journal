package com.oblivion.personaljournal.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatDateWithEmoji(date: Date): String = "📅 ${dateFormat.format(date)}"
}
