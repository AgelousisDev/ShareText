package com.agelousis.sharetext.main.ui.saved.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.R
import com.agelousis.sharetext.contact.view_holders.HeaderViewHolder
import com.agelousis.sharetext.databinding.EmptyRowLayoutBinding
import com.agelousis.sharetext.databinding.HeaderRowLayoutBinding
import com.agelousis.sharetext.databinding.SavedTextRowLayoutBinding
import com.agelousis.sharetext.main.ui.saved.enums.SavedTextAdapterViewType
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel
import com.agelousis.sharetext.main.ui.saved.view_holders.SavedTextViewHolder
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import com.agelousis.sharetext.main.ui.share_text.view_holders.EmptyViewHolder

class SavedTextAdapter(private val list: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            SavedTextAdapterViewType.EMPTY_VIEW.value -> EmptyViewHolder(binding = EmptyRowLayoutBinding.inflate(inflater, parent, false))
            SavedTextAdapterViewType.HEADER_VIEW.value -> HeaderViewHolder(binding = HeaderRowLayoutBinding.inflate(inflater, parent, false))
            SavedTextAdapterViewType.MESSAGE_VIEW.value -> SavedTextViewHolder(binding = SavedTextRowLayoutBinding.inflate(inflater, parent, false))
            else -> EmptyViewHolder(binding = EmptyRowLayoutBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? EmptyViewHolder)?.bind(emptyRow = list.getOrNull(position) as? EmptyRow ?: return)
        (holder as? HeaderViewHolder)?.bind(headerRow = list.getOrNull(position) as? HeaderRow ?: return)
        (holder as? SavedTextViewHolder)?.bind(savedMessageModel = list.getOrNull(position) as? SavedMessageModel)
    }

    override fun getItemViewType(position: Int): Int {
        (list.getOrNull(position) as? EmptyRow)?.let { return SavedTextAdapterViewType.EMPTY_VIEW.value }
        (list.getOrNull(position) as? HeaderRow)?.let { return SavedTextAdapterViewType.HEADER_VIEW.value }
        (list.getOrNull(position) as? SavedMessageModel)?.let { return SavedTextAdapterViewType.MESSAGE_VIEW.value }
        return 0
    }

    fun updateItems() {
        notifyDataSetChanged()
    }

    fun removeItemAndUpdate(context: Context, position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        //notifyItemRangeChanged(position, list.size)

        val uselessHeaderRow = list.filterIsInstance<HeaderRow>().firstOrNull { headerRow ->
            list.filterIsInstance<SavedMessageModel>().all { headerRow.title != it.channel }
        }
        uselessHeaderRow?.let {
            val headerPosition = list.indexOf(it)
            list.removeAt(headerPosition)
            notifyItemRemoved(headerPosition)
            //notifyItemRangeChanged(headerPosition, list.size)
        }
        addEmptyViewIf(emptyRow = EmptyRow(title = context.resources.getString(R.string.empty_saved_texts), icon = R.drawable.ic_empty_list)) {
            list.isEmpty()
        }
    }

    fun addEmptyViewIf(emptyRow: EmptyRow, predicate: () -> Boolean): Boolean {
        if (predicate()) {
            list.add(emptyRow)
            notifyItemInserted(0)
            notifyItemRangeChanged(0, list.size)
        }
        return predicate()
    }

    fun restoreItem(position: Int) = notifyItemChanged(position)


}