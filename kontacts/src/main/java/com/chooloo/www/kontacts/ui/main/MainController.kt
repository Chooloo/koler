package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.chooloolib.ui.settings.SettingsFragment
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

        contactsFragment.controller.setOnItemClickListener(::onContactClick)
    }

    override fun onSettingsClick() {
        component.prompts.showFragment(SettingsFragment.newInstance())
    }

    override fun onAddContactClick() {
        component.navigations.addContact("")
    }

    override fun onSearchTextChange(text: String) {
        contactsFragment.controller.applyFilter(text)
    }

    override fun onSearchFocusChange(isFocus: Boolean) {
        if (isFocus) {
            view.showSearching()
        }
    }

    override fun onContactClick(contact: ContactAccount) {
        component.dialogs.askForCompact { }
    }
}