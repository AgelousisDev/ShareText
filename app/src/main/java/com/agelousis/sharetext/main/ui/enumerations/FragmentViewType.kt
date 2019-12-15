package com.agelousis.sharetext.main.ui.enumerations

import android.content.res.Resources
import com.agelousis.sharetext.R

enum class FragmentViewType(val value: Int) {
    VIEW_ALL(2), VIEW_ONLY_SAVED(1)
}

fun FragmentViewType.localizedToolbarTitle(resources: Resources): String = when(this) {
    FragmentViewType.VIEW_ALL -> resources.getString(R.string.share_text_label)
    FragmentViewType.VIEW_ONLY_SAVED -> resources.getString(R.string.saved_label)
}

fun FragmentViewType.localizedTitleArray(resources: Resources): Array<String> = when(this) {
        FragmentViewType.VIEW_ALL -> arrayOf(resources.getString(R.string.share_text_label), resources.getString(R.string.saved_label))
        FragmentViewType.VIEW_ONLY_SAVED -> arrayOf(resources.getString(R.string.saved_label))
    }