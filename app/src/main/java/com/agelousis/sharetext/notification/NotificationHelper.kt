package com.agelousis.sharetext.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.agelousis.sharetext.R
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.utilities.Constants

object NotificationHelper {

    fun showNotification(context: Context, channelName: String, body: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, Constants.SHARE_TEXT_NOTIFICATION_CHANNEL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel( Constants.SHARE_TEXT_NOTIFICATION_CHANNEL,  Constants.SHARE_TEXT_NOTIFICATION_CHANNEL, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        mBuilder.setContentTitle(channelName)
        mBuilder.setContentText(body)
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        mBuilder.setSmallIcon(R.drawable.share_text_icon)
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.share_text_icon))
        mBuilder.setDefaults(Notification.DEFAULT_ALL)
        mBuilder.setAutoCancel(true)
        mBuilder.setLights(-0x1450dd, 2000, 2000)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        val resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.priority = NotificationCompat.PRIORITY_HIGH
        mBuilder.setContentIntent(resultPendingIntent)
        notificationManager.notify(0, mBuilder.build())
    }

}