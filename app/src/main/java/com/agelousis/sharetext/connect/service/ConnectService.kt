package com.agelousis.sharetext.connect.service

import android.os.AsyncTask
import com.agelousis.sharetext.application.MainApplication
import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.utilities.ConnectServiceBlock
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.initJsonMessageObject
import com.agelousis.sharetext.utilities.messageModel
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class ConnectService(private val ipAddress: String, private val port: Int, private val body: String, private val connectServiceBlock: ConnectServiceBlock): AsyncTask<Void?, Void?, ServerHost?>() {

    override fun doInBackground(vararg params: Void?): ServerHost? {
        try {
            MainApplication.clientSocket = Socket()
            MainApplication.clientSocket?.connect(InetSocketAddress(ipAddress, port), 10000)
            //Write to Server
            val outToServer = MainApplication.clientSocket?.getOutputStream()
            val out = DataOutputStream(outToServer)
            out.writeUTF(initJsonMessageObject(type = Constants.infoMessageType, instantValue = false, body = body))
            //Get from Server
            val inputStream = DataInputStream(MainApplication.clientSocket?.getInputStream().takeIf { it != null } ?: return null)
            return ServerHost(hostName = inputStream.readUTF().messageModel?.body)
        }
        catch (e: Exception) {
            return null
        }
    }

    override fun onPostExecute(result: ServerHost?) {
        super.onPostExecute(result)
        connectServiceBlock(result)
    }

}