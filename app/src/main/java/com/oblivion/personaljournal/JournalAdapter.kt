package com.oblivion.personaljournal

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.oblivion.personaljournal.databinding.JournalItemBinding

class JournalAdapter(
    private val items: MutableList<JournalItem>,
    private val onLongClick: (Int) -> Unit,
    private val onMenuClick: (JournalItem, MenuItem) -> Unit,
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {
    inner class JournalViewHolder(
        val binding: JournalItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onLongClick(pos)
                }
                true
            }

            binding.ivMenu.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    showPopupMenu(view, items[pos])
                }
            }
        }

        private fun showPopupMenu(
            view: View,
            item: JournalItem,
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
        val item = items[position]

        with(holder.binding) {
            tvTitle.text = item.title
            tvDate.text = item.date
            tvContent.text = item.content
            tvContent.append(item.tags.joinToString())
        }
    }

    override fun getItemCount(): Int = items.size
}
