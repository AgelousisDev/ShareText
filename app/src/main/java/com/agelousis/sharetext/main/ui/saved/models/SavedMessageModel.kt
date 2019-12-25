package com.agelousis.sharetext.main.ui.saved.models

data class SavedMessageModel(val ID: Long? = null, val channel: String, val text: String, val date: String, var showLine: Boolean = true)