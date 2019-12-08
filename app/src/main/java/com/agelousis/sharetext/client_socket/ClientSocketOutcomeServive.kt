package com.agelousis.sharetext.client_socket

import android.os.AsyncTask
import com.agelousis.sharetext.application.MainApplication
import com.agelousis.sharetext.utilities.extensions.messageModel
import com.agelousis.sharetext.utilities.extensions.sendMessageModel

class ClientSocketOutcomeServive: AsyncTask<String, Void?, Void?>() {

    override fun doInBackground(vararg params: String): Void? {
        MainApplication.clientSocket?.sendMessageModel(messageModelString = params[0])
        if (params.getOrNull(0)?.messageModel?.connectionState == false)
            MainApplication.clientSocket?.close()
        return null
    }

}