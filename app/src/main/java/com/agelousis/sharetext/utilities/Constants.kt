package com.agelousis.sharetext.utilities

import android.graphics.Color
import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.main.ui.share_text.view_holders.models.SelectionModel

typealias AnimationCompletionBlock = () -> Unit
typealias InternetConnectionBlock = (Boolean) -> Unit
typealias ConnectServiceBlock = (ServerHost?) -> Unit
typealias MessageSelectedBlock = (SelectionModel?) -> Unit
typealias ActionSendBlock = (String) -> Unit
object Constants {
    const val PREFERENCES_TAG = "MainPreferences"
    const val DARK_MODE_VALUE = "dark_mode_value"
    const val REMEMBER_IP_ADDRESS_VALUE = "remember_ip_address_value"
    const val BASIC_DIALOG_TAG = "BASIC_DIALOG_TAG"
    const val OPTIONS_BOTTOM_SHEET_TAG = "OPTIONS_BOTTOM_SHEET_TAG"
    const val LOADER_DIALOG_TAG = "LOADER_DIALOG_TAG"
    const val FACEBOOK = "Facebook"
    const val INSTAGRAM = "Instagram"
    const val LINKEDIN = "LinkedIn"
    const val EMAIL = "Email"
    const val GITHUB = "Github"
    const val email_value = "vagelis_agelousis@outlook.com"
    const val facebookActivityLink = "fb://facewebmodal/f?href=https://www.facebook.com/vagelakis.agelousis"
    const val facebookLink = "https://www.facebook.com/vagelakis.agelousis"
    const val facebookPackageName = "com.facebook.katana"
    const val messengerPackageName = "com.facebook.orca"
    const val messengerUserId = "vagelakis.agelousis"
    const val githubLink = "https://github.com/AgelousisDev/ShareText"
    const val instagramPackageName = "com.instagram.android"
    const val instagramLink = "https://www.instagram.com/vagelakis_agelousis"
    const val linkedInActivityLink = "linkedin://profile/vagelis-agelousis-7a8793124"
    const val linkedInLink = "https://www.linkedin.com/in/vagelis-agelousis-7a8793124/"
    const val linkedInPackageName = "com.linkedin.android"
    const val MESSAGE_TYPE = "type"
    const val INSTANT_VALUE = "instant"
    const val MESSAGE_BODY = "body"
    const val CONNECTION_STATE = "connection"
    const val infoMessageType = "text/info"
    const val textType = "text/plain"
    const val IP_ADDRESSES_FILE = "ipAddresses.json"
    const val IP_ADDRESS_KEY_JSON = "ip_address"
    const val DATE_FORMAT = "dd-MM-yyyy HH:mm"
    const val SHARE_TEXT_NOTIFICATION_CHANNEL = "ShareText"

    fun getContrastColor(color: Int): Int {
        val luminance = ( 0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.green(color)) / 255
        val d = if (luminance > 0.5) 0 else 255
        return  Color.rgb(d, d, d)
    }

}