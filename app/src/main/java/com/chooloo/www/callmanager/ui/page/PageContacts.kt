package com.chooloo.www.callmanager.ui.page

import com.chooloo.www.callmanager.ui.contacts.ContactsFragment

class PageContacts : PageFragment(), PageMvpView {

    private lateinit var _presenter: PageMvpPresenter<PageMvpView>
    private lateinit var _contactsFragment: ContactsFragment

    companion object {
        fun newInstance(): PageContacts = PageContacts()
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = PagePresenter()
        _presenter.attach(this)

        _contactsFragment = ContactsFragment.newInstance().apply {
            setOnScrollStateChangedListener { newState -> _presenter.onScrollStateChanged(newState) }
        }

        childFragmentManager.beginTransaction().add(binding.fragmentPageLayout.id, _contactsFragment).commitNow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun onSearchTextChanged(text: String) {
        _presenter.onSearchTextChanged(text)
    }

    override fun onDialNumberChanged(number: String) {
        _presenter.onDialNumberChanged(number)
    }
}