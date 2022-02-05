package com.chooloo.www.chooloolib.ui.callitems

import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.ui.list.ListContract

interface CallItemsContract : ListContract {
    interface View : ListContract.View<Call> {

    }

    interface Controller : ListContract.Controller<Call, View> {
        var calls: List<Call>

        fun onSplitClick(call: Call)
        fun onRejectClick(call: Call)
    }
}