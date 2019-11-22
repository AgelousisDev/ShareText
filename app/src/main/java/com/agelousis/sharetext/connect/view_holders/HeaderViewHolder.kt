package com.agelousis.sharetext.connect.view_holders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.databinding.HeaderRowLayoutBinding

class HeaderViewHolder(private val binding: HeaderRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String) {
        binding.item = item
        binding.executePendingBindings()
    }
}