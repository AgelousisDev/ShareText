package com.agelousis.sharetext.main.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val newShareTextLiveData: MutableLiveData<Pair<Int, Int>> by lazy { MutableLiveData<Pair<Int, Int>>() }

}