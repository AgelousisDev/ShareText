package com.agelousis.sharetext.service.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceMessageModel(val channelName: String, val body: String): Parcelable