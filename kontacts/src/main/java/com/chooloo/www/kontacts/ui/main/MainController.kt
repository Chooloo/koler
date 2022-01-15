package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.kontacts.R

class MainController<V : MainContract.View>(view: V) :
    BaseController<V>(view),
    MainContract.Controller<V> {

    private val contactsFragment by lazy { ContactsFragment.newInstance() }


    override fun onStart() {
        super.onStart()
        view.apply {
            headers = arrayOf(component.strings.getString(R.string.contacts))

            setContactsFragment(contactsFragment)
            setSearchHint(R.string.hint_search_contacts)
        }
    }

    override fun onSettingsClick() {
    }

    override fun onAddContactClick() {
        component.navigations.addContact("")
    }

    override fun onSearchTextChange(text: String) {
    }

    override fun onSearchFocusChange(isFocus: Boolean) {
    }
}