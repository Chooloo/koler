package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.ui.base.BaseContract

interface ContactContract : BaseContract {
    interface View : BaseContract.View {
        val contactId: Long
        var contactName: String?
        var contactImage: Uri?
        var isStarIconVisible: Boolean
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onActionSms()
        fun onActionCall()
        fun onActionEdit()
        fun onActionInfo()
        fun onActionDelete()
    }
}