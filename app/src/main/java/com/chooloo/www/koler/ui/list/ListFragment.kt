package com.chooloo.www.koler.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.databinding.ItemsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.reddit.indicatorfastscroll.FastScrollItemIndicator

abstract class ListFragment<ItemType, Adapter : ListAdapter<ItemType>> :
    BaseFragment(),
    ListContract.View<ItemType> {

    abstract val presenter: ListPresenter<ItemType, out ListFragment<ItemType, Adapter>>

    private val _binding by lazy { ItemsBinding.inflate(layoutInflater) }
    private val _isSearchable by lazy { args.getBoolean(ARG_IS_SEARCHABLE) }
    private val _isHideNoResults by lazy { args.getBoolean(ARG_IS_HIDE_NO_RESULTS, false) }

    override val searchHint by lazy { getString(R.string.hint_search_items) }
    override val isCompact by lazy { args.getBoolean(ARG_IS_COMPACT) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _binding.apply {
            itemsSearchBar.apply {
                hint = searchHint
                visibility = if (_isSearchable) VISIBLE else GONE
                setOnTextChangedListener(presenter::onSearchTextChanged)
            }
            itemsDeleteButton.setOnClickListener {
//                presenter.onDeleteItems((itemsRecyclerView.getRecyclerView().adapter as ListAdapter<*>).selectedItems as ArrayList<ItemType>)
            }
        }
        args.getString(ARG_FILTER)?.let { presenter.applyFilter(it) }
    }


    override fun scrollToTop() {
        _binding.itemsRecyclerView.smoothScrollToPosition(0)
    }

    override fun animateListView() {
        boundComponent.animationInteractor.animateRecyclerView(_binding.itemsRecyclerView)
    }

    override fun requestSearchFocus() {
        _binding.itemsSearchBar.requestFocus()
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.apply {
            empty.emptyIcon.visibility = if (isShow && !_isHideNoResults) VISIBLE else GONE
            empty.emptyText.visibility = if (isShow && !_isHideNoResults) VISIBLE else GONE
            itemsRecyclerView.visibility = if (isShow && !_isHideNoResults) GONE else VISIBLE
        }
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            showEmptyPage(false)
        }
    }

    override fun showSelecting(isSelecting: Boolean) {
        _binding.itemsDeleteButton.apply {
            if (isSelecting) {
                boundComponent.animationInteractor.animateIn(this, true)
            } else {
                boundComponent.animationInteractor.showView(this, false)
            }
        }
    }

    override fun setupScrollIndicator() {
        _binding.apply {
            try {
                itemsFastScroller.setupWithRecyclerView(
                    itemsRecyclerView,
                    { position ->
                        (itemsRecyclerView.adapter as ListAdapter<*>).getHeader(position)
                            ?.let { FastScrollItemIndicator.Text(it) }
                    })
                itemsFastScrollerThumb.setupWithFastScroller(itemsFastScroller)
            } catch (e: IllegalStateException) {
            }
        }
    }

    override fun setEmptyTextRes(@StringRes res: Int?) {
        res?.let { _binding.empty.emptyText.setText(it) }
    }

    override fun setEmptyIconRes(res: Int?) {
        res?.let { _binding.empty.emptyIcon.setImageResource(it) }
    }

    override fun setAdapter(adapter: ListAdapter<ItemType>) {
        _binding.itemsRecyclerView.adapter = adapter
    }

    companion object {
        const val ARG_FILTER = "filter"
        const val ARG_IS_COMPACT = "is_compact"
        const val ARG_IS_SEARCHABLE = "is_searchable"
        const val ARG_IS_HIDE_NO_RESULTS = "is_hide_no_results"
    }
}