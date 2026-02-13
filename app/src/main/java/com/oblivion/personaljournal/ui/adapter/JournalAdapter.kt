package com.oblivion.personaljournal.ui.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oblivion.personaljournal.R
import com.oblivion.personaljournal.data.entity.JournalEntity
import com.oblivion.personaljournal.databinding.JournalItemBinding
import com.oblivion.personaljournal.utils.DateUtils

class JournalAdapter(
    private val onMenuClick: (JournalEntity, MenuItem) -> Unit,
) : ListAdapter<JournalEntity, JournalAdapter.JournalViewHolder>(JournalDiffCallback()) {
    inner class JournalViewHolder(
        val binding: JournalItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivMenu.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    showPopupMenu(view, getItem(pos))
                }
            }
        }

        fun bind(item: JournalEntity) {
            with(binding) {
                tvTitle.text = item.title
                tvDate.text = DateUtils.dateFormat.format(item.date)
                tvTags.isVisible = item.tags.isNotEmpty()
                tvTags.text = item.tags.joinToString(" ") { "#$it" }
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
        holder.bind(getItem(position))
    }
}

private class JournalDiffCallback : DiffUtil.ItemCallback<JournalEntity>() {
    override fun areItemsTheSame(
        oldItem: JournalEntity,
        newItem: JournalEntity,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: JournalEntity,
        newItem: JournalEntity,
    ): Boolean = oldItem == newItem
}
