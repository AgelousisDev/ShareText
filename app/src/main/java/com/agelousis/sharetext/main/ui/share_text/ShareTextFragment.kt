package com.agelousis.sharetext.main.ui.share_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.agelousis.sharetext.R
import kotlinx.android.synthetic.main.fragment_share_text.view.*

class ShareTextFragment : Fragment() {

    private lateinit var shareTextViewModel: ShareTextViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        shareTextViewModel = ViewModelProviders.of(this).get(ShareTextViewModel::class.java)
        return inflater.inflate(R.layout.fragment_share_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI(view = view)
    }

    private fun configureUI(view: View) {
        view.messageTextFieldLayout.setActionDoneListener { println("qwerty") }
        view.messageTextFieldLayout.setFocusListener { println("") }
    }

}