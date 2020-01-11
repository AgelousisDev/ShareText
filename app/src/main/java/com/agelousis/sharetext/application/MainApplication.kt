package com.agelousis.sharetext.application

import android.app.Application
import java.net.Socket

class MainApplication: Application() {

    companion object {
        // Socket
        var clientSocket: Socket? = null

        // Properties
        var isOnBackground: Boolean = false
    }

}