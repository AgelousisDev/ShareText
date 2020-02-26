package com.agelousis.sharetext.main.pager_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.agelousis.sharetext.main.ui.enumerations.FragmentViewType
import com.agelousis.sharetext.main.ui.saved.SavedFragment
import com.agelousis.sharetext.main.ui.share_text.ShareTextFragment

class MainFragmentPagerAdapter(fragmentManager: FragmentManager, private val fragmentTitles: Array<String>, private val fragmentViewType: FragmentViewType): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = fragmentViewType.value

    override fun getItem(position: Int): Fragment = when(fragmentViewType) {
        FragmentViewType.VIEW_ALL -> when(position) {
            0 -> ShareTextFragment()
            1 -> SavedFragment()
            else -> ShareTextFragment()
        }
        FragmentViewType.VIEW_ONLY_SAVED -> SavedFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitles[position]

}