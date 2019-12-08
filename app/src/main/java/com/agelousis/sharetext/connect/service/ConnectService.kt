package com.agelousis.sharetext.connect.service

import android.os.AsyncTask
import com.agelousis.sharetext.application.MainApplication
import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.utilities.*
import com.agelousis.sharetext.utilities.extensions.initJsonMessageObject
import com.agelousis.sharetext.utilities.extensions.receivedMessageModel
import com.agelousis.sharetext.utilities.extensions.sendMessageModel
import java.net.InetSocketAddress
import java.net.Socket

class ConnectService(private val ipAddress: String, private val port: Int, private val body: String, private val connectServiceBlock: ConnectServiceBlock): AsyncTask<Void?, Void?, ServerHost?>() {

    override fun doInBackground(vararg params: Void?): ServerHost? {
        return try {
            MainApplication.clientSocket = Socket()
            MainApplication.clientSocket?.connect(InetSocketAddress(ipAddress, port), 10000)
            //Write to Server
            MainApplication.clientSocket?.sendMessageModel(messageModelString = initJsonMessageObject(connectionState = true, type = Constants.infoMessageType, instantValue = false, body = body))
            //Get from Server
            ServerHost(hostName = MainApplication.clientSocket?.receivedMessageModel?.body)
        }
        catch (e: Exception) {
            null
        }
    }

    override fun onPostExecute(result: ServerHost?) {
        super.onPostExecute(result)
        connectServiceBlock(result)
    }

}