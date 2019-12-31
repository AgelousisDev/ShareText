package com.agelousis.sharetext.utilities.extensions

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.agelousis.sharetext.R
import com.agelousis.sharetext.client_socket.models.MessageModel
import com.agelousis.sharetext.utilities.AnimationCompletionBlock
import com.agelousis.sharetext.utilities.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.Socket
import kotlin.random.Random

fun View.startScaleAnimation(duration: Long = 1000L, animationCompletionBlock: AnimationCompletionBlock? = null) {
    this.scaleX = 0.0f
    this.scaleY = 0.0f
    this.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration).setInterpolator(LinearInterpolator()).setListener(object: Animator.AnimatorListener {
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) { animationCompletionBlock?.invoke() }
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
    }).start()
}

@SuppressWarnings("deprecation")
fun String.toHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(this)

fun Context.openWebViewIntent(urlString: String) {
    val uri = Uri.parse(urlString)
    val intentBuilder = CustomTabsIntent.Builder()
    intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
    intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
    val chromeIntent = intentBuilder.build()
    chromeIntent.intent.setPackage("com.android.chrome")
    chromeIntent.launchUrl(this, uri)
}

/*inline val @receiver:ColorInt Int.darken
    @ColorInt
    get() = ColorUtils.blendARGB(this, Color.BLACK, 0.2f)*/

@BindingAdapter("srcCompat")
    fun setSrcCompat(view: AppCompatImageView, @DrawableRes drawable: Int) {
        view.setImageResource(drawable)
    }

@BindingAdapter("underline")
    fun setUnderline(view: AppCompatTextView, value: String) {
        if (value.isLink) view.text = String.format(view.context.resources.getString(R.string.link_value_text), value).toHtml()
    }
@BindingAdapter("textColor")
    fun setTextColor(view: MaterialTextView, @ColorRes color: Int?) {
        color?.let { view.setTextColor(it) } ?: view.setTextColor(ContextCompat.getColor(view.context, android.R.color.tab_indicator_text))
    }
@BindingAdapter("coloredRoundedBackground")
    fun setColoredRoundedBackground(view: FrameLayout, @ColorRes color: Int?) {
        color?.let { view.background.colorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN) }
    }

fun PackageManager.isPackageInstalled(packageName: String): Boolean {
    var found = true
    try {
        this.getPackageInfo(packageName, 0)
    }
    catch (e: PackageManager.NameNotFoundException) {
        found = false
    }
    return found
}

val String?.messageModel: MessageModel?
    get() = this?.let {
        with(JSONObject(it)) {
            MessageModel(connectionState = this.getBoolean(Constants.CONNECTION_STATE), type = this.getString(Constants.MESSAGE_TYPE), body = this.getString(Constants.MESSAGE_BODY), isInstantMessage = this.getBoolean(Constants.INSTANT_VALUE))
        }
    }

fun initJsonMessageObject(connectionState: Boolean = true, type: String, instantValue: Boolean, body: String) = with(JSONObject()) {
    this.put(Constants.CONNECTION_STATE, connectionState)
    this.put(Constants.MESSAGE_TYPE, type)
    this.put(Constants.INSTANT_VALUE, instantValue)
    this.put(Constants.MESSAGE_BODY, body)
    this.toString()
}

fun Context.showSnackbar(view: View, message: String, duration: Int) {
    val snackbar = Snackbar.make(view, message, duration)
    val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTypeface(ResourcesCompat.getFont(this,R.font.ubuntu_monospace), Typeface.BOLD)
    textView.setTextColor(ContextCompat.getColor(this, R.color.whiteTwo))
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
    snackbar.show()
}

val SharedPreferences?.isNightMode: Int
    get() = this?.getInt(Constants.DARK_MODE_VALUE, -1) ?: -1

val Context?.isNightMode: Int
    get() = when(this?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_NO -> 0
        Configuration.UI_MODE_NIGHT_YES -> 1
        else -> -1
    }

