package com.chooloo.www.chooloolib.ui.callitems

import com.chooloo.www.chooloolib.adapter.CallItemsAdapter
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.ui.list.ListController
import javax.inject.Inject

class CallItemsController<V : CallItemsContract.View> @Inject constructor(
    view: V,
    private val callItemsAdapter: CallItemsAdapter
) :
    ListController<Call, V>(view, callItemsAdapter),
    CallItemsContract.Controller<V> {


    private var _calls: List<Call> = listOf()

    var calls: List<Call>
        get() = _calls
        set(value) {
            _calls = value
        }


    override fun onStart() {
        super.onStart()
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