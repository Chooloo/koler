package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.dialer.DialerFragment
import com.chooloo.www.koler.ui.settings.SettingsFragment
import java.net.URLDecoder

class MainController<V : MainContract.View>(view: V) :
    BaseController<V>(view),
    MainContract.Controller<V> {

    private val _fragments by lazy { listOf(_contactsFragment, _recentsFragment) }
    private val _recentsFragment by lazy { RecentsFragment.newInstance() }
    private val _contactsFragment by lazy { ContactsFragment.newInstance() }


    override fun onStart() {
        super.onStart()

        component.permissions.checkDefaultDialer()
        
        view.apply {
            setFragmentsAdapter(_fragments.size, _fragments::get)
            setSearchHint(component.strings.getString(R.string.hint_search_items))

            headers = arrayOf(
                component.strings.getString(R.string.contacts),
                component.strings.getString(R.string.recents)
            )
            currentPageIndex = component.preferences.defaultPage.index
        }
    }

    override fun onMenuClick() {
        component.prompts.showFragment(SettingsFragment.newInstance())
    }

    override fun onDialpadFabClick() {
        component.prompts.showFragment(DialerFragment.newInstance())
    }

    override fun onViewIntent(intent: Intent) {
        val intentText = try {
            URLDecoder.decode(intent.dataString ?: "", "utf-8")
        } catch (e: Exception) {
            view.showError(R.string.error_couldnt_get_number_from_intent)
            return
        }

        if (intentText.contains("tel:")) {
            component.prompts.showFragment(DialerFragment.newInstance(intentText.substringAfter("tel:")))
        } else {
            view.showError(R.string.error_couldnt_get_number_from_intent)
        }
    }

    override fun onSearchTextChange(text: String) {
        _fragments.forEach { it.controller.applyFilter(text) }
    }

    override fun onSearchFocusChange(isFocus: Boolean) {
        if (isFocus) {
            view.showSearching()
        }
    }

    override fun onPageChange(position: Int) {
    }
}