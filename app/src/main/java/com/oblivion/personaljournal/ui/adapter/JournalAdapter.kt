package com.oblivion.personaljournal.ui.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.oblivion.personaljournal.R
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.databinding.JournalItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class JournalAdapter(
    private val onMenuClick: (JournalEntity, MenuItem) -> Unit,
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {
    private val items = mutableListOf<JournalEntity>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class JournalViewHolder(
        val binding: JournalItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivMenu.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    showPopupMenu(view, items[pos])
                }
            }
        }

        fun bind(item: JournalEntity) {
            with(binding) {
                tvTitle.text = item.title
                tvDate.text = dateFormat.format(item.date)

                if (item.tags.isNotEmpty()) {
                    tvTags.isVisible = true
                    tvTags.text = item.tags.joinToString(" ") { "#$it" }
                } else {
                    tvTags.isVisible = false
                }
            }
        }

        private fun showPopupMenu(
            view: View,
            item: JournalEntity,
        ) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.menu_journal_item, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                onMenuClick(item, menuItem)
                true
            }

            popup.show()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): JournalViewHolder {
        val binding =
            JournalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return JournalViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: JournalViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<JournalEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
