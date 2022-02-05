package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import com.chooloo.www.chooloolib.ui.base.BaseContract

interface BriefContactContract : BaseContract {
    interface View : BaseContract.View {
        val contactId: Long
        var contactName: String?
        var contactImage: Uri?
        var isStarIconVisible: Boolean
    }

    interface Controller : BaseContract.Controller<View> {
        fun onActionSms()
        fun onActionCall()
        fun onActionEdit()
        fun onActionInfo()
        fun onActionDelete()
    }
}