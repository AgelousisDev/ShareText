package com.agelousis.sharetext.client_socket.interfaces

import com.agelousis.sharetext.client_socket.models.MessageModel


interface IncomeMessage {
    fun onMessageReceived(message: MessageModel?)
}