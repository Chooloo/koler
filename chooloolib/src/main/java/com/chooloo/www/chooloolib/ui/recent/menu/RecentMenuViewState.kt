package com.chooloo.www.chooloolib.ui.recent.menu

import android.Manifest
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuViewState
import com.chooloo.www.chooloolib.util.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentMenuViewState @Inject constructor(
    private val blocked: BlockedInteractor,
    private val recents: RecentsInteractor,
    private val navigations: NavigationsInteractor,
    private val permissions: PermissionsInteractor
) : BaseMenuViewState() {
    override val menuResList = listOf(R.menu.menu_recent_extra)

    val recentId = MutableLiveData<Long>()
    val isBlocked = MutableLiveData<Boolean>()
    val recentNumber = MutableLiveData<String>()
    val showHistoryEvent = LiveEvent()

    override fun onMenuItemClick(itemId: Int) {
        super.onMenuItemClick(itemId)
        when (itemId) {
            R.id.menu_recent_history -> onShowHistory()
            R.id.menu_recent_whatsapp -> onOpenMessager() //TODO: rename button
            R.id.menu_recent_block -> onBlock(true)
            R.id.menu_recent_unblock -> onBlock(false)
        }
    }

    fun onShowHistory() {
        showHistoryEvent.call()
    }

    fun onOpenMessager() {
        navigations.openMessager(null,recentNumber.value)
    }

    fun onBlock(isBlock: Boolean) {
        permissions.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            recentNumber.value?.let {
                if (isBlock) {
                    blocked.blockNumber(it)
                } else {
                    blocked.unblockNumber(it)
                }
                isBlocked.value = isBlock
                finishEvent.call()
            }
        }
    }

    fun onDelete() {
        permissions.runWithPermissions(arrayOf(Manifest.permission.WRITE_CALL_LOG), {
            recentId.value?.let { recents.deleteRecent(it) }
            finishEvent.call()
        }, {
            errorEvent.call(R.string.error_no_permissions_edit_call_log)
        })
    }
}