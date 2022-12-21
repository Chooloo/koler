package com.chooloo.www.chooloolib.ui.callitems

import com.chooloo.www.chooloolib.data.model.Call
import com.chooloo.www.chooloolib.data.repository.calls.CallsRepository
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallItemsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    private val callsRepository: CallsRepository
) :
    ListViewState<Call>(permissions) {

    override val itemsFlow get() = callsRepository.getCalls()

    override fun onItemLeftClick(item: Call) {
        super.onItemLeftClick(item)
        item.leaveConference()
        finishEvent.call()
    }

    override fun onItemRightClick(item: Call) {
        super.onItemRightClick(item)
        item.reject()
        finishEvent.call()
    }
}