package com.agelousis.sharetext.main.ui.share_text.view_model

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.sharetext.client_socket.ClientSocketIncomeService
import com.agelousis.sharetext.client_socket.ClientSocketOutcomeServive
import com.agelousis.sharetext.client_socket.interfaces.IncomeMessage
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.database.DBManager
import com.agelousis.sharetext.service.NotificationServiceBlock
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.otherwise
import com.agelousis.sharetext.utilities.extensions.whenNull
import java.text.SimpleDateFormat
import java.util.*

class ShareTextViewModel : ViewModel(), IncomeMessage {

    var notificationServiceBlock: NotificationServiceBlock? = null

    private var clientSocketIncomeService: ClientSocketIncomeService? = null
    var serviceIsStartingReceiving: Boolean = false
        set(value) {
            field = value
            if (value) {
                clientSocketIncomeService = ClientSocketIncomeService(incomeMessage = this)
                clientSocketIncomeService?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            }
        }

    var outcomeMessageModelString: String? = null
        set(value) {
            field = value
            value?.let {
                ClientSocketOutcomeServive().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, it)
            }
        }

    override fun onMessageReceived(message: MessageModel?) {
        message.whenNull {
            connectionStateLiveData.value = false
        }.otherwise {
            notificationServiceBlock?.invoke(it.body ?: "")
            connectionStateLiveData.value = it.connectionState
            if (!it.connectionState) clientSocketIncomeService?.cancel(true)
            if (it.connectionState) messageModelLiveData.value = it
        }
    }

    val messageModelLiveData by lazy(LazyThreadSafetyMode.NONE) { MutableLiveData<MessageModel>() }

    val connectionStateLiveData by lazy { MutableLiveData<Boolean>() }

    fun saveListOfMessages(context: Context, channel: String, messageModelList: List<MessageModel>) {
        val dbManager = DBManager(context = context)
        messageModelList.forEach { dbManager.insert(channel = channel, text = it.body ?: "", date = with(SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())) {
            this.format(Date())
        }) }
        dbManager.close()
    }

}