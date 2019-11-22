package com.agelousis.sharetext.connect.service

import android.os.AsyncTask
import com.agelousis.sharetext.application.MainApplication
import com.agelousis.sharetext.utilities.ConnectServiceBlock
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.initJsonMessageObject
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class ConnectService(private val ipAddress: String, private val port: Int, private val body: String, private val connectServiceBlock: ConnectServiceBlock): AsyncTask<Void?, Void?, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        try {
            MainApplication.clientSocket = Socket()
            MainApplication.clientSocket?.connect(InetSocketAddress(ipAddress, port), 10000)
            val outToServer = MainApplication.clientSocket?.getOutputStream()
            val out = DataOutputStream(outToServer)
            out.use {
                it.writeUTF(initJsonMessageObject(type = Constants.infoMessageType, instantValue = false, body = body))
                return true
            }
        }
        catch (e: Exception) {
            return false
        }
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        connectServiceBlock(result)
    }

}