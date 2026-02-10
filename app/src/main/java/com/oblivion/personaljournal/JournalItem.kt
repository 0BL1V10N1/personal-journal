package com.oblivion.personaljournal

import kotlinx.serialization.Serializable

@Serializable
data class JournalItem(
    var title: String,
    var content: String,
    var date: String,
    val tags: List<String> = emptyList(),
)
