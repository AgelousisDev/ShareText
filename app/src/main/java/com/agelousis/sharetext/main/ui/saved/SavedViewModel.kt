package com.agelousis.sharetext.main.ui.saved

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.sharetext.database.DBManager
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel

class SavedViewModel : ViewModel() {

    val savedMessageModelList by lazy(LazyThreadSafetyMode.NONE) { MutableLiveData<List<SavedMessageModel>>() }

    fun fetchSavedMessageList(context: Context) {
        val dbManager = DBManager(context = context)
        savedMessageModelList.value = dbManager.fetch()
    }

}