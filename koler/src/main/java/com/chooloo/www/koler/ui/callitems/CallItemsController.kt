package com.chooloo.www.koler.ui.callitems

import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.koler.adapter.CallItemsAdapter
import com.chooloo.www.chooloolib.ui.list.ListController

class CallItemsController<V : CallItemsContract.View>(view: V) :
    ListController<Call, V>(view),
    CallItemsContract.Controller<V> {

    private var _calls: List<Call> = listOf()

    var calls: List<Call>
        get() = _calls
        set(value) {
            _calls = value
        }


    override val adapter by lazy {
        CallItemsAdapter(component).apply {
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