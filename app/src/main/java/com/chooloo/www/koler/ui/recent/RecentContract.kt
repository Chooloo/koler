package com.chooloo.www.koler.ui.recent

import android.graphics.drawable.Drawable
import com.chooloo.www.koler.ui.base.BaseContract

interface RecentContract : BaseContract {
    interface View : BaseContract.View {
        val recentId: Long
        val hideItem: () -> Unit
        var recentName: String?
        var recentCaption: String?
        var recentImage: Drawable?
        var isContactVisible: Boolean
        var isAddContactVisible: Boolean

        fun showRecentMenu(number: String)
        fun openHistoryView(number: String)
        fun openContactView(contactId: Long)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onActionSms()
        fun onActionCall()
        fun onActionMenu()
        fun onActionDelete()
        fun onActionAddContact()
        fun onActionOpenContact()
        fun onActionShowHistory()
    }
}