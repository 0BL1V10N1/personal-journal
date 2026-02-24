package com.oblivion.personaljournal.utils

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.oblivion.personaljournal.R
import com.oblivion.personaljournal.utils.Constants.MAX_TAGS

object ChipUtils {
    fun setupTagInput(
        context: Context,
        editText: EditText,
        chipGroup: ChipGroup,
    ) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) return@setOnEditorActionListener false

            val text =
                editText.text
                    ?.toString()
                    ?.trim()
                    .orEmpty()
            if (text.isEmpty()) return@setOnEditorActionListener true

            if (chipGroup.childCount < MAX_TAGS) {
                addChipToGroup(context, chipGroup, text, true)
            } else {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.toast_max_tags, MAX_TAGS),
                        Toast.LENGTH_SHORT,
                    ).show()
            }

            editText.text?.clear()

            true
        }
    }

    fun addChipToGroup(
        context: Context,
        chipGroup: ChipGroup,
        text: String,
        isCloseable: Boolean = false,
    ) {
        val chip =
            Chip(context).apply {
                this.text = text
                isCloseIconVisible = isCloseable
                isClickable = isCloseable

                if (isCloseable) {
                    setOnCloseIconClickListener { chipGroup.removeView(this) }
                }
            }

        chipGroup.addView(chip)
    }

    fun extractChipTexts(chipGroup: ChipGroup): List<String> =
        chipGroup.children
            .filterIsInstance<Chip>()
            .map { it.text.toString() }
            .toList()
}
