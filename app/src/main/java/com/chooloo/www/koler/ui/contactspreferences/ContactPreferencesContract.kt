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

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onBlockClick()
        fun onUnblockClick()
        fun onFavoriteClick()
        fun onUnFavoriteClick()
    }
}