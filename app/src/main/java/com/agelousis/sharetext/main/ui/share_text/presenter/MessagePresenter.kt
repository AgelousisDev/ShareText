package com.agelousis.sharetext.main.ui.share_text.presenter

import com.agelousis.sharetext.client_socket.models.MessageModel

interface MessagePresenter {
    fun onMessageClicked(messageModel: MessageModel)
}