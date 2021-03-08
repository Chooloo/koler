package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.BaseContract

interface RecentContract : BaseContract {
    interface View : BaseContract.View {
        fun callRecent()
        fun smsRecent()
        fun deleteRecent()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onActionCall()
        fun onActionSms()
        fun onActionDelete()
    }
}