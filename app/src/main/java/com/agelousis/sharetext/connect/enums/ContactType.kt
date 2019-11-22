package com.agelousis.sharetext.connect.enums

import com.agelousis.sharetext.R
import com.agelousis.sharetext.utilities.Constants

enum class ContactType {
    FACEBOOK, INSTAGRAM, LINKEDIN, EMAIL;

    val icon: Int get() = when(this) {
            FACEBOOK -> R.drawable.ic_facebook
            INSTAGRAM -> R.drawable.ic_instagram
            LINKEDIN -> R.drawable.ic_linkedin
            EMAIL -> R.drawable.ic_email
        }

    val type: String get() =
            when(this) {
                FACEBOOK -> Constants.FACEBOOK
                INSTAGRAM -> Constants.INSTAGRAM
                LINKEDIN -> Constants.LINKEDIN
                EMAIL -> Constants.EMAIL
            }

}