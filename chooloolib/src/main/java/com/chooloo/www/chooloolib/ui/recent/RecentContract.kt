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
        var isBlockButtonVisible: Boolean
        var isBlockButtonActivated: Boolean
    }

    interface Controller : BaseContract.Controller<View> {
        fun onActionSms()
        fun onActionCall()
        fun onActionDelete()
        fun onActionAddContact()
        fun onActionOpenContact()
        fun onActionShowHistory()
        fun onActionBlock(isBlock: Boolean)
    }
}