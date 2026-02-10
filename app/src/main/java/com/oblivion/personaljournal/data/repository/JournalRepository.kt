package com.oblivion.personaljournal.data.repository

import com.oblivion.personaljournal.data.dao.JournalDao
import com.oblivion.personaljournal.data.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(
    private val journalDao: JournalDao,
) {
    val allEntries: Flow<List<JournalEntity>> = journalDao.getAllEntries()

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
