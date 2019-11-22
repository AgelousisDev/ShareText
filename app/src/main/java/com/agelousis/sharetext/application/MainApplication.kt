package com.agelousis.sharetext.application

import android.app.Application
import java.net.Socket

class MainApplication: Application() {

    companion object {
        var clientSocket: Socket? = null
    }

}