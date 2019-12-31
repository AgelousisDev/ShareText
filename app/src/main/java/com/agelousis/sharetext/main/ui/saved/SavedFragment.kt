package com.agelousis.sharetext.main.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.agelousis.sharetext.R
import com.agelousis.sharetext.main.MainActivity
import com.agelousis.sharetext.main.ui.enumerations.FragmentViewType
import com.agelousis.sharetext.main.ui.saved.adapters.SavedTextAdapter
import com.agelousis.sharetext.main.ui.saved.enums.SwipeAction
import com.agelousis.sharetext.main.ui.saved.enums.ViewMode
import com.agelousis.sharetext.main.ui.saved.helpers.SavedTextItemTouchHelper
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel
import com.agelousis.sharetext.main.ui.share_text.models.EmptyRow
import com.agelousis.sharetext.main.ui.share_text.models.HeaderRow
import com.agelousis.sharetext.utilities.Constants
import com.agelousis.sharetext.utilities.extensions.applyToAll
import com.agelousis.sharetext.utilities.extensions.getCompatColor
import com.agelousis.sharetext.utilities.extensions.randomColor
import com.agelousis.sharetext.utilities.extensions.shareText
import kotlinx.android.synthetic.main.fragment_saved.view.*
import java.util.*

class SavedFragment : Fragment() {

    var savedViewModel: SavedViewModel? = null
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
                    view?.bottomAppBarSearchField?.isIconified = false
                    //context?.showKeyboard(view = view?.bottomAppBarSearchField?.let { it } ?: return, show = true)
                }
                ViewMode.VIEW_MODE -> {
                    view?.bottomAppBarSearchButton?.show()
                    view?.bottomAppBarTitle?.visibility = View.VISIBLE
                    view?.bottomAppBarSearchField?.visibility = View.GONE
                    //context?.showKeyboard(view = view?.bottomAppBarSearchField?.let { it } ?: return, show = false)
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomAppBar(view = view)
        addObserverAndFetch(view = view)
        configureRecyclerView(view = view)
    }

    private fun configureBottomAppBar(view: View) {
        if ((activity as? MainActivity)?.fragmentViewType == FragmentViewType.VIEW_ALL) view.bottomAppBar.navigationIcon = null

        view.bottomAppBar.setNavigationOnClickListener {
            when((context as? MainActivity)?.fragmentViewType) {
                FragmentViewType.VIEW_ALL -> (activity as? MainActivity)?.onBackPressed()
                FragmentViewType.VIEW_ONLY_SAVED -> activity?.finish()
            }
        }
        view.bottomAppBarSearchButton.setOnClickListener {
           viewMode = ViewMode.EDIT_MODE
        }
        view.bottomAppBarSearchField.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                queryItems(query = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        view.bottomAppBarSearchField.setOnCloseListener {
            viewMode = ViewMode.VIEW_MODE
            false
        }
    }

    private fun configureRecyclerView(view: View) {
        view.savedTextRecyclerView.adapter = SavedTextAdapter(list = filteredList)
        val itemTouchHelper = ItemTouchHelper(SavedTextItemTouchHelper(context = view.context) innerBlock@ { swipeAction, position ->
            when(swipeAction) {
                SwipeAction.SHARE -> {
                    (view.savedTextRecyclerView.adapter as? SavedTextAdapter)?.restoreItem(position = position)
                    context?.shareText(content = (filteredList.getOrNull(position) as? SavedMessageModel)?.text ?: return@innerBlock)
                }
                SwipeAction.DELETE -> {
                    val itemOfFilteredList = filteredList.getOrNull(position) as? SavedMessageModel
                    (view.savedTextRecyclerView.adapter as? SavedTextAdapter)?.removeItemAndUpdate(view.context, position = position)
                    savedViewModel?.deleteSavedMessage(context = view.context, savedMessageModel = list.getOrNull(list.indexOf(itemOfFilteredList ?: return@innerBlock)) as? SavedMessageModel)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(view.savedTextRecyclerView)
    }

    private fun addObserverAndFetch(view: View) {
        savedViewModel?.fetchSavedMessageList(context = view.context)
        savedViewModel?.savedMessageModelList?.observe(this, Observer { savedTextMessageModelList ->
            (activity as? MainActivity)?.mainViewModel?.newShareTextLiveData?.value = Pair(first = 1, second = savedTextMessageModelList.size)
            view.bottomAppBarSearchField.setQuery(null, false)
            viewMode = ViewMode.VIEW_MODE
            list.clear()
            filteredList.clear()

            if ((view.savedTextRecyclerView.adapter as? SavedTextAdapter)?.addEmptyViewIf(EmptyRow(title = resources.getString(R.string.empty_saved_texts), icon = R.drawable.ic_empty)) {
                    savedTextMessageModelList.isEmpty()
                } == false) {
                savedTextMessageModelList.groupBy { it.channel }.toSortedMap().forEach { map ->
                    val colorOfCircleHeader = view.context.randomColor
                    list.add(HeaderRow(title = map.key, showLine = false, headerTextColor = context?.getCompatColor(color = R.color.grey)))
                    list.addAll(with(map.value) {
                        this.applyToAll { it.colorOfCircleHeaderBackground = colorOfCircleHeader; it.colorOfCircleHeader = Constants.getContrastColor(color = colorOfCircleHeader) }
                        this
                    })
                }
                (list.lastOrNull() as? SavedMessageModel)?.showLine = false
            }
            filteredList.addAll(list)
            configureBottomAppBarVisibility(state = filteredList.none { it is EmptyRow })
            if (savedViewModel?.notifyDateSetChangedEnabled == true) (view.savedTextRecyclerView.adapter as? SavedTextAdapter)?.updateItems()
        })
    }

    private fun configureBottomAppBarVisibility(state: Boolean) {
        view?.bottomAppBar?.visibility = if (state) View.VISIBLE else View.GONE
        if (state) view?.bottomAppBarSearchButton?.show() else view?.bottomAppBarSearchButton?.hide()
    }

    private fun queryItems(query: String?) {
        query?.let { unwrappedQuery ->
            filteredList.clear()
            (list.filterIsInstance<SavedMessageModel>()).groupBy { it.channel }.toSortedMap().filter { map -> map.value.any { it.text.toLowerCase(Locale.getDefault()).contains(unwrappedQuery.toLowerCase(Locale.getDefault())) } }.forEach { map ->
                filteredList.add(HeaderRow(title = map.key, showLine = false, headerTextColor = context?.getCompatColor(color = R.color.grey)))
                filteredList.addAll(map.value.filter { savedMessageModel -> savedMessageModel.text.toLowerCase(Locale.getDefault()).contains(unwrappedQuery.toLowerCase(Locale.getDefault())) })
            }
            if (filteredList.isEmpty() && unwrappedQuery.isNotEmpty())
                filteredList.add(EmptyRow(title = String.format(resources.getString(R.string.search_empty_result_text_with_value), unwrappedQuery), icon = R.drawable.ic_empty))
            else if (filteredList.isEmpty() && unwrappedQuery.isEmpty()) filteredList.add(EmptyRow(title = resources.getString(R.string.empty_saved_texts), icon = R.drawable.ic_empty))
            (view?.savedTextRecyclerView?.adapter as? SavedTextAdapter)?.updateItems()
        }
    }

}