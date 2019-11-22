package com.agelousis.sharetext.custom.views

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.agelousis.sharetext.R
import com.agelousis.sharetext.utilities.darken

class BasicButton(context: Context, attrs: AttributeSet): AppCompatButton(context, attrs) {

    var buttonBackgroundColor: String? = null
        set(value) {
            field = value
            value?.let { background?.colorFilter = PorterDuffColorFilter(Color.parseColor(it), PorterDuff.Mode.SRC_IN) }
        }

    init {
        configureButton()
        setupAttributes(attrs = attrs)
    }

    private fun configureButton() {
        setBackgroundResource(R.drawable.basic_button_background)
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> background?.colorFilter = PorterDuffColorFilter(buttonBackgroundColor?.let { Color.parseColor(it).darken } ?: ContextCompat.getColor(context, R.color.colorAccent).darken, PorterDuff.Mode.SRC_IN)
                MotionEvent.ACTION_UP -> background?.colorFilter = PorterDuffColorFilter(buttonBackgroundColor?.let { Color.parseColor(it) } ?: ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
                MotionEvent.ACTION_CANCEL -> background?.colorFilter = PorterDuffColorFilter(buttonBackgroundColor?.let { Color.parseColor(it) } ?: ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
            }
            false
        }
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.BasicButton, 0, 0)
            buttonBackgroundColor = attributes.getString(R.styleable.BasicButton_buttonBackgroundColor)
            attributes.recycle()
        }
    }

}