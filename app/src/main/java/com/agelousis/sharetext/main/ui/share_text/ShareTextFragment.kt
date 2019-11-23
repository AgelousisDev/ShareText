package com.agelousis.sharetext.main.ui.share_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.main.ui.share_text.adapters.ShareTextAdapter
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.utilities.Constants
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.fragment_share_text.view.*

class ShareTextFragment : Fragment() {

    private lateinit var shareTextViewModel: ShareTextViewModel
    private val list = arrayListOf<Any>()

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

        // RecyclerView
        list.addAll(listOf(MessageModel(type = Constants.textType, body = "Hello, how are you?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "I am fine you?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "This is made with RecyclerView.ViewHolder", isInstantMessage = false), MessageModel(type = Constants.textType, body = "Did you integrate git?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "What about clicking on the item?\nEh?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "Maybe a dialog to get the details of it", isInstantMessage = false), MessageModel(type = Constants.textType, body = "Check the margins better", isInstantMessage = false), MessageModel(type = Constants.textType, body = "https://www.google.com", isInstantMessage = false)))
        val flexLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        view.shareTextRecyclerView.itemAnimator = DefaultItemAnimator()
        view.shareTextRecyclerView.layoutManager = flexLayoutManager
        view.shareTextRecyclerView.adapter = ShareTextAdapter(list = list)
    }

}