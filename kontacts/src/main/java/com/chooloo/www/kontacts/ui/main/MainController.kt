package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.kontacts.R
import javax.inject.Inject

class MainController @Inject constructor(
    view: MainContract.View
) :
    BaseController<MainContract.View>(view),
    MainContract.Controller {

    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var stringsInteractor: StringsInteractor
    @Inject lateinit var promptsInteractor: PromptsInteractor
    @Inject lateinit var dialogsInteractor: DialogsInteractor
    @Inject lateinit var navigationsInteractor: NavigationsInteractor

    private val contactsFragment by lazy { fragmentFactory.getContactsFragment() }


    override fun onSetup() {
        super.onSetup()

        view.apply {
            headers = arrayOf(stringsInteractor.getString(R.string.contacts))

            setContactsFragment(contactsFragment)
            setSearchHint(R.string.hint_search_contacts)
        }

        contactsFragment.controller.setOnItemClickListener(::onContactClick)
    }

    override fun onSettingsClick() {
        promptsInteractor.showFragment(fragmentFactory.getSettingsFragment())
    }

    override fun onAddContactClick() {
        navigationsInteractor.addContact("")
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
        dialogsInteractor.askForCompact { }
    }
}