package com.oblivion.personaljournal.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.color.DynamicColors
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.oblivion.personaljournal.R
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.databinding.ActivityMainBinding
import com.oblivion.personaljournal.databinding.DialogDetailEntryBinding
import com.oblivion.personaljournal.databinding.DialogEditEntryBinding
import com.oblivion.personaljournal.ui.adapter.JournalAdapter
import com.oblivion.personaljournal.utils.Constants.MAX_TAGS
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JournalAdapter

    private val viewModel: JournalViewModel by viewModels()

    private var selectedDate: Date? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupChipInput()
        setupRecyclerView()
        observeData()

        updateButtonState()
    }

    private fun setupListeners() {
        binding.root.setOnClickListener {
            binding.etTitle.clearFocus()
            binding.etContent.clearFocus()
            binding.etTag.clearFocus()
        }

        binding.tvDate.setOnClickListener { showDatePicker() }

        binding.etTitle.addTextChangedListener { updateButtonState() }

        binding.btnSave.setOnClickListener { addItem() }
    }

    private fun setupChipInput() {
        binding.etTag.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text =
                    binding.etTag.text
                        ?.toString()
                        ?.trim()
                        .orEmpty()

                if (text.isNotEmpty()) {
                    val cg = binding.cgTags
                    if (cg.childCount < MAX_TAGS) {
                        addChipToGroup(cg, text)
                    } else {
                        Toast.makeText(this, "Vous ne pouvez ajouter que $MAX_TAGS tags.", Toast.LENGTH_SHORT).show()
                    }

                    binding.etTag.text?.clear()
                }

                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter =
            JournalAdapter(
                onMenuClick = { item, menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_edit -> showEditDialog(item)
                        R.id.menu_delete -> showDeleteDialog(item)
                        R.id.menu_detail -> showDetailDialog(item)
                    }
                },
            )

        binding.rvJournal.adapter = adapter
        binding.rvJournal.layoutManager = LinearLayoutManager(this)
    }

    private fun observeData() {
        viewModel.allEntries.observe(this) { entries ->
            adapter.submitList(entries)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(
        initialDate: Date? = null,
        onDateSelected: ((Date) -> Unit)? = null,
    ) {
        val picker =
            MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(getString(R.string.date_picker_title))
                .setSelection(initialDate?.time ?: MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val date = Date(selection)

            if (onDateSelected != null) {
                onDateSelected(date)
            } else {
                selectedDate = date
                binding.tvDate.text = "📅 ${dateFormat.format(date)}"
                updateButtonState()
            }
        }

        picker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun showDeleteDialog(entry: JournalEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setPositiveButton(R.string.btn_delete) { dialog, _ ->
                viewModel.delete(entry)
                dialog.dismiss()
            }.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showEditDialog(entry: JournalEntity) {
        val dialogBinding = DialogEditEntryBinding.inflate(LayoutInflater.from(this))
        var editedDate = entry.date

        dialogBinding.etTitle.setText(entry.title)
        dialogBinding.etContent.setText(entry.content)
        dialogBinding.tvDate.text = "📅 ${dateFormat.format(entry.date)}"
        entry.tags.forEach { tag ->
            addChipToGroup(dialogBinding.cgTags, tag)
        }

        dialogBinding.tvDate.setOnClickListener {
            showDatePicker(editedDate) { newDate ->
                editedDate = newDate
                dialogBinding.tvDate.text = "📅 ${dateFormat.format(newDate)}"
            }
        }

        dialogBinding.etTags.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val tagText =
                    dialogBinding.etTags.text
                        ?.toString()
                        ?.trim()
                        .orEmpty()

                if (tagText.isNotEmpty()) {
                    val cg = dialogBinding.cgTags
                    if (cg.childCount < MAX_TAGS) {
                        addChipToGroup(cg, tagText)
                        dialogBinding.etTags.text?.clear()
                    } else {
                        Toast.makeText(this, "Vous ne pouvez ajouter que $MAX_TAGS tags.", Toast.LENGTH_SHORT).show()
                    }
                }

                true
            } else {
                false
            }
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_edit_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { dialog, _ ->
                val newTags =
                    dialogBinding.cgTags.children
                        .filterIsInstance<Chip>()
                        .map { it.text.toString() }
                        .toList()

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

                viewModel.update(updatedEntry)

                Snackbar
                    .make(binding.root, getString(R.string.snackbar_note_updated), Snackbar.LENGTH_SHORT)
                    .show()

                dialog.dismiss()
            }.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun showDetailDialog(entry: JournalEntity) {
        val dialogBinding = DialogDetailEntryBinding.inflate(LayoutInflater.from(this))

        dialogBinding.tvTitle.text = entry.title
        dialogBinding.tvDate.text = dateFormat.format(entry.date)
        dialogBinding.tvContent.text = entry.content.ifEmpty { getString(R.string.label_no_content) }

        if (entry.tags.isNotEmpty()) {
            dialogBinding.tvTagsLabel.isVisible = true
            dialogBinding.cgTags.isVisible = true
            entry.tags.forEach { tag ->
                val chip =
                    Chip(this).apply {
                        text = tag
                        isClickable = false
                    }
                dialogBinding.cgTags.addView(chip)
            }
        } else {
            dialogBinding.tvTagsLabel.isVisible = false
            dialogBinding.cgTags.isVisible = false
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_detail_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_close) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun addChipToGroup(
        chipGroup: ChipGroup,
        text: String,
    ) {
        val chip =
            Chip(this).apply {
                this.text = text
                isCloseIconVisible = true

                setOnCloseIconClickListener {
                    chipGroup.removeView(this)
                }
            }

        chipGroup.addView(chip)
    }

    private fun addItem() {
        val existingTags =
            binding.cgTags.children
                .filterIsInstance<Chip>()
                .map { it.text.toString() }
                .toList()

        val entry =
            JournalEntity(
                title =
                    binding.etTitle.text
                        ?.toString()
                        .orEmpty(),
                content =
                    binding.etContent.text
                        ?.toString()
                        .orEmpty(),
                date = selectedDate!!,
                tags = existingTags,
            )

        viewModel.insert(entry)

        clearInputs()

        Snackbar
            .make(binding.root, getString(R.string.snackbar_note_added), Snackbar.LENGTH_LONG)
            .show()
    }

    private fun clearInputs() {
        binding.etTitle.text?.clear()
        binding.etContent.text?.clear()
        binding.cgTags.removeAllViews()
        selectedDate = null
        binding.tvDate.text = getString(R.string.date_text_view_hint)

        updateButtonState()
    }

    private fun updateButtonState() {
        binding.btnSave.isEnabled =
            !(
                binding.etTitle.text
                    ?.toString()
                    .isNullOrBlank()
            ) &&
            selectedDate != null
    }
}
