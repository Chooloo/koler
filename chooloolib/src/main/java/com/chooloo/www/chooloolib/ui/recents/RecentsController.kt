package com.chooloo.www.chooloolib.ui.recents

import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListController
import javax.inject.Inject

class RecentsController<V : RecentsContract.View> @Inject constructor(
    view: V,
    recentsAdapter: RecentsAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val liveDataFactory: LiveDataFactory,
    private val permissionsInteractor: PermissionsInteractor
) :
    ListController<RecentAccount, V>(view, recentsAdapter),
    RecentsContract.Controller<V> {

    override val noResultsIconRes = R.drawable.round_history_24
    override val noResultsTextRes = R.string.error_no_results_recents
    override val noPermissionsTextRes = R.string.error_no_permissions_recents


    private val recentsLiveData by lazy {
        liveDataFactory.allocRecentsProviderLiveData()
    }

    override fun applyFilter(filter: String) {
        super.applyFilter(filter)
        try {
            recentsLiveData.filter = filter
        } catch (e: Exception) {
        }
    }

    override fun fetchData(callback: (items: List<RecentAccount>, hasPermissions: Boolean) -> Unit) {
        permissionsInteractor.runWithReadCallLogPermissions {
            if (it) {
                recentsLiveData.observe(lifecycleOwner) { data ->
                    callback.invoke(data, true)
                }
            } else {
                callback.invoke(emptyList(), false)
            }
        }
    }
}