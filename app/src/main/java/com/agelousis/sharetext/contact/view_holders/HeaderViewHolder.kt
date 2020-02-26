package com.agelousis.sharetext.contact.view_holders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.databinding.HeaderRowLayoutBinding
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow

class HeaderViewHolder(private val binding: HeaderRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(headerRow: HeaderRow) {
        binding.headerItem = headerRow
        binding.executePendingBindings()
    }

}