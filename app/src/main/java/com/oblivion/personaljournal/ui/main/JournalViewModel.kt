package com.oblivion.personaljournal.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.data.repository.JournalRepository
import com.oblivion.personaljournal.utils.Constants.SEARCH_DEBOUNCE_MS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class JournalViewModel
    @Inject
    constructor(
        private val repository: JournalRepository,
    ) : ViewModel() {
        val allEntries: LiveData<List<JournalEntity>> = repository.allEntries.asLiveData()

        private val searchQuery = MutableStateFlow("")

        // Combine search query with all entries
        // Optimized with debounce and distinctUntilChanged to avoid unnecessary database queries
        val searchResults: LiveData<List<JournalEntity>> =
            searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) return@flatMapLatest repository.allEntries

                    repository.searchEntries(query)
                }.asLiveData()

        fun setSearchQuery(query: String) {
            searchQuery.value = query
        }

        suspend fun insert(entry: JournalEntity): Long = repository.insert(entry)

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
