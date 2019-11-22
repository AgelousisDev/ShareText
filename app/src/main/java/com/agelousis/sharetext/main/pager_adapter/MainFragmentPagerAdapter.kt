package com.agelousis.sharetext.main.pager_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.agelousis.sharetext.main.ui.notifications.NotificationsFragment
import com.agelousis.sharetext.main.ui.saved.SavedFragment
import com.agelousis.sharetext.main.ui.share_text.ShareTextFragment

class MainFragmentPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 3

    override fun getItem(position: Int): Fragment = when(position) {
        0 -> ShareTextFragment()
        1 -> SavedFragment()
        2 -> NotificationsFragment()
        else -> ShareTextFragment()
    }
}