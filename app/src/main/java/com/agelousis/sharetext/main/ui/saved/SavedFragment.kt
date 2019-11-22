package com.agelousis.sharetext.main.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.agelousis.sharetext.R

class SavedFragment : Fragment() {

    private lateinit var savedViewModel: SavedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }
}