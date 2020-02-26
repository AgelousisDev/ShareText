package com.agelousis.sharetext.custom.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.agelousis.sharetext.R
import com.agelousis.sharetext.utilities.ActionSendBlock
import kotlinx.android.synthetic.main.message_text_field_layout.view.*

class MessageTextField(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private var itemView: View? = null

    init {
        itemView = View.inflate(context, R.layout.message_text_field_layout, null)
        addView(itemView)
        configureTextField(view = itemView)
    }

    private fun configureTextField(view: View?) {
        view?.messageTextField?.imeOptions = EditorInfo.IME_ACTION_SEND
        view?.messageTextField?.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }

    fun setActionDoneListener(actionSendBlock: ActionSendBlock) {
        itemView?.messageTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) itemView?.messageTextField?.text?.toString()?.let(actionSendBlock); itemView?.messageTextField?.text?.clear()
            true
        }
    }

    fun sendMessageButtonListener(actionSendBlock: ActionSendBlock) {
        itemView?.sendMessageButton?.setOnClickListener { itemView?.messageTextField?.text?.toString()?.let(actionSendBlock); itemView?.messageTextField?.text?.clear() }
    }

}