val SharedPreferences.areIPAddressesRemembered: Boolean
    get() = this.getBoolean(Constants.REMEMBER_IP_ADDRESS_VALUE, false)

fun Context.saveIPAddress(ipAddress: String?) {
    val unwrappedIPAddress = ipAddress?.let { it } ?: return
    val file = File("${this.filesDir.absolutePath}/${Constants.IP_ADDRESSES_FILE}")
    if (!file.exists()) file.createNewFile()
    FileInputStream(file).use {
        val bufferedReader = BufferedReader(InputStreamReader(it))
        val jsonString = bufferedReader.readText()
        if (jsonString.isEmpty())
            with(FileWriter(file, false)) {
                this.write(with(JSONArray()) {
                    this.put(JSONObject().put(Constants.IP_ADDRESS_KEY_JSON, unwrappedIPAddress))
                    this.toString()
                })
                this.flush()
                this.close()
            }
        else {
            val jsonArray = JSONArray(jsonString)
            if (jsonArray.getJSONObject(jsonArray.length() - 1).getString(Constants.IP_ADDRESS_KEY_JSON) != unwrappedIPAddress)
                jsonArray.put(with(JSONObject()) {
                    this.put(Constants.IP_ADDRESS_KEY_JSON, unwrappedIPAddress)
                    this
                })
            with(FileWriter(file, false)) {
                this.write(jsonArray.toString())
                this.flush()
                this.close()
            }
        }
    }
}

fun Context.getLastSavedIPAddress(): String? {
    val file = File("${this.filesDir?.absolutePath}/${Constants.IP_ADDRESSES_FILE}")
    if (!file.exists()) file.createNewFile()
    FileInputStream(file).use {
        val bufferedReader = BufferedReader(InputStreamReader(it))
        val jsonString = bufferedReader.readText()
        return if (jsonString.isEmpty()) null
        else {
            with(JSONArray(jsonString)) {
                this.getJSONObject(this.length() - 1).getString(Constants.IP_ADDRESS_KEY_JSON)
            }
        }
    }
}

fun <T> T?.whenNull(receiver: () -> Unit): T? {
    return if (this == null) {
        receiver.invoke()
        null
    } else this
}

fun <T> T?.otherwise(receiver: (unwrapped: T) -> Unit): T? {
    return if (this != null) {
        receiver.invoke(this)
        this
    } else null
}

val String?.isLink: Boolean
    get() = this?.startsWith("https://") == true || this?.startsWith("http://") == true

val Socket.receivedMessageModel: MessageModel?
    get() = try { this.getInputStream()?.let { DataInputStream(it).readUTF().messageModel } } catch(e: Exception) { null }

fun Socket.sendMessageModel(messageModelString: String) = this.getOutputStream()?.let { DataOutputStream(it).writeUTF(messageModelString) }

/*fun Context.showKeyboard(view: View, show: Boolean) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (show) inputMethodManager?.showSoftInput(view, 0) else inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}*/

fun Context.getCompatColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Drawable.fromVector(padding: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.setBounds(padding, padding, canvas.width - padding, canvas.height - padding)
    this.draw(canvas)
    return bitmap
}

fun <T> Array<out T>.second(): T {
    if (isEmpty())
        throw NoSuchElementException("Array is empty.")
    return this[1]
}

fun Context.shareText(content: String) {
    startActivity(with(Intent(Intent.ACTION_SEND)) {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, content)
        this
    })
}

val Context.randomColor: Int
    get()  {
        var color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        if (color == ContextCompat.getColor(this, R.color.colorAccentDarker))
            color = ContextCompat.getColor(this, R.color.colorAccent)
        return color
    }

inline fun <T> Iterable<T>.applyToAll(action: (T) -> Unit) { for (element in this) action(element) }

inline fun <T> Array<out T>.applyToAll(action: (T) -> Unit) { for (element in this) action(element) }



