package com.agelousis.sharetext.connect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.connect.adapters.ContactAdapter
import com.agelousis.sharetext.connect.enums.ContactType
import com.agelousis.sharetext.connect.options_sheet.BottomSheetFragment
import com.agelousis.sharetext.connect.service.ConnectService
import com.agelousis.sharetext.custom.dialogs.BasicDialog
import com.agelousis.sharetext.custom.dialogs.BasicDialogType
import com.agelousis.sharetext.custom.loader_dialog.LoaderDialog
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.main.ui.enumerations.FragmentViewType
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import com.agelousis.sharetext.utilities.*
import com.agelousis.sharetext.utilities.extensions.*
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        setupUI()
        configureRecyclerView()
    }

    private fun setupUI() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.instructions_label),text = resources.getString(R.string.share_text_instructions), icon = R.drawable.ic_info, headerBackgroundColor = ContextCompat.getColor(this@ConnectActivity, R.color.colorAccent)))
        checkSavedValues()

        instructionsIconButton.setOnClickListener { BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.instructions_label),text = resources.getString(R.string.share_text_instructions), icon = R.drawable.ic_info, headerBackgroundColor = ContextCompat.getColor(this@ConnectActivity, R.color.colorAccent))) }
        moreButton.setOnClickListener { BottomSheetFragment.show(supportFragmentManager = supportFragmentManager) }
        connectButton.setOnClickListener {
            startActivity(with(Intent(this@ConnectActivity, MainActivity::class.java)) {
                putExtra(MainActivity.SERVER_HOST_EXTRA, ServerHost(hostName = "TestingChannel"))
                putExtra(MainActivity.FRAGMENT_VIEW_TYPE_EXTRA, FragmentViewType.VIEW_ALL)
                this
            })
            /*isFormValidated {
                if (getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE).areIPAddressesRemembered)
                    saveIPAddress(ipAddress = ipAddressLayout.text)
                LoaderDialog.show(supportFragmentManager = supportFragmentManager)
                ConnectService(ipAddress = ipAddressLayout.text ?: "", port = portLayout.text?.toInt() ?: 0, body = String.format(resources.getString(R.string.device_connected_message), Build.MODEL)) {
                    LoaderDialog.hide(supportFragmentManager = supportFragmentManager)
                    it.whenNull {
                        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.warning_label), text = resources.getString(R.string.connection_failed_label), icon = R.drawable.ic_exclamation_mark, headerBackgroundColor = ContextCompat.getColor(this@ConnectActivity, R.color.red)))
                    }?.otherwise {
                        portLayout.text = ""
                        startActivityForResult(with(Intent(this@ConnectActivity, MainActivity::class.java)) {
                            putExtra(MainActivity.SERVER_HOST_EXTRA, it)
                            putExtra(MainActivity.FRAGMENT_VIEW_TYPE_EXTRA, FragmentViewType.VIEW_ALL)
                            this
                        }, MainActivity.REQUEST_CODE)
                    }
                }.execute()
            }*/
        }

        viewSavedLabel.setOnClickListener { startActivity(with(Intent(this@ConnectActivity, MainActivity::class.java)) {
            this.putExtra(MainActivity.FRAGMENT_VIEW_TYPE_EXTRA, FragmentViewType.VIEW_ONLY_SAVED)
            this
        }) }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.REQUEST_CODE && resultCode == Activity.RESULT_CANCELED)
            BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.warning_label), text = resources.getString(R.string.disconnected_message), icon = R.drawable.ic_exclamation_mark, headerBackgroundColor = ContextCompat.getColor(this@ConnectActivity, R.color.red)))
    }

}
