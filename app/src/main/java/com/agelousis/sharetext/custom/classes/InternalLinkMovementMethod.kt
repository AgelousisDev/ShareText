package com.agelousis.sharetext.custom.classes

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * Set this on a textview and then you can potentially open links locally if applicable
 */
typealias OnLinkClickedBlock = (String) -> Boolean
class InternalLinkMovementMethod(private val onLinkClickedBlock: OnLinkClickedBlock): LinkMovementMethod() {
   override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            var x = event.x
            var y = event.y
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y.toInt())
            val off = layout.getOffsetForHorizontal(line, x)
            val link: Array<URLSpan> = buffer.getSpans(off, off, URLSpan::class.java)
            if (link.isNotEmpty()) {
                val url = link[0].url
                val handled = onLinkClickedBlock(url)
                if (handled) {
                    return true
                }
                return super.onTouchEvent(widget, buffer, event)
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }
}