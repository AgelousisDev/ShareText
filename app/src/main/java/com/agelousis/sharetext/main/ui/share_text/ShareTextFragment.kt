package com.agelousis.sharetext.main.ui.share_text

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.main.ui.saved.SavedFragment
import com.agelousis.sharetext.main.ui.share_text.adapters.ShareTextAdapter
import com.agelousis.sharetext.main.ui.share_text.enums.MessagesViewType
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.main.ui.share_text.view_model.ShareTextViewModel
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.initJsonMessageObject
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.fragment_share_text.view.*

class ShareTextFragment : Fragment() {



    private var menu: Menu? = null
    var shareTextViewModel: ShareTextViewModel? = null
    private val listOfMessages = arrayListOf<Any>()
    private var messagesViewType: MessagesViewType? = null
        set(value) {
            field = value
            showMenuItems(show = value == MessagesViewType.EDIT)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        shareTextViewModel = ViewModelProviders.of(this).get(ShareTextViewModel::class.java)
        return inflater.inflate(R.layout.fragment_share_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI(view = view)
        //configureViewModel()
    }

    private fun configureUI(view: View) {
        view.messageTextFieldLayout.setActionDoneListener { shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(type = Constants.textType, instantValue = false, body = it) }
        view.messageTextFieldLayout.sendMessageButtonListener { shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(type = Constants.textType, instantValue = false, body = it) }

        // RecyclerView
        //listOfMessages.add(EmptyRow(title = resources.getString(R.string.start_sharing_text), icon = R.drawable.share_text_header_icon))
        listOfMessages.addAll(arrayOf(MessageModel(connectionState = true, type = "text/plain", body = "Hello Ubuntu 19.10", isInstantMessage = false),
            MessageModel(connectionState = true, type = "text/plain", body = "Hello Ubuntu 19.10", isInstantMessage = false),
            MessageModel(connectionState = true, type = "text/plain", body = "Hello Ubuntu 19.10", isInstantMessage = false),
            MessageModel(connectionState = true, type = "text/plain", body = "Hello Ubuntu 19.10", isInstantMessage = false)))
        val flexLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        view.shareTextRecyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom -> if (bottom < oldBottom) view.shareTextRecyclerView.post { view.shareTextRecyclerView.scrollToPosition((view.shareTextRecyclerView.adapter?.itemCount ?: 0) - 1) } }
        view.shareTextRecyclerView.itemAnimator = DefaultItemAnimator()
        view.shareTextRecyclerView.layoutManager = flexLayoutManager
        view.shareTextRecyclerView.adapter = ShareTextAdapter(list = listOfMessages)

        // Selected Messages Observer
        (view.shareTextRecyclerView.adapter as? ShareTextAdapter)?.selectedMessagesLiveData?.observe(this, Observer {
            messagesViewType = if (it.isNotEmpty()) MessagesViewType.EDIT else MessagesViewType.VIEW
            if (it.isNotEmpty()) setSelectedMessagesCount(size = it.size)
        })
    }

    private fun setSelectedMessagesCount(size: Int) {
        menu?.findItem(R.id.menuSelectedItems)?.title = String.format(resources.getString(R.string.selected_messages_value), size)
    }

    private fun showMenuItems(show: Boolean) {
        menu?.children?.forEach { it.isVisible = show }
        if (!show) menu?.findItem(R.id.menuSelectedItems)?.title = ""
    }

    private fun configureViewModel() {
        shareTextViewModel?.serviceIsStartingReceiving = true
        shareTextViewModel?.messageModelLiveData?.observe(this, Observer { messageModel ->
            listOfMessages.removeAll { it is EmptyRow }
            listOfMessages.add(messageModel)
            (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.updateItems()
        })
        shareTextViewModel?.connectionStateLiveData?.observe(this, Observer {
            if (!it) {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        })
    }

    private fun clearSelectedMessages() {
        showMenuItems(show = false)
        (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.clearSelectedItems()
        (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.updateItems()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_activity_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuClose -> clearSelectedMessages()
            R.id.menuSave -> {
                shareTextViewModel?.saveListOfMessages(context = context?.let { it } ?: return super.onOptionsItemSelected(item), channel = (context as? MainActivity)?.serverHost?.hostName ?: "", messageModelList = (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.selectedMessagesLiveData?.value ?: listOf())
                activity?.supportFragmentManager?.fragments?.filterIsInstance<SavedFragment>()?.firstOrNull()?.apply {
                    this.savedViewModel?.fetchSavedMessageList(context = context?.let { it } ?: return super.onOptionsItemSelected(item))
                }
                clearSelectedMessages()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}