package com.oblivion.personaljournal.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
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
import com.oblivion.personaljournal.utils.ChipUtils
import com.oblivion.personaljournal.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JournalAdapter
    private lateinit var searchAdapter: JournalAdapter

    private val viewModel: JournalViewModel by viewModels()

    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupListeners()
        setupChipInput()
        setupRecyclerView()
        setupSearch()
        observeData()

        updateButtonState()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                top = insets.top,
                bottom = insets.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
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
        ChipUtils.setupTagInput(this, binding.etTag, binding.cgTags)
    }

    private fun setupRecyclerView() {
        adapter = createJournalAdapter(hideSearchOnEdit = false)
        binding.rvJournal.adapter = adapter
        binding.rvJournal.layoutManager = LinearLayoutManager(this)

        searchAdapter = createJournalAdapter(hideSearchOnEdit = true)
        binding.rvSearchResults.adapter = searchAdapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
    }

    private fun createJournalAdapter(hideSearchOnEdit: Boolean): JournalAdapter {
        return JournalAdapter(
            onMenuClick = { item, menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        if (hideSearchOnEdit) {
                            binding.svSearch.hide()
                        }
                        showEditDialog(item)
                    }
                    R.id.menu_delete -> showDeleteDialog(item)
                    R.id.menu_detail -> showDetailDialog(item)
                }
            },
        )
    }

    private fun setupSearch() {
        binding.svSearch.setupWithSearchBar(binding.sbSearch)

        binding.svSearch.editText.addTextChangedListener { text ->
            viewModel.setSearchQuery(text?.toString().orEmpty())
        }

        binding.svSearch.editText.setOnEditorActionListener { _, _, _ ->
            binding.sbSearch.setText(binding.svSearch.text)
            false
        }
    }

    private fun observeData() {
        viewModel.allEntries.observe(this) { entries ->
            adapter.submitList(entries)
        }

        viewModel.searchResults.observe(this) { entries ->
            searchAdapter.submitList(entries)
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
                .setTitleText(R.string.date_picker_title)
                .setSelection(initialDate?.time ?: MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val date = Date(selection)

            if (onDateSelected != null) {
                onDateSelected(date)
            } else {
                selectedDate = date
                binding.tvDate.text = "📅 ${DateUtils.dateFormat.format(date)}"
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
        dialogBinding.tvDate.text = "📅 ${DateUtils.dateFormat.format(entry.date)}"
        entry.tags.forEach { tag ->
            ChipUtils.addChipToGroup(this, dialogBinding.cgTags, tag)
        }

        dialogBinding.tvDate.setOnClickListener {
            showDatePicker(editedDate) { newDate ->
                editedDate = newDate
                dialogBinding.tvDate.text = "📅 ${DateUtils.dateFormat.format(newDate)}"
            }
        }

        ChipUtils.setupTagInput(this, dialogBinding.etTags, dialogBinding.cgTags)

        MaterialAlertDialogBuilder(this)
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

                viewModel.update(updatedEntry)

                Snackbar
                    .make(binding.root, R.string.snackbar_note_updated, Snackbar.LENGTH_SHORT)
                    .show()

                dialog.dismiss()
            }.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun showDetailDialog(entry: JournalEntity) {
        val dialogBinding = DialogDetailEntryBinding.inflate(LayoutInflater.from(this))

        dialogBinding.tvTitle.text = entry.title
        dialogBinding.tvDate.text = DateUtils.dateFormat.format(entry.date)
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

    private fun addItem() {
        val existingTags = ChipUtils.extractChipTexts(binding.cgTags)

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
            .make(binding.root, R.string.snackbar_note_added, Snackbar.LENGTH_LONG)
            .setAction(R.string.btn_cancel) {
                viewModel.deleteById(entry.id)
            }.show()
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
