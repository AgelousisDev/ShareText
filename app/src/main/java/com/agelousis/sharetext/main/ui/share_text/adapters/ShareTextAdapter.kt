package com.agelousis.sharetext.main.ui.share_text.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.databinding.EmptyRowLayoutBinding
import com.agelousis.sharetext.databinding.MessageRowLayoutBinding
import com.agelousis.sharetext.main.ui.share_text.enums.ShareTextAdapterViewType
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.main.ui.share_text.view_holders.EmptyViewHolder
import com.agelousis.sharetext.main.ui.share_text.view_holders.MessageViewHolder

class ShareTextAdapter(private val list: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ShareTextAdapterViewType.EMPTY_VIEW.value -> EmptyViewHolder(binding = EmptyRowLayoutBinding.inflate(inflater))
            ShareTextAdapterViewType.MESSAGE_VIEW.value -> MessageViewHolder(binding = MessageRowLayoutBinding.inflate(inflater))
            else -> EmptyViewHolder(binding = EmptyRowLayoutBinding.inflate(inflater))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? EmptyViewHolder)?.bind(emptyRow = list.getOrNull(position) as? EmptyRow ?: return)
        (holder as? MessageViewHolder)?.bind(messageModel = list.getOrNull(position) as? MessageModel)
    }

    override fun getItemViewType(position: Int): Int {
        (list.getOrNull(position) as? EmptyRow)?.let { return ShareTextAdapterViewType.EMPTY_VIEW.value }
        (list.getOrNull(position) as? MessageModel)?.let { return ShareTextAdapterViewType.MESSAGE_VIEW.value }
        return 0
    }

}