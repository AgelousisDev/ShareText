package com.agelousis.sharetext.connect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.connect.enums.ContactType
import com.agelousis.sharetext.connect.view_holders.ContactViewHolder
import com.agelousis.sharetext.connect.view_holders.HeaderViewHolder
import com.agelousis.sharetext.databinding.ContactRowLayoutBinding
import com.agelousis.sharetext.databinding.HeaderRowLayoutBinding

class ContactAdapter(private val contactList: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            0 -> HeaderViewHolder(HeaderRowLayoutBinding.inflate(inflater))
            1 -> ContactViewHolder(parent.context, ContactRowLayoutBinding.inflate(inflater))
            else -> HeaderViewHolder(HeaderRowLayoutBinding.inflate(inflater))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(contactList[position] as String)
        (holder as? ContactViewHolder)?.bind(contactList[position] as ContactType)
    }

    override fun getItemCount(): Int = contactList.size

    override fun getItemViewType(position: Int): Int {
        (contactList.getOrNull(position) as? String)?.let { return 0 }
        (contactList.getOrNull(position) as? ContactType)?.let { return 1 }
        return 0
    }

}