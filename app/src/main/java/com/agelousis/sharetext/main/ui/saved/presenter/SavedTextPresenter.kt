package com.agelousis.sharetext.main.ui.saved.presenter

import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel

interface SavedTextPresenter {
    fun onSavedTextClicked(savedMessageModel: SavedMessageModel)
}