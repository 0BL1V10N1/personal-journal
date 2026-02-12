package com.oblivion.personaljournal.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oblivion.personaljournal.R
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.databinding.ActivityMainBinding
import com.oblivion.personaljournal.ui.adapter.JournalAdapter
import com.oblivion.personaljournal.utils.ChipUtils
import com.oblivion.personaljournal.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JournalAdapter
    private lateinit var searchAdapter: JournalAdapter
    private lateinit var dialogHelper: JournalDialogHelper

    private val viewModel: JournalViewModel by viewModels()

    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogHelper =
            JournalDialogHelper(
                activity = this,
                onUpdate = { viewModel.update(it) },
                onDelete = { viewModel.delete(it) },
            )

        setupWindowInsets()
        setupListeners()
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

        binding.tvDate.setOnClickListener {
            dialogHelper.showDatePicker { date ->
                selectedDate = date
                binding.tvDate.text = DateUtils.formatDateWithEmoji(date)
                updateButtonState()
            }
        }

        binding.etTitle.addTextChangedListener { updateButtonState() }

        binding.btnSave.setOnClickListener { addItem() }

        ChipUtils.setupTagInput(this, binding.etTag, binding.cgTags)
    }

    private fun setupRecyclerView() {
        adapter =
            JournalAdapter(
                onMenuClick = { item, menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_edit -> dialogHelper.showEditDialog(item, binding.root)
                        R.id.menu_delete -> dialogHelper.showDeleteDialog(item)
                        R.id.menu_detail -> dialogHelper.showDetailDialog(item)
                    }
                },
            )

        binding.rvJournal.adapter = adapter
        binding.rvJournal.layoutManager = LinearLayoutManager(this)

        searchAdapter =
            JournalAdapter(
                onMenuClick = { item, menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_edit -> {
                            binding.svSearch.hide()
                            dialogHelper.showEditDialog(item, binding.root)
                        }

                        R.id.menu_delete -> dialogHelper.showDeleteDialog(item)
                        R.id.menu_detail -> dialogHelper.showDetailDialog(item)
                    }
                },
            )

        binding.rvSearchResults.adapter = searchAdapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
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

        clearInputs()

        lifecycleScope.launch {
            val insertedId = viewModel.insert(entry)

            Snackbar
                .make(binding.root, R.string.snackbar_note_added, Snackbar.LENGTH_LONG)
                .setAction(R.string.btn_cancel) {
                    viewModel.deleteById(insertedId)
                }.show()
        }
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
