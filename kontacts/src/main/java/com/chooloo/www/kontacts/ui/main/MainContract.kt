package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.base.BaseContract
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var searchText: String?
        var headers: Array<String>

        fun showSearching()
        fun setSearchHint(resId: Int)
        fun setContactsFragment(contactsFragment: ContactsFragment)
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onSettingsClick()
        fun onAddContactClick()
        fun onSearchTextChange(text: String)
        fun onSearchFocusChange(isFocus: Boolean)
        fun onContactClick(contact: ContactAccount)
    }
}