package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.ui.base.BaseContract

interface ContactContract : BaseContract {
    interface View : BaseContract.View {
        val contactId: Long
        var contactName: String?
        var contactImage: Uri?
        var isStarIconVisible: Boolean

        fun showMenu()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onActionSms()
        fun onActionFav()
        fun onActionCall()
        fun onActionEdit()
        fun onActionInfo()
        fun onActionMenu()
        fun onActionDelete()
    }
}