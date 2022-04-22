package com.chooloo.www.chooloolib.ui.recents

import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_CONTACTS
import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.livedata.RecentsLiveData
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.repository.recents.RecentsRepository
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class RecentsViewState @Inject constructor(
    recentsRepository: RecentsRepository,
    private val permissions: PermissionsInteractor,
    private val clipboardManager: ClipboardManager
) :
    ListViewState<RecentAccount>() {

    override val noResultsIconRes = R.drawable.round_history_24
    override val noResultsTextRes = R.string.error_no_results_recents
    override var noPermissionsTextRes = R.string.error_no_permissions_recents

    private val recentsLiveData = recentsRepository.getRecents() as RecentsLiveData

    val showRecentEvent = DataLiveEvent<RecentAccount>()


    override fun onFilterChanged(filter: String?) {
        super.onFilterChanged(filter)
        if (permissions.hasSelfPermissions(arrayOf(READ_CONTACTS, READ_CALL_LOG))) {
            onPermissionsChanged(true)
            recentsLiveData.filter = filter
        }
    }

    override fun onItemClick(item: RecentAccount) {
        super.onItemClick(item)
        permissions.runWithReadCallLogPermissions { showRecentEvent.call(item) }
    }

    override fun onItemLongClick(item: RecentAccount) {
        super.onItemLongClick(item)
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied number", item.number))
        messageEvent.call(R.string.number_copied_to_clipboard)
    }

    override fun getItemsObservable(callback: (LiveData<List<RecentAccount>>) -> Unit) {
        permissions.runWithReadCallLogPermissions { clp ->
            if (!clp) noPermissionsTextRes = R.string.error_no_permissions_recents
            onPermissionsChanged(clp)
            permissions.runWithReadContactsPermissions { rcp ->
                if (!rcp) noPermissionsTextRes = R.string.error_no_permissions_contacts
                onPermissionsChanged(clp && rcp)
                if (clp && rcp) callback.invoke(recentsLiveData)
            }
        }
    }
}