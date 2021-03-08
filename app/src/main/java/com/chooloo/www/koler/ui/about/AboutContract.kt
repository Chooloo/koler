package com.chooloo.www.koler.ui.about

import com.chooloo.www.koler.ui.base.BaseContract

interface AboutContract : BaseContract {
    interface View : BaseContract.View {
        fun openSource()
        fun sendEmail()
        fun reportBug()
        fun rateApp()
        fun donate()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onOpenSourceClick()
        fun onSendEmailClick()
        fun onReportBugClick()
        fun onRateAppClick()
        fun onDonateClick()
    }
}