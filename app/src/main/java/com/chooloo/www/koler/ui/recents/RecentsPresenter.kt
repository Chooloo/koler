package com.chooloo.www.koler.ui.recents

import android.Manifest.permission.WRITE_CALL_LOG
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : ListContract.View<Recent>>(view: V) :
    ListPresenter<Recent, V>(view),
    ListContract.Presenter<Recent, V> {

    private val recentsLiveData by lazy {
        boundComponent.liveDataFactory.allocRecentsProviderLiveData()
    }


    override val requiredPermissions
        get() = RecentsContentResolver.REQUIRED_PERMISSIONS

    override val noResultsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_results_recents)

    override val noPermissionsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_permissions_recents)


    override fun observeData() {
        recentsLiveData.observe(boundComponent.lifecycleOwner, this::onDataChanged)
    }

    override fun onItemClick(item: Recent) {
        view.showItem(item)
    }

    override fun applyFilter(filter: String) {
        recentsLiveData.filter = filter
    }

    override fun onDeleteItems(items: ArrayList<Recent>) {
        boundComponent.permissionInteractor.runWithPermissions(arrayOf(WRITE_CALL_LOG), {
            boundComponent.permissionInteractor.runWithPrompt(R.string.warning_delete_recents) {
                items.forEach { boundComponent.recentsInteractor.deleteRecent(it.id) }
            }
        })
    }
}