package com.oblivion.personaljournal

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.color.DynamicColors
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.oblivion.personaljournal.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    companion object {
        private const val MAX_TAGS = 5
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JournalAdapter

    private val journalList = mutableListOf<JournalItem>()
    private var selectedDate: String? = null

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this) // Dynamic colors
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupChipInput()
        setupRecyclerView()

        updateButtonState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val json = Json.encodeToString(journalList)
        outState.putString("journalList", json)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        savedInstanceState?.getString("journalList")?.let { json ->
            journalList.addAll(Json.decodeFromString(json))
        }
    }

    private fun setupListeners() {
        binding.root.setOnClickListener {
            binding.titleEditText.clearFocus()
            binding.contentEditText.clearFocus()
            binding.tagEditText.clearFocus()
        }

        binding.dateTextView.setOnClickListener { showDatePicker() }

        binding.titleEditText.addTextChangedListener { updateButtonState() }

        binding.saveButton.setOnClickListener { addItem() }
    }

    private fun setupChipInput() {
        binding.tagEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text =
                    binding.tagEditText.text
                        .toString()
                        .trim()

                if (text.isNotEmpty()) {
                    if (binding.tagChipGroup.childCount < MAX_TAGS) {
                        addChip(text)
                    } else {
                        Toast.makeText(this, "Vous ne pouvez ajouter que $MAX_TAGS tags.", Toast.LENGTH_SHORT).show()
                    }

                    binding.tagEditText.text?.clear()
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
                journalList,
                onLongClick = { position ->
                    showDeleteDialog(position)
                },
                onMenuClick = { item, menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_edit -> Toast.makeText(this, "Modifier ${item.title}", Toast.LENGTH_SHORT).show()
                        R.id.menu_delete -> Toast.makeText(this, "Supprimer ${item.title}", Toast.LENGTH_SHORT).show()
                        R.id.menu_detail -> Toast.makeText(this, "Détails ${item.title}", Toast.LENGTH_SHORT).show()
                    }
                },
            )

        binding.journalRecyclerView.adapter = adapter
        binding.journalRecyclerView.layoutManager =
            LinearLayoutManager(this)
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker() {
        val picker =
            MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(getString(R.string.date_picker_title))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val calendar =
                Calendar.getInstance().apply {
                    timeInMillis = selection
                }

            selectedDate = dateFormat.format(calendar.time)
            binding.dateTextView.text = "📅 $selectedDate"

            updateButtonState()
        }

        picker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun showDeleteDialog(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Supprimer la note ?")
            .setMessage("Voulez-vous vraiment supprimer cette note ?")
            .setPositiveButton("Supprimer") { dialog, _ ->
                journalList.removeAt(position)
                adapter.notifyItemRemoved(position)
                dialog.dismiss()
            }.setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun addChip(text: String) {
        val chip =
            Chip(this).apply {
                this.text = text
                isCloseIconVisible = true
                setOnCloseIconClickListener { binding.tagChipGroup.removeView(this) }
            }

        binding.tagChipGroup.addView(chip)
    }

    private fun addItem() {
        val existingTags =
            binding.tagChipGroup.children
                .filterIsInstance<Chip>()
                .map { it.text.toString() }
                .toList()

        val item =
            JournalItem(
                title = binding.titleEditText.text.toString(),
                content = binding.contentEditText.text.toString(),
                date = selectedDate.orEmpty(),
                tags = existingTags,
            )

        journalList.add(item)
        val position = journalList.lastIndex
        adapter.notifyItemInserted(position)

        clearInputs()

        Snackbar
            .make(binding.root, getString(R.string.snackbar_note_added), Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbar_undo) {
                journalList.removeAt(position)
                adapter.notifyItemRemoved(position)
            }.show()
    }

    private fun clearInputs() {
        binding.titleEditText.text?.clear()
        binding.contentEditText.text?.clear()
        binding.tagChipGroup.removeAllViews()
        selectedDate = null
        binding.dateTextView.text = getString(R.string.date_text_view_hint)

        updateButtonState()
    }

    private fun updateButtonState() {
        binding.saveButton.isEnabled =
            binding.titleEditText.text
                .toString()
                .isNotBlank() &&
            selectedDate != null
    }
}
