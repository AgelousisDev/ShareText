package com.agelousis.sharetext.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.agelousis.sharetext.R
import com.agelousis.sharetext.connect.ConnectActivity
import com.agelousis.sharetext.database.DBManager
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel
import com.agelousis.sharetext.network.InternetConnection
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.isNightMode
import com.agelousis.sharetext.utilities.extensions.showSnackbar
import com.agelousis.sharetext.utilities.extensions.startScaleAnimation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE)
        when (sharedPreferences.isNightMode) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appIcon.startScaleAnimation { with(InternetConnection { if (it) { startActivity(Intent(this@SplashActivity, ConnectActivity::class.java)); this@SplashActivity.finish() } else { showSnackbar(view = constraintLayout, message = resources.getString(R.string.internet_connection_not_available_label), duration = Snackbar.LENGTH_LONG) } }) { execute() } }
        //saveMocData()
    }

    private fun saveMocData() {
        val dbManager = DBManager(context = this)
        dbManager.insert(SavedMessageModel(channel = "Windows", text = "Hello World on channel1", date = "07-03-2015 12:10"))
        dbManager.insert(SavedMessageModel(channel = "Linux", text = "Hello World on channel 2", date = "09-12-2013 10:08"))
        dbManager.insert(SavedMessageModel(channel = "KDE Plasma", text = "Hello World on channel 3", date = "06-02-2009 14:28"))
        dbManager.insert(SavedMessageModel(channel = "Windows", text = "Hello World on channel 1", date = "01-09-1993 09:21"))
        dbManager.insert(SavedMessageModel(channel = "iOS", text = "Hello World on channel 4", date = "14-06-2014 17:54"))
        dbManager.insert(SavedMessageModel(channel = "Linux", text = "Hello World on channel 2 As you know, I work on Android data binding so let's enable it by adding the following lines of code in our build.gradle file. Hope you are all set! Now let's make a ... " +
                "Enter animation using RecyclerView and LayoutAnimation ... " +
                "https://proandroiddev.com › enter-animation... " +
                "Μετάφραση αυτής της σελίδας " +
                "22 Ιουλ 2017 - In this tutorial we'll learn an easy way to add an initial content animation for a", date = "10-08-2000 19:30"))
        dbManager.insert(SavedMessageModel(channel = "KDE Plasma", text = "Hello World on channel 3", date = "04-11-1993 04:43"))
        dbManager.close()
    }

}
