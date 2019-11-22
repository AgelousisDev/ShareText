package com.agelousis.sharetext.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.main.pager_adapter.MainFragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.navigationShareText -> viewPager.currentItem = 0
            R.id.navigationSaved -> viewPager.currentItem = 1
            R.id.navigationNotifications -> viewPager.currentItem = 2
        }
        return true
    }
    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> { navView.selectedItemId = R.id.navigationShareText; supportActionBar?.title = resources.getString(R.string.share_text_label) }
            1 -> { navView.selectedItemId = R.id.navigationSaved; supportActionBar?.title = resources.getString(R.string.saved_label) }
            2 -> { navView.selectedItemId = R.id.navigationNotifications; supportActionBar?.title = resources.getString(R.string.title_notifications) }
            else -> navView.selectedItemId = R.id.navigationShareText
        }
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
        supportActionBar?.title = resources.getString(R.string.share_text_label)
    }

    private fun configureUI() {
        viewPager.adapter = MainFragmentPagerAdapter(fragmentManager = supportFragmentManager)
        navView.setOnNavigationItemSelectedListener(this)
        viewPager.addOnPageChangeListener(this)
    }

}
