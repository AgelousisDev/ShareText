package com.agelousis.sharetext.client_socket

import android.os.AsyncTask
import android.os.Build
import com.agelousis.sharetext.client_socket.interfaces.IncomeMessage
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.initJsonMessageObject
import com.agelousis.sharetext.utilities.extensions.messageModel
import java.io.*
import java.net.*
import java.lang.Exception
import kotlin.concurrent.thread

class ClientSocket(ipAddress: String, port: Int, private val incomeMessage: IncomeMessage): AsyncTask<Void?, Void?, MessageModel?>() {

    private var client: Socket? = null

    init {
        thread {
            client = Socket(ipAddress, port)
            sendMessage(message = initJsonMessageObject(
                type = Constants.infoMessageType,
                instantValue = false,
                body = Build.MODEL
            )
            )
        }
    }

    override fun doInBackground(vararg params: Void?): MessageModel? {
        val inputStream = client?.getInputStream()
        inputStream?.use {
            with(BufferedReader(InputStreamReader(it))) {
                return this.readText().messageModel
            }
        }
        return null
    }

    override fun onPostExecute(result: MessageModel?) {
        super.onPostExecute(result)
        incomeMessage.onMessageReceived(message = result)
    }

    private fun getHostAddress(): String {
        return try
        {
            val socket = DatagramSocket()
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002)
            System.out.println(socket.localAddress.hostAddress)
            socket.localAddress.hostAddress
        }
        catch(ex: Exception) {
            ex.printStackTrace()
            "localhost"
        }
    }


    
    private fun sendMessage(message: String) {
        val outToServer = client?.getOutputStream()
        val out = DataOutputStream(outToServer)
        //out.writeByte(0)
        out.writeUTF(message)
    }
    
    fun attachFile(username: String, message: String, filePath: String) {
        sendMessage(message)
        try
        {
                val dos = DataOutputStream(client?.getOutputStream())
                dos.writeByte(1)
                dos.writeUTF(filePath)
                val fis = FileInputStream(filePath)
                var bytesAvailable = fis.available()

                val maxBufferSize = 1024
                var bufferSize = Math.min(bytesAvailable, maxBufferSize)
                val buffer = ByteArray(bufferSize)

                var bytesRead = fis.read(buffer, 0, bufferSize)

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize)
                    bytesAvailable = fis.available()
                    bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    bytesRead = fis.read(buffer, 0, bufferSize)
                }
                dos.flush()
                dos.close()
        }
        catch(ex: IOException) {
            println(ex.toString())
        }
    }
    
    fun loggedIn(user: String) {
        try
        {
            val outToServer = client?.getOutputStream()
            val out = DataOutputStream(outToServer)
            out.writeUTF("$user just arrived")
        }
        catch(ex: IOException) {
            println(ex.message)
        }
    }
    
}
//java ClientSocket localhost 6066
