package com.oblivion.personaljournal.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
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

        private val searchQuery = MutableLiveData("")

        val searchResults: LiveData<List<JournalEntity>> =
            searchQuery.switchMap { query ->
                if (query.isNullOrBlank()) {
                    repository.allEntries.asLiveData()
                } else {
                    repository.searchEntries(query).asLiveData()
                }
            }

        fun setSearchQuery(query: String) {
            searchQuery.value = query
        }

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
