package com.agelousis.sharetext.network

import android.os.AsyncTask
import com.agelousis.sharetext.utilities.InternetConnectionBlock
import java.net.URL

class InternetConnection(private val internetConnectionBlock: InternetConnectionBlock): AsyncTask<Void?, Void?, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {
        return try {
            with(URL("https://google.com").openConnection()) { this }.getInputStream() != null
        }
        catch(ex: Exception) {
            false
        }
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        internetConnectionBlock(result)
    }
}