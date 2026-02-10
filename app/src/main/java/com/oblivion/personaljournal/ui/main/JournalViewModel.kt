package com.oblivion.personaljournal.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel
    @Inject
    constructor(
        private val repository: JournalRepository,
    ) : ViewModel() {
        val allEntries: LiveData<List<JournalEntity>> = repository.allEntries.asLiveData()

        fun insert(entry: JournalEntity) =
            viewModelScope.launch {
                repository.insert(entry)
            }

        fun update(entry: JournalEntity) =
            viewModelScope.launch {
                repository.update(entry)
            }

        fun delete(entry: JournalEntity) =
            viewModelScope.launch {
                repository.delete(entry)
            }

        fun deleteById(id: Long) =
            viewModelScope.launch {
                repository.deleteById(id)
            }
    }
