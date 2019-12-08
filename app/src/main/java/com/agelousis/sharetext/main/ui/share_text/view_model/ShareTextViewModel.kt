package com.agelousis.sharetext.main.ui.share_text.view_model

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.sharetext.client_socket.ClientSocketIncomeService
import com.agelousis.sharetext.client_socket.ClientSocketOutcomeServive
import com.agelousis.sharetext.client_socket.interfaces.IncomeMessage
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.utilities.extensions.otherwise
import com.agelousis.sharetext.utilities.extensions.whenNull

class ShareTextViewModel : ViewModel(), IncomeMessage {

    var serviceIsStartingReceiving: Boolean = false
        set(value) {
            field = value
            if (value) {
                ClientSocketIncomeService(incomeMessage = this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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
            messageModelLiveData.value = it
        }
    }

    val messageModelLiveData by lazy(LazyThreadSafetyMode.NONE) { MutableLiveData<MessageModel>() }

    val connectionStateLiveData by lazy { MutableLiveData<Boolean>() }

}