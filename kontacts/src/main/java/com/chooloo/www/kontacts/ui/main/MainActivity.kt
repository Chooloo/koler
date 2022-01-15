package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.kontacts.R
import com.chooloo.www.kontacts.databinding.MainBinding

class MainActivity : BaseActivity(), MainContract.View {
    override val contentView by lazy { binding.root }

    override var searchText: String?
        get() = binding.mainSearchBar.text
        set(value) {
            binding.mainSearchBar.text = value
        }

    override var headers: Array<String>
        get() = binding.mainTabs.headers
        set(value) {
            binding.mainTabs.headers = value
        }

    private val binding by lazy { MainBinding.inflate(layoutInflater) }

    override fun onSetup() {
    }

    override fun showSearching() {
        binding.root.transitionToState(R.id.constraint_set_main_collapsed)
    }


    override fun setSearchHint(resId: Int) {
        binding.mainSearchBar.setHint(resId)
    }

    override fun setContactsFragment(contactsFragment: ContactsFragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentContainer.id, contactsFragment).commitNow()
    }
}