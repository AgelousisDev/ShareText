package com.agelousis.sharetext.main.ui.saved.view_holders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.databinding.SavedTextRowLayoutBinding
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel
import com.agelousis.sharetext.main.ui.saved.presenter.SavedTextPresenter
import com.agelousis.sharetext.utilities.extensions.isLink
import com.agelousis.sharetext.utilities.extensions.openWebViewIntent

class SavedTextViewHolder(private val binding: SavedTextRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(savedMessageModel: SavedMessageModel?) {
        binding.model = savedMessageModel
        binding.presenter = object: SavedTextPresenter {
            override fun onSavedTextClicked(savedMessageModel: SavedMessageModel) {
                savedMessageModel.text.takeIf { it.isLink }?.apply { binding.root.context?.openWebViewIntent(urlString = this) }
            }
        }
        binding.executePendingBindings()
    }

}