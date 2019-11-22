package com.agelousis.sharetext.custom.dialogs

enum class BasicDialogType {
    INSTRUCTIONS, WARNING;

    var headerBackgroundColor: Int? = null
    var text: String? = null
    var icon: Int? = null

}