package com.oblivion.personaljournal.ui.main

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
            .setPositiveButton(R.string.btn_delete) { _, _ ->
                onDelete(entry)
            }.setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    fun showEditDialog(
        entry: JournalEntity,
        snackbarAnchor: View,
    ) {
        val dialogBinding = DialogEditEntryBinding.inflate(LayoutInflater.from(activity))
        var editedDate = entry.date

        with(dialogBinding) {
            etTitle.setText(entry.title)
            etContent.setText(entry.content)
            tvDate.text = DateUtils.formatDateWithEmoji(entry.date)
            entry.tags.forEach { tag ->
                ChipUtils.addChipToGroup(activity, cgTags, tag, true)
            }

            tvDate.setOnClickListener {
                showDatePicker(editedDate) { newDate ->
                    editedDate = newDate
                    tvDate.text = DateUtils.formatDateWithEmoji(newDate)
                }
            }

            ChipUtils.setupTagInput(activity, etTags, cgTags)
        }

        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dialog_edit_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                val updatedEntry =
                    entry.copy(
                        title = dialogBinding.etTitle.text.toString(),
                        content = dialogBinding.etContent.text.toString(),
                        date = editedDate,
                        tags = ChipUtils.extractChipTexts(dialogBinding.cgTags),
                    )

                onUpdate(updatedEntry)

                Snackbar
                    .make(snackbarAnchor, R.string.snackbar_note_updated, Snackbar.LENGTH_SHORT)
                    .show()
            }.setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    fun showDetailDialog(entry: JournalEntity) {
        val dialogBinding =
            DialogDetailEntryBinding.inflate(LayoutInflater.from(activity)).apply {
                tvTitle.text = entry.title
                tvDate.text = DateUtils.formatDate(entry.date)
                tvContent.text = entry.content.ifEmpty { activity.getString(R.string.label_no_content) }

                val hasTags = entry.tags.isNotEmpty()
                tvTagsLabel.isVisible = hasTags
                cgTags.isVisible = hasTags

                if (hasTags) {
                    entry.tags.forEach { tag ->
                        ChipUtils.addChipToGroup(activity, cgTags, tag)
                    }
                }
            }

        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dialog_detail_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_close, null)
            .show()
    }
}
