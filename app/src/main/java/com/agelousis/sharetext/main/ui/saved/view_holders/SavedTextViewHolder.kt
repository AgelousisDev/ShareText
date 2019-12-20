package com.agelousis.sharetext.main.ui.saved.view_holders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.databinding.SavedTextRowLayoutBinding
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel

class SavedTextViewHolder(private val binding: SavedTextRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(savedMessageModel: SavedMessageModel?) {
        binding.model = savedMessageModel
        binding.executePendingBindings()
    }

}