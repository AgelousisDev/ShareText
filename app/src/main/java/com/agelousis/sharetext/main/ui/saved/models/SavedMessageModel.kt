package com.agelousis.sharetext.main.ui.saved.models

import java.lang.StringBuilder

data class SavedMessageModel(val ID: Long? = null, val channel: String, val text: String, val date: String, var showLine: Boolean = true, var colorOfCircleHeaderBackground: Int? = null, var colorOfCircleHeader: Int? = null) {

    val joinedChannelLetters: String
        get() {
            val builder = StringBuilder()
            for (i in 0..1) { builder.append(this.channel.split(" ").getOrNull(i)?.firstOrNull()) }
            return builder.toString()
        }

}