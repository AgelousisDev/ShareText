package com.agelousis.sharetext.main.ui.share_text.view_holders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.databinding.MessageRowLayoutBinding
import com.agelousis.sharetext.main.ui.share_text.presenter.MessagePresenter
import com.agelousis.sharetext.utilities.isLink
import com.agelousis.sharetext.utilities.openWebViewIntent

class MessageViewHolder(private val binding: MessageRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(messageModel: MessageModel?) {
        binding.item = messageModel
        binding.presenter = object: MessagePresenter {
            override fun onMessageClicked(messageModel: MessageModel) {
                messageModel.body?.takeIf { it.isLink }?.apply { binding.root.context?.openWebViewIntent(urlString = this) }
            }
        }
        binding.executePendingBindings()
    }

}