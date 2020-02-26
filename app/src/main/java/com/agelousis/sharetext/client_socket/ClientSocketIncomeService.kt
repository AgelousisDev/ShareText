package com.agelousis.sharetext.client_socket

import android.os.AsyncTask
import com.agelousis.sharetext.application.MainApplication
import com.agelousis.sharetext.client_socket.interfaces.IncomeMessage
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.utilities.extensions.receivedMessageModel

class ClientSocketIncomeService(private val incomeMessage: IncomeMessage): AsyncTask<Void?, MessageModel?, Void?>() {

    private val messageModelReceived: MessageModel?
        get() = MainApplication.clientSocket?.receivedMessageModel

    override fun doInBackground(vararg params: Void?): Void? {
        while(true)
            publishProgress(messageModelReceived ?: return null)
    }

    override fun onProgressUpdate(vararg values: MessageModel?) {
        super.onProgressUpdate(*values)
        incomeMessage.onMessageReceived(message = values.getOrNull(0))
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        incomeMessage.onMessageReceived(message = null)
    }

}