package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.base.BaseContract

interface ContactContract : BaseContract {
    interface View : BaseContract.View {
        var contactName: String?
        var contactImage: Uri?
        var isStarIconVisible: Boolean

        fun showMenu()
        fun smsContact()
        fun callContact()
        fun editContact()
        fun openContact()
        fun deleteContact()
        fun setFavorite(isFavorite: Boolean)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onLoadContact(contact: Contact)
        fun onActionSms()
        fun onActionFav()
        fun onActionCall()
        fun onActionEdit()
        fun onActionInfo()
        fun onActionMenu()
        fun onActionDelete()
    }
}