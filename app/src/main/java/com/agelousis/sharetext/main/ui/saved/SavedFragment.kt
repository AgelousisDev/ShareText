package com.agelousis.sharetext.main.ui.saved

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.agelousis.sharetext.R
import com.agelousis.sharetext.utilities.extensions.showKeyboard
import kotlinx.android.synthetic.main.fragment_saved.view.*

class SavedFragment : Fragment() {

    private lateinit var savedViewModel: SavedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomAppBar(view = view)
    }

    private fun configureBottomAppBar(view: View) {
        view.bottomAppBar.setNavigationOnClickListener { activity?.finish() }
        view.bottomAppBarSearchButton.setOnClickListener {
            view.bottomAppBarSearchButton.hide()
            view.bottomAppBarTitle.visibility = View.GONE
            view.bottomAppBarSearchField.visibility = View.VISIBLE
            view.bottomAppBarSearchField.requestFocus()
            context?.showKeyboard(view = view.bottomAppBarSearchField, show = true)
        }
        view.bottomAppBarSearchField.addTextChangedListener {
            if (it?.isEmpty() == true) {
                view.bottomAppBarSearchButton.show()
                view.bottomAppBarTitle.visibility = View.VISIBLE
                view.bottomAppBarSearchField.visibility = View.GONE
                context?.showKeyboard(view = view.bottomAppBarSearchField, show = false)
            }
        }
    }

}