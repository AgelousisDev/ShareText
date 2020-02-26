package com.agelousis.sharetext.main.ui.share_text

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.main.ui.saved.SavedFragment
import com.agelousis.sharetext.main.ui.share_text.adapters.ShareTextAdapter
import com.agelousis.sharetext.main.ui.share_text.enums.MessagesViewType
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.main.ui.share_text.view_model.ShareTextViewModel
import com.agelousis.sharetext.service.NotificationService
import com.agelousis.sharetext.service.models.ServiceMessageModel
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.copyText
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
        shareTextViewModel = ViewModelProvider(this).get(ShareTextViewModel::class.java)
        return inflater.inflate(R.layout.fragment_share_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI(view = view)
        configureViewModel()
    }

    private fun configureUI(view: View) {
        view.messageTextFieldLayout.setActionDoneListener { sendMessage(body = it.takeIf { it.isNotEmpty() } ?: return@setActionDoneListener) }
        view.messageTextFieldLayout.sendMessageButtonListener { sendMessage(body = it.takeIf { it.isNotEmpty() } ?: return@sendMessageButtonListener) }

        // RecyclerView
        listOfMessages.add(EmptyRow(title = resources.getString(R.string.start_sharing_text), icon = R.drawable.share_text_header_icon))
        val flexLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        view.shareTextRecyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom -> if (bottom < oldBottom) view.shareTextRecyclerView.post { view.shareTextRecyclerView.scrollToPosition((view.shareTextRecyclerView.adapter?.itemCount ?: 0) - 1) } }
        view.shareTextRecyclerView.itemAnimator = DefaultItemAnimator()
        view.shareTextRecyclerView.layoutManager = flexLayoutManager
        view.shareTextRecyclerView.adapter = ShareTextAdapter(list = listOfMessages)

        // Selected Messages Observer
        (view.shareTextRecyclerView.adapter as? ShareTextAdapter)?.selectedMessagesLiveData?.observe(viewLifecycleOwner, Observer {
            messagesViewType = if (it.isNotEmpty()) MessagesViewType.EDIT else MessagesViewType.VIEW
            if (it.isNotEmpty()) setSelectedMessagesCount(size = it.size)
        })
    }

    private fun sendMessage(body: String) {
        shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(type = Constants.textType, instantValue = false, body = body)
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
        shareTextViewModel?.messageModelLiveData?.observe(viewLifecycleOwner, Observer { messageModel ->
            addMessageModel(messageModel = messageModel)
        })
        shareTextViewModel?.connectionStateLiveData?.observe(viewLifecycleOwner, Observer {
            if (!it) {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        })
        shareTextViewModel?.notificationServiceBlock = { messageModel ->
            addMessageModel(messageModel = messageModel)
            context?.startService(with(Intent(context, NotificationService::class.java)) {
                putExtra(
                    NotificationService.SERVICE_MESSAGE_MODEL_EXTRA,
                    ServiceMessageModel(
                        channelName = (activity as? MainActivity)?.serverHost?.hostName ?: "",
                        body = messageModel.body ?: ""
                    )
                )
                this
            })
        }
    }

    private fun addMessageModel(messageModel: MessageModel) {
        listOfMessages.removeAll { it is EmptyRow }
        listOfMessages.add(messageModel)
        (activity as? MainActivity)?.mainViewModel?.newShareTextLiveData?.value = Pair(first = 0, second = listOfMessages.size)
        (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.updateItems()
    }

    private fun clearSelectedMessages() {
        showMenuItems(show = false)
        (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.clearSelectedItems()
        (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.updateItems()
    }

    private fun copySelectedMessages() {
        (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.selectedMessagesLiveData?.value?.let { list ->
            context?.copyText(text = list.mapNotNull { it.body }.joinToString(separator = System.lineSeparator()))
        }
        clearSelectedMessages()
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
                shareTextViewModel?.saveListOfMessages(context = context?.let { it } ?: return super.onOptionsItemSelected(item), channel = (activity as? MainActivity)?.serverHost?.hostName ?: "", messageModelList = (view?.shareTextRecyclerView?.adapter as? ShareTextAdapter)?.selectedMessagesLiveData?.value ?: listOf())
                activity?.supportFragmentManager?.fragments?.filterIsInstance<SavedFragment>()?.firstOrNull()?.apply {
                    this.savedViewModel?.fetchSavedMessageList(context = context?.let { it } ?: return super.onOptionsItemSelected(item))
                }
                clearSelectedMessages()
            }
            R.id.menuCopy -> copySelectedMessages()
        }
        return super.onOptionsItemSelected(item)
    }

}