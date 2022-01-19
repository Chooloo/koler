package com.chooloo.www.chooloolib.ui.callitems

import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.ui.list.ListContract

interface CallItemsContract : ListContract {
    interface View : ListContract.View<Call> {

    }

    interface Controller<V : View> : ListContract.Controller<Call, V> {
        fun onSplitClick(call: Call)
        fun onRejectClick(call: Call)
    }
}