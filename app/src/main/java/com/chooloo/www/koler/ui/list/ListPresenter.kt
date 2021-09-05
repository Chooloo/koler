package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.base.BasePresenter

abstract class ListPresenter<ItemType, V : ListContract.View<ItemType>>(view: V) :
    BasePresenter<V>(view),
    ListContract.Presenter<ItemType, V> {

    private var _permissionsGranted = false

    abstract val adapter: ListAdapter<ItemType>


    override fun onStart() {
        boundComponent.permissionInteractor.runWithPermissions(
            permissions = this.requiredPermissions,
            grantedCallback = ::onPermissionsGranted,
            blockedCallback = ::onPermissionsBlocked,
            rationaleMessage = null,
            deniedCallback = null
        )
        if (boundComponent.preferencesInteractor.isScrollIndicator) {
            view.setupScrollIndicator()
        }
        view.showLoading(true)
        adapter.apply {
            isCompact = view.isCompact
            setOnItemClickListener(this@ListPresenter::onItemClick)
            setOnItemLongClickListener(this@ListPresenter::onItemLongClick)
            setOnSelectingChangeListener(this@ListPresenter::onIsSelectingChanged)
        }
        view.setAdapter(adapter)
    }

    override fun onResults() {
        view.showEmptyPage(false)
    }

    override fun onNoResults() {
        view.emptyStateText = noResultsMessage
        view.showEmptyPage(true)
    }

    override fun onSwipeRefresh() {
        view.requestSearchFocus()
    }

    override fun onPermissionsGranted() {
        _permissionsGranted = true
        view.emptyStateText = noResultsMessage
        observeData()
    }

    override fun onSearchTextChanged(text: String) {
        if (_permissionsGranted) {
            applyFilter(text)
        }
    }

    override fun onDataChanged(items: ArrayList<ItemType>) {
        adapter.data = convertDataToListBundle(items)
        view.showLoading(false)
        if (adapter.itemCount == 0) {
            onNoResults()
        } else {
            onResults()
        }
    }

    override fun onIsSelectingChanged(isSelecting: Boolean) {
        view.showSelecting(isSelecting)
    }

    override fun onPermissionsBlocked(permissions: Array<String>) {
        view.emptyStateText = noPermissionsMessage
    }

    abstract fun observeData()
    abstract fun applyFilter(filter: String)
    abstract fun convertDataToListBundle(data: ArrayList<ItemType>): ListBundle<ItemType>
}