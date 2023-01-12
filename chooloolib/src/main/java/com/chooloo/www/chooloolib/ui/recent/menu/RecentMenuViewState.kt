package com.chooloo.www.chooloolib.ui.recent.menu

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuViewState
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentMenuViewState @Inject constructor(
    private val blocked: BlockedInteractor,
    private val recents: RecentsInteractor,
    private val navigations: NavigationsInteractor,
    private val permissions: PermissionsInteractor
) : BaseMenuViewState() {
    override val menuResList = listOf(R.menu.menu_recent_extra)

    private val _recentId = MutableLiveData<Long>()
    private val _isBlocked = MutableLiveData<Boolean>()
    private val _recentNumber = MutableLiveData<String>()
    private val _showHistoryEvent = MutableLiveEvent()
    private val _confirmDeleteEvent = MutableLiveEvent()

    val recentId = _recentId as LiveData<Long>
    val isBlocked = _isBlocked as LiveData<Boolean>
    val recentNumber = _recentNumber as LiveData<String>
    val showHistoryEvent = _showHistoryEvent as LiveEvent
    val confirmDeleteEvent = _confirmDeleteEvent as LiveEvent

    override fun onMenuItemClick(itemId: Int) {
        super.onMenuItemClick(itemId)
        when (itemId) {
            R.id.menu_recent_history -> onShowHistory()
            R.id.menu_recent_whatsapp -> onOpenWhatsapp()
            R.id.menu_recent_block -> onBlock(true)
            R.id.menu_recent_unblock -> onBlock(false)
            R.id.menu_recent_delete -> _confirmDeleteEvent.call()
        }
    }

    fun onShowHistory() {
        _showHistoryEvent.call()
    }

    fun onOpenWhatsapp() {
        navigations.openWhatsapp(recentNumber.value)
    }

    fun onBlock(isBlock: Boolean) {
        permissions.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            recentNumber.value?.let {
                viewModelScope.launch {
                    if (isBlock) {
                        blocked.blockNumber(it)
                    } else {
                        blocked.unblockNumber(it)
                    }
                }
                _isBlocked.value = isBlock
                onFinish()
            }
        }
    }

    fun onDelete() {
        permissions.runWithPermissions(arrayOf(Manifest.permission.WRITE_CALL_LOG), {
            recentId.value?.let(recents::deleteRecent)
            onFinish()
        }, {
            onError(R.string.error_no_permissions_edit_call_log)
        })
    }

    fun onRecentId(recentId: Long) {
        _recentId.value = recentId
    }

    fun onIsBlocked(isBlocked: Boolean) {
        _isBlocked.value = isBlocked
    }

    fun onRecentNumber(recentNumber: String) {
        _recentNumber.value = recentNumber
    }
}