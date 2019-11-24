package com.agelousis.sharetext.connect

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.connect.adapters.ContactAdapter
import com.agelousis.sharetext.connect.enums.ContactType
import com.agelousis.sharetext.connect.options_sheet.BottomSheetFragment
import com.agelousis.sharetext.connect.service.ConnectService
import com.agelousis.sharetext.custom.dialogs.BasicDialog
import com.agelousis.sharetext.custom.dialogs.BasicDialogType
import com.agelousis.sharetext.custom.loader_dialog.LoaderDialog
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.areIPAddressesRemembered
import com.agelousis.sharetext.utilities.getLastSavedIPAddress
import com.agelousis.sharetext.utilities.saveIPAddress
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        setupUI()
        configureRecyclerView()
    }

    private fun setupUI() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = with(BasicDialogType.INSTRUCTIONS) { text = resources.getString(R.string.share_text_instructions); icon = R.drawable.ic_info; headerBackgroundColor = ContextCompat.getColor(this@ConnectActivity, R.color.colorAccent); this})
        checkSavedValues()

        instructionsIconButton.setOnClickListener { BasicDialog.show(supportFragmentManager, dialogType = with(BasicDialogType.INSTRUCTIONS) { text = resources.getString(R.string.share_text_instructions); icon = R.drawable.ic_info; this}) }
        moreButton.setOnClickListener { BottomSheetFragment.show(supportFragmentManager = supportFragmentManager) }
        connectButton.setOnClickListener {
            startActivity(Intent(this@ConnectActivity, MainActivity::class.java))
            /*isFormValidated {
                if (getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE).areIPAddressesRemembered)
                    saveIPAddress(ipAddress = ipAddressLayout.text)
                LoaderDialog.show(supportFragmentManager = supportFragmentManager)
                ConnectService(ipAddress = ipAddressLayout.text ?: "", port = portLayout.text?.toInt() ?: 0, body = String.format(resources.getString(R.string.device_connected_message), Build.MODEL)) {
                    when(it) {
                        true -> {
                            LoaderDialog.hide(supportFragmentManager = supportFragmentManager)
                            startActivity(Intent(this@ConnectActivity, MainActivity::class.java))
                        }
                        false -> {
                            LoaderDialog.hide(supportFragmentManager = supportFragmentManager)
                            BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = with(BasicDialogType.WARNING) { text = resources.getString(R.string.connection_failed_label); icon = R.drawable.ic_exclamation_mark; headerBackgroundColor = ContextCompat.getColor(this@ConnectActivity, R.color.red); this})
                        }
                    }
                }.execute()
            }*/
        }
    }

    private fun checkSavedValues() {
        if (getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE).areIPAddressesRemembered)
            ipAddressLayout.text = getLastSavedIPAddress()
    }

    private fun isFormValidated(completionBlock: () -> Unit) {
        when {
            ipAddressLayout.text?.isEmpty() == true -> ipAddressLayout.setErrorMessage(message = resources.getString(R.string.field_required_error))
            portLayout.text?.isEmpty() == true -> portLayout.setErrorMessage(message = resources.getString(R.string.field_required_error))
            else -> completionBlock()
        }
    }

    private fun configureRecyclerView() {
        val adapter = ContactAdapter(contactList = arrayListOf(HeaderRow(title = resources.getString(R.string.contact_developer_header_label)), ContactType.FACEBOOK, ContactType.INSTAGRAM, ContactType.LINKEDIN, ContactType.EMAIL))
        val gridLayoutManager = GridLayoutManager(this@ConnectActivity, 4)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when(position) { 0 -> 4; else -> 1 }
        }
        contactRecyclerView.layoutManager = gridLayoutManager
        contactRecyclerView.adapter = adapter
    }

}
