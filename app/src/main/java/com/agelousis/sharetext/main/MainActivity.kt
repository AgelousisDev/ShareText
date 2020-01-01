package com.agelousis.sharetext.main

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.custom.dialogs.BasicDialog
import com.agelousis.sharetext.custom.dialogs.BasicDialogType
import com.agelousis.sharetext.main.pager_adapter.MainFragmentPagerAdapter
import com.agelousis.sharetext.main.ui.enumerations.FragmentViewType
import com.agelousis.sharetext.main.ui.enumerations.localizedTitleArray
import com.agelousis.sharetext.main.ui.enumerations.localizedToolbarTitle
import com.agelousis.sharetext.main.ui.share_text.ShareTextFragment
import com.agelousis.sharetext.main.view_models.MainViewModel
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.applyToAll
import com.agelousis.sharetext.utilities.extensions.getCompatColor
import com.agelousis.sharetext.utilities.extensions.initJsonMessageObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_app_bar_layout.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    companion object {
        const val SERVER_HOST_EXTRA = "MainActivity=serverHostExtra"
        const val FRAGMENT_VIEW_TYPE_EXTRA = "MainActivity=fragmentViewType"
        const val REQUEST_CODE = 1
    }

    val serverHost: ServerHost? by lazy { intent?.extras?.getParcelable<ServerHost?>(SERVER_HOST_EXTRA) }
    val fragmentViewType: FragmentViewType? by lazy { intent?.extras?.getSerializable(FRAGMENT_VIEW_TYPE_EXTRA) as? FragmentViewType }
    var mainViewModel: MainViewModel? = null
    var isOnBackground = false

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        if (position == mainViewModel?.newShareTextLiveData?.value?.first ?: -1)
            showBadgeOrClear()
    }

    override fun onBackPressed() {
        when(fragmentViewType) {
            FragmentViewType.VIEW_ALL -> BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.warning_label), text = resources.getString(R.string.exit_share_text_message), icon = R.drawable.ic_exclamation_mark, headerBackgroundColor = ContextCompat.getColor(this@MainActivity, R.color.red)) {
                (supportFragmentManager.fragments.firstOrNull { it is ShareTextFragment } as? ShareTextFragment)?.shareTextViewModel?.outcomeMessageModelString =
                    initJsonMessageObject(connectionState = false, type = Constants.infoMessageType, instantValue = false, body = "")
                setResult(Activity.RESULT_OK)
                this@MainActivity.finish()
            })
            FragmentViewType.VIEW_ONLY_SAVED -> super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setupToolbar()
        configureUI()
        addNewTextObserver()
    }

    private fun setupToolbar() {
        appBar.visibility = if (fragmentViewType == FragmentViewType.VIEW_ALL) View.VISIBLE else View.GONE
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbarTitle.text = serverHost?.hostName ?: fragmentViewType?.localizedToolbarTitle(resources = resources) ?: resources.getString(R.string.share_text_label)
    }

    private fun configureUI() {
        val fragmentViewType = fragmentViewType?.let { it } ?: return
        viewPager.offscreenPageLimit = fragmentViewType.value
        viewPager.adapter = MainFragmentPagerAdapter(fragmentManager = supportFragmentManager, fragmentTitles = fragmentViewType.localizedTitleArray(resources = resources), fragmentViewType = fragmentViewType)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(this)
        tabLayout.visibility = if (fragmentViewType == FragmentViewType.VIEW_ALL) View.VISIBLE else View.GONE
        if (fragmentViewType == FragmentViewType.VIEW_ALL) arrayOf(tabLayout.getTabAt(0), tabLayout.getTabAt(1)).applyToAll {
            it?.orCreateBadge
            it?.badge?.backgroundColor = getCompatColor(color = R.color.colorAccent)
        }
    }

    private fun addNewTextObserver() {
        mainViewModel?.newShareTextLiveData?.observe(this, Observer {
            if (viewPager.currentItem != it.first) showBadgeOrClear(position = it.first, count = it.second)
        })
    }

    private fun showBadgeOrClear(position: Int = 0, count: Int = 0) {
        if (count == 0) {
            tabLayout.getTabAt(0)?.badge?.isVisible = false
            tabLayout.getTabAt(1)?.badge?.isVisible = false
            return
        }
        when(position) {
            0 -> {
                tabLayout.getTabAt(1)?.badge?.isVisible = false
                tabLayout.getTabAt(0)?.badge?.isVisible = true
                tabLayout.getTabAt(0)?.orCreateBadge?.number = count
            }
            1 -> {
                tabLayout.getTabAt(0)?.badge?.isVisible = false
                tabLayout.getTabAt(1)?.badge?.isVisible = true
                tabLayout.getTabAt(1)?.orCreateBadge?.number = count
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isOnBackground = true
    }

    override fun onResume() {
        super.onResume()
        isOnBackground = false
    }

}