package com.agelousis.sharetext.custom.views

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.agelousis.sharetext.R
import com.agelousis.sharetext.custom.classes.IPAddressInputFilter
import kotlinx.android.synthetic.main.basic_text_field_layout.view.*

class BasicTextFieldLayout(context: Context, attrs: AttributeSet?): FrameLayout(context, attrs) {
    private var itemView: View? = null
    private var hint: String? = null
        set(value) {
            field = value
            value?.let { itemView?.textInputLayout?.hint = it }
        }
    private var inputType: Int? = null
        set(value) {
            field = value
            value.takeIf { it != null && it != 0 }?.apply {
                when(this) {
                    0 -> itemView?.textInputEditText?.inputType = InputType.TYPE_CLASS_TEXT
                    1 -> itemView?.textInputEditText?.inputType = InputType.TYPE_CLASS_NUMBER
                    2 -> { itemView?.textInputEditText?.inputType = InputType.TYPE_CLASS_NUMBER and InputType.TYPE_NUMBER_FLAG_DECIMAL; itemView?.textInputEditText?.keyListener = DigitsKeyListener.getInstance("0123456789."); itemView?.textInputEditText?.filters = arrayOf(IPAddressInputFilter()); }
                    else -> itemView?.textInputEditText?.inputType = InputType.TYPE_CLASS_TEXT
                }
            }
        }
    private var isLastField: Boolean = false
        set(value) {
            field = value
            if (value) itemView?.textInputEditText?.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    var text: String? = null
        get() = itemView?.textInputEditText?.text?.toString()
        set(value) {
            field = value
            value?.let { itemView?.textInputEditText?.setText(it) }
        }

    init {
        itemView = View.inflate(context, R.layout.basic_text_field_layout, null)
        addView(itemView)
        setupAttributes(attrs = attrs)
        configureTextWatcher()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.BasicTextFieldLayout, 0, 0)
            hint = attributes.getString(R.styleable.BasicTextFieldLayout_hint)
            inputType = attributes.getInt(R.styleable.BasicTextFieldLayout_inputTextType, 0)
            isLastField = attributes.getBoolean(R.styleable.BasicTextFieldLayout_isLastField, false)
            attributes.recycle()
        }
    }

    fun setErrorMessage(message: String) {
        itemView?.textInputLayout?.error = message
    }

    private fun configureTextWatcher() {
        itemView?.textInputEditText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemView?.textInputLayout?.error = null
            }
        })
    }

}