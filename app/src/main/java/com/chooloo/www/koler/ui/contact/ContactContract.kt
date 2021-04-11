package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.base.BaseContract

interface ContactContract : BaseContract {
    interface View : BaseContract.View {
        var contactName: String?
        var contactImage: Uri?
        var isStarIconActivated: Boolean

        fun callContact()
        fun smsContact()
        fun editContact()
        fun openContact()
        fun deleteContact()
        fun setFavorite(isFavorite: Boolean)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onLoadContact(contact: Contact)
        fun onActionCall()
        fun onActionSms()
        fun onActionEdit()
        fun onActionInfo()
        fun onActionDelete()
        fun onActionFav()
    }
}