package com.agelousis.sharetext.main.ui.share_text.view_holders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.databinding.EmptyRowLayoutBinding
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow

class EmptyViewHolder(private val binding: EmptyRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(emptyRow: EmptyRow) {
        binding.item = emptyRow
        binding.executePendingBindings()
    }

}