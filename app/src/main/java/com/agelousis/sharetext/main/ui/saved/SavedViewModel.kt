package com.agelousis.sharetext.main.ui.saved

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.sharetext.database.DBManager
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel

class SavedViewModel : ViewModel() {

    val savedMessageModelList by lazy(LazyThreadSafetyMode.NONE) { MutableLiveData<List<SavedMessageModel>>() }
    var notifyDateSetChangedEnabled: Boolean = true

    fun fetchSavedMessageList(context: Context) {
        notifyDateSetChangedEnabled = true
        val dbManager = DBManager(context = context)
        savedMessageModelList.value = dbManager.fetch()
        dbManager.close()
    }

    fun deleteSavedMessage(context: Context, savedMessageModel: SavedMessageModel?) {
        savedMessageModel?.ID?.let { id ->
            notifyDateSetChangedEnabled = false
            val dbManager = DBManager(context = context)
            dbManager.delete(id = id)
            savedMessageModelList.value = dbManager.fetch()
            dbManager.close()
        }
    }

}