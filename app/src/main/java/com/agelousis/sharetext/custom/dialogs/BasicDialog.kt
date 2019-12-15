package com.agelousis.sharetext.custom.dialogs

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.agelousis.sharetext.R
import com.agelousis.sharetext.custom.classes.InternalLinkMovementMethod
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.openWebViewIntent
import com.agelousis.sharetext.utilities.extensions.toHtml
import kotlinx.android.synthetic.main.basic_dialog_layout.view.*


class BasicDialog(private val dialogType: BasicDialogType): DialogFragment() {

    companion object {
        fun show(supportFragmentManager: FragmentManager, dialogType: BasicDialogType) {
            with(BasicDialog(dialogType)) {
                retainInstance = true
                this
            }.show(supportFragmentManager, Constants.BASIC_DIALOG_TAG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return inflater.inflate(R.layout.basic_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI(view = view)
    }

    private fun configureUI(view: View) {
        val context = context.takeIf { it != null } ?: return
        view.basicDialogHeader.background.colorFilter = PorterDuffColorFilter(dialogType.headerBackgroundColor ?: ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        view.basicDialogHeaderTitle.text = dialogType.title ?: resources.getString(R.string.instructions_label)
        view.basicDialogHeaderImage.setImageResource(dialogType.icon ?: R.drawable.ic_info)
        view.basicDialogInstructionsText.text = dialogType.text?.toHtml()
        view.basicDialogInstructionsText.movementMethod = InternalLinkMovementMethod(onLinkClickedBlock = {
            context.openWebViewIntent(urlString = it)
            true
        })
        view.basicDialogOkButton.setBackgroundColor((dialogType.headerBackgroundColor ?: ContextCompat.getColor(context, R.color.colorAccent)))
        view.basicDialogOkButton.setOnClickListener { dismiss(); dialogType.basicDialogButtonBlock?.invoke() ?: dismiss() }
    }

}