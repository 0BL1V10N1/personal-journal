package com.oblivion.personaljournal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.oblivion.personaljournal.data.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries")
    fun getAllEntries(): Flow<List<JournalEntity>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): JournalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity): Long

    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM journal_entries WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchEntries(query: String): Flow<List<JournalEntity>>
}
