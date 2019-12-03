package com.agelousis.sharetext.main.ui.share_text.view_holders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.databinding.MessageRowLayoutBinding
import com.agelousis.sharetext.main.ui.share_text.presenter.MessagePresenter
import com.agelousis.sharetext.main.ui.share_text.view_holders.models.SelectionModel
import com.agelousis.sharetext.utilities.MessageSelectedBlock
import com.agelousis.sharetext.utilities.isLink
import com.agelousis.sharetext.utilities.openWebViewIntent

class MessageViewHolder(private val binding: MessageRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(messageModel: MessageModel?, messageSelectedBlock: MessageSelectedBlock) {
        binding.item = messageModel
        binding.presenter = object: MessagePresenter {
            override fun onMessageClicked(messageModel: MessageModel) {
                messageModel.body?.takeIf { it.isLink }?.apply { binding.root.context?.openWebViewIntent(urlString = this) }
            }
        }
        itemView.tag = SelectionModel(isSelected = false, messageModel = messageModel)
        itemView.setOnLongClickListener {
            (it.tag as? SelectionModel)?.apply {
                when(this.isSelected) {
                    false -> {
                        binding.messageRowCardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                        binding.messageRowTextView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.dayNightTextOnBackground))
                        it.tag = with(this) {
                            this.isSelected = true
                            this
                        }
                        messageSelectedBlock(it.tag as? SelectionModel)
                    }
                    true -> {
                        binding.messageRowCardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorAccent))
                        binding.messageRowTextView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                        it.tag = with(this) {
                            this.isSelected = false
                            this
                        }
                        messageSelectedBlock(it.tag as? SelectionModel)
                    }
                }
            }
            true
        }
        binding.executePendingBindings()
    }

}