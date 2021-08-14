package com.chooloo.www.koler.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.databinding.ItemsBinding
import com.chooloo.www.koler.interactor.animation.AnimationInteractorImpl
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.PreferencesManager
import com.reddit.indicatorfastscroll.FastScrollItemIndicator

abstract class ListFragment<ItemType, Adapter : ListAdapter<ItemType>> :
    BaseFragment(),
    ListContract.View<ItemType> {

    private val _binding by lazy { ItemsBinding.inflate(layoutInflater) }
    private val _permissionsManager by lazy { PermissionsManager(baseActivity) }
    private val _isSearchable by lazy { argsSafely.getBoolean(ARG_IS_SEARCHABLE) }
    private val _preferencesManager by lazy { PreferencesManager.getInstance(baseActivity) }
    private val _animationInteractor by lazy { AnimationInteractorImpl(_preferencesInteractor) }
    private val _preferencesInteractor by lazy { PreferencesInteractorImpl(_preferencesManager) }
    private val _presenter by lazy { ListPresenter<ItemType, ListContract.View<ItemType>>(this) }

    override val itemCount get() = adapter.itemCount
    override val requiredPermissions: Array<String>? = null
    override val searchHint by lazy { getString(R.string.hint_search_items) }
    override val noResultsMessage by lazy { getString(R.string.error_no_results) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions) }
    override val hideNoResults by lazy { argsSafely.getBoolean(ARG_IS_HIDE_NO_RESULTS, false) }

    override var emptyStateText: String?
        get() = _binding.itemsEmptyText.text.toString()
        set(value) {
            _binding.itemsEmptyText.text = value

        }

    abstract val adapter: Adapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        adapter.apply {
            setOnItemClickListener(::onItemClick)
            setOnItemLongClickListener(::onItemLongClick)
        }

        _binding.apply {
            itemsSearchBar.apply {
                hint = searchHint
                visibility = if (_isSearchable) VISIBLE else GONE
                setOnTextChangedListener(_presenter::onSearchTextChanged)
            }
            itemsSwipeRefreshLayout.apply {
                setOnRefreshListener(_presenter::onSwipeRefresh)
                if (!_isSearchable) {
                    setDistanceToTriggerSync(9999999)
                }
            }
            itemsRecyclerView.apply {
                adapter = this@ListFragment.adapter.apply {
                    isCompact = argsSafely.getBoolean(ARG_IS_COMPACT)

                    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                        override fun onChanged() {
                            super.onChanged()
                            _presenter.onDataChanged()
                        }
                    })
                    setOnSelectingChangeListener { _presenter.onSelectingChanged(it) }
                }
            }
            itemsDeleteButton.setOnClickListener {
                onDeleteItems((itemsRecyclerView.adapter as ListAdapter<*>).selectedItems as ArrayList<ItemType>)
            }
        }

        if (_preferencesInteractor.isScrollIndicator) {
            setupScrollIndicator()
        }

        requiredPermissions?.let {
            _permissionsManager.runWithPermissions(
                permissions = it,
                grantedCallback = _presenter::onPermissionsGranted,
                blockedCallback = _presenter::onPermissionsBlocked
            )
        } ?: _presenter.onPermissionsGranted()
    }


    //region list view

    override fun attachData() {
        onAttachData()
    }

    override fun animateListView() {
        _animationInteractor.animateRecyclerView(_binding.itemsRecyclerView)
    }

    override fun requestSearchFocus() {
        _binding.itemsSearchBar.requestFocus()
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.apply {
            itemsEmptyText.visibility = if (isShow && !hideNoResults) VISIBLE else GONE
            itemsRecyclerView.visibility = if (isShow && !hideNoResults) GONE else VISIBLE
        }
    }

    override fun showSelecting(isSelecting: Boolean) {
        _binding.itemsDeleteButton.apply {
            if (isSelecting) {
                _animationInteractor.animateIn(this)
            } else {
                _animationInteractor.showView(this, false)
            }
        }
    }

    override fun updateData(data: ListBundle<ItemType>) {
        adapter.data = data
    }

    override fun toggleRefreshing(isRefreshing: Boolean) {
        _binding.itemsSwipeRefreshLayout.isRefreshing = isRefreshing
    }

    //endregion


    private fun setupScrollIndicator() {
        _binding.apply {
            itemsFastScroller.setupWithRecyclerView(itemsRecyclerView, { position ->
                adapter.getHeader(position)?.let { FastScrollItemIndicator.Text(it) }
            })
            itemsFastScrollerThumb.setupWithFastScroller(itemsFastScroller)
        }
    }


    //region opened methods

    open fun onAttachData() {}
    open fun onItemClick(item: ItemType) {}
    open fun onItemLongClick(item: ItemType) {}
    open fun onDeleteItems(items: ArrayList<ItemType>) {}

    //endregion


    companion object {
        const val ARG_IS_COMPACT = "is_compact"
        const val ARG_IS_SEARCHABLE = "is_searchable"
        const val ARG_IS_HIDE_NO_RESULTS = "is_hide_no_results"
    }
}