package com.oblivion.personaljournal.ui.main

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.oblivion.personaljournal.R
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.databinding.DialogDetailEntryBinding
import com.oblivion.personaljournal.databinding.DialogEditEntryBinding
import com.oblivion.personaljournal.utils.ChipUtils
import com.oblivion.personaljournal.utils.DateUtils
import java.util.Date

class JournalDialogHelper(
    private val activity: AppCompatActivity,
    private val onUpdate: (JournalEntity) -> Unit,
    private val onDelete: (JournalEntity) -> Unit,
) {
    fun showDatePicker(
        initialDate: Date? = null,
        onDateSelected: (Date) -> Unit,
    ) {
        val picker =
            MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.date_picker_title)
                .setSelection(initialDate?.time ?: MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        picker.addOnPositiveButtonClickListener { selection ->
            onDateSelected(Date(selection))
        }

        picker.show(activity.supportFragmentManager, "MATERIAL_DATE_PICKER")
    }

    fun showDeleteDialog(entry: JournalEntity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setPositiveButton(R.string.btn_delete) { dialog, _ ->
                onDelete(entry)
                dialog.dismiss()
            }.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    fun showEditDialog(
        entry: JournalEntity,
        snackbarAnchor: View,
    ) {
        val dialogBinding = DialogEditEntryBinding.inflate(LayoutInflater.from(activity))
        var editedDate = entry.date

        dialogBinding.etTitle.setText(entry.title)
        dialogBinding.etContent.setText(entry.content)
        dialogBinding.tvDate.text = DateUtils.formatDateWithEmoji(entry.date)
        entry.tags.forEach { tag ->
            ChipUtils.addChipToGroup(activity, dialogBinding.cgTags, tag)
        }

        dialogBinding.tvDate.setOnClickListener {
            showDatePicker(editedDate) { newDate ->
                editedDate = newDate
                dialogBinding.tvDate.text = DateUtils.formatDateWithEmoji(newDate)
            }
        }

        ChipUtils.setupTagInput(activity, dialogBinding.etTags, dialogBinding.cgTags)

        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dialog_edit_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { dialog, _ ->
                val newTags = ChipUtils.extractChipTexts(dialogBinding.cgTags)

                val updatedEntry =
                    entry.copy(
                        title =
                            dialogBinding.etTitle.text
                                ?.toString()
                                .orEmpty(),
                        content =
                            dialogBinding.etContent.text
                                ?.toString()
                                .orEmpty(),
                        date = editedDate,
                        tags = newTags,
                    )

                onUpdate(updatedEntry)

                Snackbar
                    .make(snackbarAnchor, R.string.snackbar_note_updated, Snackbar.LENGTH_SHORT)
                    .show()

                dialog.dismiss()
            }.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    fun showDetailDialog(entry: JournalEntity) {
        val dialogBinding = DialogDetailEntryBinding.inflate(LayoutInflater.from(activity))

        dialogBinding.tvTitle.text = entry.title
        dialogBinding.tvDate.text = DateUtils.dateFormat.format(entry.date)
        dialogBinding.tvContent.text =
            entry.content.ifEmpty { activity.getString(R.string.label_no_content) }

        if (entry.tags.isNotEmpty()) {
            dialogBinding.tvTagsLabel.isVisible = true
            dialogBinding.cgTags.isVisible = true
            entry.tags.forEach { tag ->
                val chip =
                    Chip(activity).apply {
                        text = tag
                        isClickable = false
                    }
                dialogBinding.cgTags.addView(chip)
            }
        } else {
            dialogBinding.tvTagsLabel.isVisible = false
            dialogBinding.cgTags.isVisible = false
        }

        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dialog_detail_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_close) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
