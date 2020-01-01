package com.agelousis.sharetext.main.view_models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.sharetext.notification.NotificationHelper

class MainViewModel: ViewModel() {

    val newShareTextLiveData: MutableLiveData<Pair<Int, Int>> by lazy { MutableLiveData<Pair<Int, Int>>() }

    fun showNotification(context: Context, channelName: String, body: String) = NotificationHelper.showNotification(context = context, channelName = channelName, body = body)

}