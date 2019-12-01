package com.agelousis.sharetext.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.custom.dialogs.BasicDialog
import com.agelousis.sharetext.custom.dialogs.BasicDialogType
import com.agelousis.sharetext.main.pager_adapter.MainFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_app_bar_layout.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val SERVER_HOST_EXTRA = "MainActivity=serverHostExtra"
    }

    private val serverHost: ServerHost? by lazy { intent?.extras?.getParcelable<ServerHost?>(SERVER_HOST_EXTRA) }

    override fun onBackPressed() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.warning_label), text = resources.getString(R.string.exit_share_text_message), icon = R.drawable.ic_exclamation_mark, headerBackgroundColor = ContextCompat.getColor(this@MainActivity, R.color.red)) { super.onBackPressed() })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        configureUI()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbarTitle.text = serverHost?.hostName ?: resources.getString(R.string.share_text_label)
    }

    private fun configureUI() {
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = MainFragmentPagerAdapter(fragmentManager = supportFragmentManager, fragmentTitles = arrayOf(resources.getString(R.string.share_text_label), resources.getString(R.string.saved_label)))
        tabLayout.setupWithViewPager(viewPager)
    }

}
