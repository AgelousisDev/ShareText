package com.agelousis.sharetext.main.ui.saved

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.sharetext.database.DBManager
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel

class SavedViewModel : ViewModel() {

    val savedMessageModelList by lazy(LazyThreadSafetyMode.NONE) { MutableLiveData<List<SavedMessageModel>>() }

    fun fetchSavedMessageList(context: Context) {
        /*val dbManager = DBManager(context = context)
        savedMessageModelList.value = dbManager.fetch()
        dbManager.close()*/
        savedMessageModelList.value = listOf(SavedMessageModel(channel = "Windows", text = "Hello World on channel1", date = "07-03-2015 12:10"),
            SavedMessageModel(channel = "Linux", text = "Hello World on channel 2", date = "09-12-2013 10:08"),SavedMessageModel(channel = "KDE Plasma", text = "Hello World on channel 3", date = "06-02-2009 14:28"),
            SavedMessageModel(channel = "Windows", text = "Hello World on channel 1", date = "01-09-1993 09:21"),SavedMessageModel(channel = "iOS", text = "Hello World on channel 4", date = "14-06-2014 17:54"),
            SavedMessageModel(channel = "Linux", text = "Hello World on channel 2 As you know, I work on Android data binding so let's enable it by adding the following lines of code in our build.gradle file. Hope you are all set! Now let's make a ... " +
                    "Enter animation using RecyclerView and LayoutAnimation ... " +
                    "https://proandroiddev.com › enter-animation... " +
                    "Μετάφραση αυτής της σελίδας " +
                    "22 Ιουλ 2017 - In this tutorial we'll learn an easy way to add an initial content animation for a", date = "10-08-2000 19:30"),SavedMessageModel(channel = "KDE Plasma", text = "Hello World on channel 3", date = "04-11-1993 04:43"))
    }

    fun deleteSavedMessage(context: Context, savedMessageModel: SavedMessageModel?) {
        savedMessageModel?.ID?.let { id ->
            val dbManager = DBManager(context = context)
            dbManager.delete(id = id)
            dbManager.close()
        }
    }

}