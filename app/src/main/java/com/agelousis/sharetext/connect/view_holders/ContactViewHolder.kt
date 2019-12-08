package com.agelousis.sharetext.connect.view_holders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.R
import com.agelousis.sharetext.connect.enums.ContactType
import com.agelousis.sharetext.connect.presenter.ContactUsPresenter
import com.agelousis.sharetext.databinding.ContactRowLayoutBinding
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.isPackageInstalled
import com.agelousis.sharetext.utilities.extensions.openWebViewIntent

class ContactViewHolder(private val context: Context, private val binding: ContactRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ContactType) {
        binding.item = item
        binding.presenter = object: ContactUsPresenter {
            override fun onContactClicked() {
                when(item) {
                    ContactType.FACEBOOK -> if (context.packageManager.isPackageInstalled(packageName = Constants.facebookPackageName)) {
                        context.startActivity(with(Intent()) {
                            action = Intent.ACTION_VIEW
                            data = Uri.parse(Constants.facebookActivityLink)
                            `package` = Constants.facebookPackageName
                            this
                        })
                    }
                    else context.openWebViewIntent(urlString = Constants.facebookLink)
                    ContactType.INSTAGRAM -> if (context.packageManager.isPackageInstalled(packageName = Constants.instagramPackageName)) {
                        context.startActivity(with(Intent()) {
                            action = Intent.ACTION_VIEW
                            data = Uri.parse(Constants.instagramLink)
                            `package` = Constants.instagramPackageName
                            this
                        })
                    }
                    else context.openWebViewIntent(urlString = Constants.instagramLink)
                    ContactType.LINKEDIN -> if (context.packageManager.isPackageInstalled(packageName = Constants.linkedInPackageName)) {
                        context.startActivity(with(Intent()) {
                            action = Intent.ACTION_VIEW
                            data = Uri.parse(Constants.linkedInActivityLink)
                            `package` = Constants.linkedInPackageName
                            this
                        })
                    }
                    else context.openWebViewIntent(urlString = Constants.linkedInLink)
                    ContactType.EMAIL -> ShareCompat.IntentBuilder.from(context as Activity)
                        .setChooserTitle(context.resources.getString(R.string.send_email_label))
                        .setType("message/rfc822")
                        .addEmailTo(Constants.email_value)
                        .setSubject(context.resources.getString(R.string.app_name))
                        .startChooser()
                }
            }
        }
        binding.executePendingBindings()
    }
}