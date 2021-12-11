package com.chooloo.www.koler.ui.recents

import android.Manifest.permission.WRITE_CALL_LOG
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListController

class RecentsController<V : ListContract.View<Recent>>(view: V) :
    ListController<Recent, V>(view),
    ListContract.Controller<Recent, V> {

    override val adapter by lazy { RecentsAdapter(boundComponent) }

    override val noResultsIconRes = R.drawable.round_history_24
    override val noResultsTextRes = R.string.error_no_results_recents
    override val noPermissionsTextRes = R.string.error_no_permissions_recents
    override val requiredPermissions = RecentsContentResolver.REQUIRED_PERMISSIONS


    private val recentsLiveData by lazy {
        boundComponent.liveDataFactory.allocRecentsProviderLiveData()
    }


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

    override fun convertDataToListBundle(data: ArrayList<Recent>) = ListBundle.fromRecents(data)
}