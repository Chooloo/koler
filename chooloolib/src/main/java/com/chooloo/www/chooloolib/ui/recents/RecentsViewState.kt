package com.chooloo.www.chooloolib.ui.recents

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.livedata.contentprovider.RecentsProviderLiveData
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.repository.recents.RecentsRepository
import com.chooloo.www.chooloolib.ui.list.ListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class RecentsViewState @Inject constructor(
    recentsRepository: RecentsRepository,
    private val permissions: PermissionsInteractor
) :
    ListViewState<RecentAccount>() {

    override val noResultsIconRes = R.drawable.round_history_24
    override val noResultsTextRes = R.string.error_no_results_recents
    override val noPermissionsTextRes = R.string.error_no_permissions_recents

    private val recentsLiveData = recentsRepository.getRecents() as RecentsProviderLiveData


    override fun onFilterChanged(filter: String?) {
        super.onFilterChanged(filter)
        recentsLiveData.filter = filter
    }

    override fun getItemsObservable(callback: (LiveData<List<RecentAccount>>) -> Unit) {
        permissions.runWithReadCallLogPermissions {
            onPermissionsChanged(it)
            if (it) callback.invoke(recentsLiveData)
        }
    }
}