package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : ListContract.View<Recent>>(view: V) :
    ListPresenter<Recent, V>(view),
    ListContract.Presenter<Recent, V> {

    override val requiredPermissions
        get() = RecentsContentResolver.REQUIRED_PERMISSIONS

    override val noResultsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_results_recents)

    override val noPermissionsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_permissions_recents)


    override fun observeData() {
        boundComponent.recentsProviderLiveData.observe(
            boundComponent.lifecycleOwner,
            this::onDataChanged
        )
    }

    override fun onItemClick(item: Recent) {
        view.showItem(item)
    }

    override fun applyFilter(filter: String) {
        boundComponent.recentsProviderLiveData.filter = filter
    }

    override fun onDeleteItems(items: ArrayList<Recent>) {
        boundComponent.permissionInteractor.runWithPrompt(R.string.warning_delete_recents) {
            items.forEach { boundComponent.recentsInteractor.deleteRecent(it.id) }
        }
    }
}