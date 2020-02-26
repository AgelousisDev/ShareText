package com.agelousis.sharetext.client_socket.models

data class MessageModel(val connectionState: Boolean, val type: String?, val body: String?, val isInstantMessage: Boolean)