package com.agelousis.sharetext.custom.dialogs

typealias BasicDialogButtonBlock = () -> Unit
data class BasicDialogType(val headerBackgroundColor: Int? = null, val title: String? = null, val text: String? = null, val icon: Int? = null, val basicDialogButtonBlock: BasicDialogButtonBlock? = null)