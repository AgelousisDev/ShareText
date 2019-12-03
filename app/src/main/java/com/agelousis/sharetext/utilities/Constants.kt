package com.agelousis.sharetext.utilities

import com.agelousis.sharetext.client_socket.models.ServerHost
import com.agelousis.sharetext.main.ui.share_text.view_holders.models.SelectionModel


typealias AnimationCompletionBlock = () -> Unit
typealias ImeActionDoneCompletionBlock = () -> Unit
typealias InternetConnectionBlock = (Boolean) -> Unit
typealias ConnectServiceBlock = (ServerHost?) -> Unit
typealias FocusChangeCompletionBlock = (Boolean) -> Unit
typealias MessageSelectedBlock = (SelectionModel?) -> Unit
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
    const val email_value = "vagelis_agelousis@outlook.com"
    const val facebookActivityLink = "fb://facewebmodal/f?href=https://www.facebook.com/vagelakis.agelousis"
    const val facebookLink = "https://www.facebook.com/vagelakis.agelousis"
    const val facebookPackageName = "com.facebook.katana"
    const val messengerPackageName = "com.facebook.orca"
    const val messengerUserId = "vagelakis.agelousis"
    const val instagramPackageName = "com.instagram.android"
    const val instagramLink = "https://www.instagram.com/vagelakis_agelousis"
    const val linkedInActivityLink = "linkedin://profile/vagelis-agelousis-7a8793124"
    const val linkedInLink = "https://www.linkedin.com/in/vagelis-agelousis-7a8793124/"
    const val linkedInPackageName = "com.linkedin.android"
    const val MESSAGE_TYPE = "type"
    const val INSTANT_VALUE = "instant"
    const val MESSAGE_BODY = "body"
    const val infoMessageType = "text/info"
    const val textType = "text/plain"
    const val IP_ADDRESSES_FILE = "ipAddresses.json"
    const val IP_ADDRESS_KEY_JSON = "ip_address"
}