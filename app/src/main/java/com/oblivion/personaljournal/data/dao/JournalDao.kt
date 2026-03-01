package com.oblivion.personaljournal.data.dao

import androidx.room.*
import com.oblivion.personaljournal.data.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries")
    fun getAllEntries(): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity): Long

    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
        SELECT * FROM journal_entries
        WHERE title LIKE '%' || :query || '%'
           OR content LIKE '%' || :query || '%'
           OR tags LIKE '%' || :query || '%'
           OR strftime('%d/%m/%Y', date / 1000, 'unixepoch', 'localtime') LIKE '%' || :query || '%'
        """,
    )
    fun searchEntries(query: String): Flow<List<JournalEntity>>
}
