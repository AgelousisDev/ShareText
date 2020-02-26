package com.agelousis.sharetext.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.notification.NotificationHelper
import com.agelousis.sharetext.service.models.ServiceMessageModel

typealias NotificationServiceBlock = (messageModel: MessageModel) -> Unit
class NotificationService: Service() {

    companion object {
        const val SERVICE_MESSAGE_MODEL_EXTRA = "NotificationService=serviceMessageModelExtra"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        (intent?.extras?.getParcelable(SERVICE_MESSAGE_MODEL_EXTRA) as? ServiceMessageModel)?.let {
            NotificationHelper.showNotification(context = this, channelName = it.channelName, body = it.body)
        }
        return START_STICKY
    }

}