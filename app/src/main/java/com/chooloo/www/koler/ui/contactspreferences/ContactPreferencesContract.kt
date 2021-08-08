package com.chooloo.www.koler.ui.contactspreferences

import com.chooloo.www.koler.ui.base.BaseContract

class ContactPreferencesContract {
    interface View : BaseContract.View {
        fun toggleContactBlocked(isBlock: Boolean)
        fun toggleContactFavorite(isFavorite: Boolean)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onBlockClick()
        fun onUnblockClick()
        fun onFavoriteClick()
        fun onUnFavoriteClick()
    }
}