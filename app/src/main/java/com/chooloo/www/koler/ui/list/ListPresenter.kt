package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BasePresenter

abstract class ListPresenter<ItemType, V : ListContract.View<ItemType>>(view: V) :
    BasePresenter<V>(view),
    ListContract.Presenter<ItemType, V> {

    private var _permissionsGranted = false

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
        view.toggleRefreshing(false)
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
        view.updateData(items)
        if (view.itemCount == 0) {
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
}