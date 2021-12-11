package com.chooloo.www.koler.ui.contactspreferences

import com.chooloo.www.koler.ui.base.BaseContract

class ContactPreferencesContract {
    interface View : BaseContract.View {
        val contactId: Long
        var isBlockContactVisible: Boolean
        var isUnblockContactVisible: Boolean
        var isFavoriteContactVisible: Boolean
        var isUnfavoriteContactVisible: Boolean
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onBlockClick()
        fun onUnblockClick()
        fun onFavoriteClick()
        fun onUnFavoriteClick()
    }
}