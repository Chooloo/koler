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
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.AnimationManager
import com.chooloo.www.koler.util.permissions.runWithPermissions

abstract class ListFragment<ItemType, DataType, Adapter : ListAdapter<ItemType>> : BaseFragment(),
    ListContract.View<ItemType> {
    private val _presenter by lazy { ListPresenter<ItemType, ListContract.View<ItemType>>() }
    private val _binding by lazy { ItemsBinding.inflate(layoutInflater) }
    private val _isSearchable by lazy { argsSafely.getBoolean(ARG_IS_SEARCHABLE) }

    override val itemCount get() = adapter.itemCount
    override val requiredPermissions: Array<String>? = null
    override val noResultsMessage by lazy { getString(R.string.error_no_results) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions) }
    override val searchHint by lazy { getString(R.string.hint_search_items) }

    abstract val adapter: Adapter

    companion object {
        const val ARG_IS_COMPACT = "is_compact"
        const val ARG_IS_SEARCHABLE = "is_searchable"
    }

    override var emptyStateText: String?
        get() = _binding.itemsEmptyText.text.toString()
        set(value) {
            _binding.itemsEmptyText.text = value

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter.attach(this)

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
                if(!_isSearchable){
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
                }
            }
        }

        requiredPermissions?.let {
            runWithPermissions(
                permissions = it,
                grantedCallback = _presenter::onPermissionsGranted,
                blockedCallback = _presenter::onPermissionsBlocked
            )
        } ?: _presenter.onPermissionsGranted()
    }

    override fun attachData() {
        onAttachData()
    }

    override fun animateListView() {
        AnimationManager(_activity).runLayoutAnimation(_binding.itemsRecyclerView)
    }

    override fun requestSearchFocus() {
        _binding.itemsSearchBar.requestFocus()
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.apply {
            itemsEmptyText.visibility = if (isShow) VISIBLE else GONE
            itemsRecyclerView.visibility = if (isShow) GONE else VISIBLE
        }
    }

    override fun updateData(data: ListBundle<ItemType>) {
        adapter.data = data
    }

    override fun toggleRefreshing(isRefreshing: Boolean) {
        _binding.itemsSwipeRefreshLayout.isRefreshing = isRefreshing
    }

    abstract fun onAttachData()
    abstract fun onItemClick(item: ItemType)
    abstract fun onItemLongClick(item: ItemType)
}