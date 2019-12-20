package com.agelousis.sharetext.main.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.agelousis.sharetext.R
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.main.ui.enumerations.FragmentViewType
import com.agelousis.sharetext.main.ui.saved.adapters.SavedTextAdapter
import com.agelousis.sharetext.main.ui.saved.enums.ViewMode
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import com.agelousis.sharetext.utilities.extensions.showKeyboard
import kotlinx.android.synthetic.main.fragment_saved.view.*
import java.util.*

class SavedFragment : Fragment() {

    private var savedViewModel: SavedViewModel? = null
    private var list = arrayListOf<Any>()
    private var filteredList = arrayListOf<Any>()
    private var viewMode: ViewMode = ViewMode.VIEW_MODE
        set(value) {
            field = value
            when(value) {
                ViewMode.EDIT_MODE -> {
                    view?.bottomAppBarSearchButton?.hide()
                    view?.bottomAppBarTitle?.visibility = View.GONE
                    view?.bottomAppBarSearchField?.visibility = View.VISIBLE
                    view?.bottomAppBarSearchField?.requestFocus()
                    context?.showKeyboard(view = view?.bottomAppBarSearchField?.let { it } ?: return, show = true)
                }
                ViewMode.VIEW_MODE -> {
                    view?.bottomAppBarSearchButton?.show()
                    view?.bottomAppBarTitle?.visibility = View.VISIBLE
                    view?.bottomAppBarSearchField?.visibility = View.GONE
                    context?.showKeyboard(view = view?.bottomAppBarSearchField?.let { it } ?: return, show = false)
                }
            }
        }

    override fun onResume() {
        super.onResume()
        fetchSavedDataWith()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomAppBar(view = view)
        configureRecyclerView(view = view)
    }

    private fun configureBottomAppBar(view: View) {
        view.bottomAppBar.setNavigationOnClickListener {
            if (viewMode == ViewMode.VIEW_MODE)
                when((context as? MainActivity)?.fragmentViewType) {
                    FragmentViewType.VIEW_ALL -> (activity as? MainActivity)?.onBackPressed()
                    FragmentViewType.VIEW_ONLY_SAVED -> activity?.finish()
                }
            else viewMode = ViewMode.VIEW_MODE
        }
        view.bottomAppBarSearchButton.setOnClickListener {
           viewMode = ViewMode.EDIT_MODE
        }
        view.bottomAppBarSearchField.addTextChangedListener {
            if (it?.isEmpty() == true)
                viewMode = ViewMode.VIEW_MODE
            else queryItems(query = it?.toString())
        }
    }

    private fun configureRecyclerView(view: View) {
        view.savedTextRecyclerView?.adapter = SavedTextAdapter(list = filteredList)
    }

    private fun fetchSavedDataWith(query: String? = null) {
        savedViewModel?.fetchSavedMessageList(context = context?.let { it } ?: return)
        savedViewModel?.savedMessageModelList?.observe(this, Observer { savedTextMessageModelList ->
            if (savedTextMessageModelList.isEmpty()) {
                list.add(EmptyRow(title = resources.getString(R.string.empty_shared_text_list_label)))
            }
            else {
                list.clear()
                savedTextMessageModelList.groupBy { it.channel }.toSortedMap().forEach { map ->
                    list.add(HeaderRow(title = map.key, showLine = false))
                    list.addAll(map.value)
                }
                (list.lastOrNull() as? SavedMessageModel)?.showLine = false
            }
            filteredList.addAll(list)
            view?.savedTextRecyclerView?.adapter?.notifyDataSetChanged()
        })
    }

    private fun queryItems(query: String?) {
        query?.let {
            filteredList.clear()
            savedViewModel?.savedMessageModelList?.value?.groupBy { it.channel }?.toSortedMap()?.filter { map -> map.value.any { it.text.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault())) } }?.forEach {
                filteredList.add(HeaderRow(title = it.key, showLine = false))
                filteredList.addAll(it.value)
            }
            view?.savedTextRecyclerView?.adapter?.notifyDataSetChanged()
        }
    }

}