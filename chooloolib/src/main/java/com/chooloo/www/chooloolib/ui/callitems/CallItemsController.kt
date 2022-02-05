package com.chooloo.www.chooloolib.ui.callitems

import com.chooloo.www.chooloolib.adapter.CallItemsAdapter
import com.chooloo.www.chooloolib.adapter.ListAdapter
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.ui.list.ListController
import javax.inject.Inject

class CallItemsController @Inject constructor(
    view: CallItemsContract.View,
    private val callItemsAdapter: CallItemsAdapter
) :
    ListController<Call, CallItemsContract.View>(view, callItemsAdapter),
    CallItemsContract.Controller {
    override val adapter: ListAdapter<Call> = callItemsAdapter

    override var calls: List<Call>
        get() = _calls
        set(value) {
            _calls = value
        }

    private var _calls: List<Call> = listOf()


    override fun onSetup() {
        super.onSetup()
        callItemsAdapter.apply {
            setOnItemLeftButtonClickListener(this@CallItemsController::onSplitClick)
            setOnItemRightButtonClickListener(this@CallItemsController::onRejectClick)
        }
    }

    override fun fetchData(callback: (items: List<Call>, hasPermissions: Boolean) -> Unit) {
        callback.invoke(_calls, true)
    }

    override fun onSplitClick(call: Call) {
        call.leaveConference()
        view.finish()
    }

    override fun onRejectClick(call: Call) {
        call.reject()
        view.finish()
    }
}