package com.oblivion.personaljournal.utils

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.oblivion.personaljournal.utils.Constants.MAX_TAGS

object ChipUtils {
    fun extractChipTexts(chipGroup: ChipGroup): List<String> =
        chipGroup.children
            .filterIsInstance<Chip>()
            .map { it.text.toString() }
            .toList()

    fun addChipToGroup(
        context: Context,
        chipGroup: ChipGroup,
        text: String,
    ) {
        val chip =
            Chip(context).apply {
                this.text = text
                isCloseIconVisible = true

                setOnCloseIconClickListener {
                    chipGroup.removeView(this)
                }
            }

        chipGroup.addView(chip)
    }

    fun setupTagInput(
        context: Context,
        editText: EditText,
        chipGroup: ChipGroup,
    ) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text =
                    editText.text
                        ?.toString()
                        ?.trim()
                        .orEmpty()

                if (text.isNotEmpty()) {
                    if (chipGroup.childCount < MAX_TAGS) {
                        addChipToGroup(context, chipGroup, text)
                    } else {
                        Toast.makeText(context, "Vous ne pouvez ajouter que $MAX_TAGS tags.", Toast.LENGTH_SHORT).show()
                    }

                    editText.text?.clear()
                }

                true
            } else {
                false
            }
        }
    }
}
