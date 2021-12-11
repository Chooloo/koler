package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.base.BaseController

abstract class ListController<ItemType, V : ListContract.View<ItemType>>(view: V) :
    BaseController<V>(view),
    ListContract.Controller<ItemType, V> {

    private var _permissionsGranted = false
    private var _onItemsChangedListener: (ArrayList<ItemType>) -> Unit? = {}
    private val noPermissionsIconRes = R.drawable.round_block_24

    override fun onStart() {
        boundComponent.permissionInteractor.runWithPermissions(
            permissions = this.requiredPermissions,
            grantedCallback = ::onPermissionsGranted,
            blockedCallback = ::onPermissionsBlocked,
            rationaleMessage = null,
            deniedCallback = null
        )

        adapter.apply {
            setOnItemClickListener(this@ListController::onItemClick)
            setOnItemLongClickListener(this@ListController::onItemLongClick)
        }

        view.apply {
            showLoading(true)
            setAdapter(adapter)
            if (component.preferencesInteractor.isScrollIndicator) {
                setupScrollIndicator()
            }
        }
    }

    override fun onResults() {
        view.showEmptyPage(false)
    }


    override fun onNoResults() {
        view.setEmptyIconRes(noResultsIconRes)
        view.setEmptyTextRes(noResultsTextRes)
        view.showEmptyPage(true)
    }

    override fun onSwipeRefresh() {
        view.requestSearchFocus()
    }

    override fun onPermissionsGranted() {
        _permissionsGranted = true
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
        if (items.size == 0) {
            onNoResults()
        } else {
            onResults()
        }
        view.scrollToTop()
        _onItemsChangedListener.invoke(items)
    }

    override fun onIsSelectingChanged(isSelecting: Boolean) {
        view.showSelecting(isSelecting)
    }

    override fun onPermissionsBlocked(permissions: Array<String>) {
        view.setEmptyIconRes(noPermissionsIconRes)
        view.setEmptyTextRes(noPermissionsTextRes)
    }

    override fun setOnItemsChangedListener(onItemsChangedListener: (ArrayList<ItemType>) -> Unit?) {
        _onItemsChangedListener = onItemsChangedListener
    }


    abstract val noResultsIconRes: Int?
    abstract val noResultsTextRes: Int?
    abstract val noPermissionsTextRes: Int?
    abstract val adapter: ListAdapter<ItemType>

    abstract fun observeData()
    abstract fun applyFilter(filter: String)
    abstract fun convertDataToListBundle(data: ArrayList<ItemType>): ListBundle<ItemType>
}