package com.oblivion.personaljournal.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTagsList(tags: List<String>): String = Json.encodeToString(tags)

    @TypeConverter
    fun toTagsList(tagsString: String): List<String> = Json.decodeFromString(tagsString)

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }
}
