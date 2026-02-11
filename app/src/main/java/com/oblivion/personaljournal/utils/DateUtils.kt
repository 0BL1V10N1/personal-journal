package com.oblivion.personaljournal.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    /**
     * Date formatter for UI display.
     * Note: SimpleDateFormat is not thread-safe. This should only be used from the main UI thread.
     * All current usage is from UI components (MainActivity and JournalAdapter) on the main thread.
     */
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
}
