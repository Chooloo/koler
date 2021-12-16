package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseController
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment
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
            currentPageIndex = component.preferences.defaultPage.index
            headers = arrayOf(
                component.strings.getString(R.string.contacts),
                component.strings.getString(R.string.recents)
            )
            setFragmentsAdapter(_fragments.size, _fragments::get)
            setSearchHint(component.strings.getString(R.string.hint_search_items))
        }
    }

    override fun onMenuClick() {
        view.openSettings()
    }

    override fun onDialpadFabClick() {
        view.openDialer()
    }

    override fun onViewIntent(intent: Intent) {
        val intentText = try {
            URLDecoder.decode(intent.dataString ?: "", "utf-8")
        } catch (e: Exception) {
            view.showError(R.string.error_couldnt_get_number_from_intent)
            return
        }

        if (intentText.contains("tel:")) {
            view.openDialer(intentText.substringAfter("tel:"))
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
}