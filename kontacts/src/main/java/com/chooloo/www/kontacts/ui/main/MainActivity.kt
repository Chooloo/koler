package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.kontacts.R
import com.chooloo.www.kontacts.databinding.MainBinding
import com.chooloo.www.kontacts.di.factory.controllerfactory.ControllerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), MainContract.View {
    override val contentView by lazy { binding.root }
    override lateinit var controller: MainContract.Controller

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

    @Inject lateinit var controllerFactoryKontacts: ControllerFactory


    override fun onSetup() {
        controller = controllerFactoryKontacts.getMainController(this)
        binding.apply {
            mainMenuButton.setOnClickListener { controller.onSettingsClick() }
            mainAddContactButton.setOnClickListener { controller.onAddContactClick() }
            mainSearchBar.apply {
                setOnTextChangedListener(controller::onSearchTextChange)
                editText?.setOnFocusChangeListener { _, hasFocus ->
                    controller.onSearchFocusChange(hasFocus)
                }
            }
        }
    }

    override fun showSearching() {
        binding.root.transitionToState(R.id.constraint_set_main_collapsed)
    }


    override fun setSearchHint(resId: Int) {
        binding.mainSearchBar.setHint(resId)
    }

    override fun setContactsFragment(contactsFragment: ContactsFragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentContainer.id, contactsFragment).commit()
    }
}