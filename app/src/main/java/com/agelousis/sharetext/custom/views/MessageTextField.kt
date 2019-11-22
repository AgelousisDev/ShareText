package com.agelousis.sharetext.custom.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.agelousis.sharetext.R
import com.agelousis.sharetext.utilities.FocusChangeCompletionBlock
import com.agelousis.sharetext.utilities.ImeActionDoneCompletionBlock
import kotlinx.android.synthetic.main.message_text_field_layout.view.*

class MessageTextField(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private var itemView: View? = null

    init {
        itemView = View.inflate(context, R.layout.message_text_field_layout, null)
        addView(itemView)
    }

    fun setFocusListener(focusChangeCompletionBlock: FocusChangeCompletionBlock) {
        itemView?.messageTextField?.setOnFocusChangeListener { _, hasFocus -> focusChangeCompletionBlock(hasFocus) }
    }

    fun setActionDoneListener(actionDoneCompletionBlock: ImeActionDoneCompletionBlock) {
        itemView?.messageTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) actionDoneCompletionBlock()
            true
        }
    }

}