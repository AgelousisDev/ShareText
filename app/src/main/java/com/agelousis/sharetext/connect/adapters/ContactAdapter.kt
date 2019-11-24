package com.agelousis.sharetext.connect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.connect.enums.ContactType
import com.agelousis.sharetext.connect.view_holders.ContactViewHolder
import com.agelousis.sharetext.databinding.ContactRowLayoutBinding
import com.agelousis.sharetext.databinding.HeaderRowLayoutBinding
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import com.agelousis.sharetext.connect.view_holders.HeaderViewHolder

class ContactAdapter(private val contactList: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            0 -> HeaderViewHolder(
                HeaderRowLayoutBinding.inflate(inflater)
            )
            1 -> ContactViewHolder(parent.context, ContactRowLayoutBinding.inflate(inflater))
            else -> HeaderViewHolder(
                HeaderRowLayoutBinding.inflate(inflater)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(headerRow = contactList[position] as HeaderRow)
        (holder as? ContactViewHolder)?.bind(item = contactList[position] as ContactType)
    }

    override fun getItemCount(): Int = contactList.size

    override fun getItemViewType(position: Int): Int {
        (contactList.getOrNull(position) as? HeaderRow)?.let { return 0 }
        (contactList.getOrNull(position) as? ContactType)?.let { return 1 }
        return 0
    }

}