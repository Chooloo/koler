package com.chooloo.www.chooloolib.ui.recent

import android.graphics.drawable.Drawable
import com.chooloo.www.chooloolib.ui.base.BaseContract

interface RecentContract : BaseContract {
    interface View : BaseContract.View {
        val recentId: Long
        var recentName: String?
        var recentCaption: String?
        var recentImage: Drawable?
        var isContactVisible: Boolean
        var isAddContactVisible: Boolean

        fun openHistoryView(number: String)
        fun openContactView(contactId: Long)
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onActionSms()
        fun onActionCall()
        fun onActionDelete()
        fun onActionAddContact()
        fun onActionOpenContact()
        fun onActionShowHistory()
    }
}