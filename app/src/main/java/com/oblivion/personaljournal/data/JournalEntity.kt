package com.oblivion.personaljournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "journal_entries")
@TypeConverters(Converters::class)
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: Date,
    val tags: List<String> = emptyList(),
)
