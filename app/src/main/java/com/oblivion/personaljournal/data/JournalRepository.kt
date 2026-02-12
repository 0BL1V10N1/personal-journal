package com.oblivion.personaljournal.data

import kotlinx.coroutines.flow.Flow

class JournalRepository(
    private val journalDao: JournalDao,
) {
    val allEntries: Flow<List<JournalEntity>> = journalDao.getAllEntries()

    fun searchEntries(query: String): Flow<List<JournalEntity>> = journalDao.searchEntries(query)

    suspend fun insert(entry: JournalEntity): Long = journalDao.insert(entry)

    suspend fun update(entry: JournalEntity) {
        journalDao.update(entry)
    }

    suspend fun delete(entry: JournalEntity) {
        journalDao.delete(entry)
    }

    suspend fun deleteById(id: Long) {
        journalDao.deleteById(id)
    }
}
