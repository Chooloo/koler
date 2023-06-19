package com.chooloo.www.chooloolib.ui.recents

import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_CONTACTS
import android.content.ClipData
import android.content.ClipboardManager
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.RecentAccount
import com.chooloo.www.chooloolib.data.repository.recents.RecentsRepository
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
open class RecentsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    private val clipboardManager: ClipboardManager,
    private val recentsRepository: RecentsRepository,
) :
    ListViewState<RecentAccount>(permissions) {

    override val noResultsIconRes = R.drawable.history
    override val noResultsTextRes = R.string.error_no_results_recents
    override val permissionsImageRes = R.drawable.history
    override val permissionsTextRes = R.string.error_no_permissions_recents
    override val requiredPermissions = listOf(READ_CALL_LOG, READ_CONTACTS)

    private val _showRecentEvent = MutableDataLiveEvent<RecentAccount>()

    val showRecentEvent = _showRecentEvent as DataLiveEvent<RecentAccount>


    override fun onItemClick(item: RecentAccount) {
        super.onItemClick(item)
        _showRecentEvent.call(item)
    }

    override fun onItemLongClick(item: RecentAccount) {
        super.onItemLongClick(item)
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied number", item.number))
        onMessage(R.string.number_copied_to_clipboard)
    }

    override fun getItemsFlow(filter: String?): Flow<List<RecentAccount>>? =
        recentsRepository.getRecents(filter)
}
