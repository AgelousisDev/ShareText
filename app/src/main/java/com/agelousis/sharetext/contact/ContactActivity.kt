package com.agelousis.sharetext.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.contact.adapters.ContactAdapter
import com.agelousis.sharetext.contact.enums.ContactType
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import kotlinx.android.synthetic.main.activity_connect.contactRecyclerView
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity() {

    private val listItems by lazy { arrayListOf(HeaderRow(title = resources.getString(R.string.project_label)), ContactType.GITHUB, HeaderRow(title = resources.getString(R.string.contact_developer_header_label)), ContactType.FACEBOOK, ContactType.INSTAGRAM, ContactType.LINKEDIN, ContactType.EMAIL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        configureToolbar()
        configureRecyclerView()
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun configureRecyclerView() {
        val adapter = ContactAdapter(contactList = listItems)
        val gridLayoutManager = GridLayoutManager(this@ContactActivity, 4)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when(listItems.getOrNull(position)) {
                is HeaderRow -> 4
                is ContactType -> 1
                else -> 1
            }
        }
        contactRecyclerView.layoutManager = gridLayoutManager
        contactRecyclerView.adapter = adapter
    }

}
