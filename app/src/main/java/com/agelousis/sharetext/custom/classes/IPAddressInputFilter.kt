package com.agelousis.sharetext.custom.classes

import android.text.InputFilter
import android.text.Spanned

class IPAddressInputFilter: InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (end > start) {
            val resultingTxt = (dest.toString().substring(0, dstart) + source?.subSequence(start, end) + dest.toString().substring(dend))
            if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?".toRegex())) return ""
            else if (resultingTxt.split(Regex("\\.")).dropLastWhile { it.isEmpty() }.any { it.toInt() > 255 }) return ""
        }
        return null
    }
}