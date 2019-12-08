package com.agelousis.sharetext.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.agelousis.sharetext.R
import com.agelousis.sharetext.connect.ConnectActivity
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
        when {
            sharedPreferences.isNightMode == 1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPreferences.isNightMode == 0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appIcon.startScaleAnimation { with(InternetConnection { if (it) { startActivity(Intent(this@SplashActivity, ConnectActivity::class.java)); this@SplashActivity.finish() } else { showSnackbar(view = constraintLayout, message = resources.getString(R.string.internet_connection_not_available_label), duration = Snackbar.LENGTH_LONG) } }) { execute() } }
    }
}
