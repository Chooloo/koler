package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment
import com.chooloo.www.chooloolib.ui.dialer.DialerFragment
import com.chooloo.www.chooloolib.ui.recent.RecentFragment
import com.chooloo.www.koler.R
import com.chooloo.www.koler.di.factory.fragment.FragmentFactory
import java.net.URLDecoder
import javax.inject.Inject
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory as ChoolooFragmentsFactory

class MainController @Inject constructor(
    view: MainContract.View,
    private val fragmentFactory: FragmentFactory,
    private val promptsInteractor: PromptsInteractor,
    private val stringsInteractor: StringsInteractor,
    private val permissionsInteractor: PermissionsInteractor,
    private val preferencesInteractor: PreferencesInteractor,
    private val choolooFragmentsFactory: ChoolooFragmentsFactory
) :
    BaseController<MainContract.View>(view),
    MainContract.Controller {

    private val _fragments by lazy { listOf(_contactsFragment, _recentsFragment) }
    private val _recentsFragment by lazy { choolooFragmentsFactory.getRecentsFragment() }
    private val _contactsFragment by lazy { choolooFragmentsFactory.getContactsFragment() }


    override fun onSetup() {
        super.onSetup()

        permissionsInteractor.checkDefaultDialer()

        _recentsFragment.setOnItemClickListener(::onRecentItemClick)
        _contactsFragment.setOnItemClickListener(::onContactItemClick)

        view.apply {
            setFragmentsAdapter(_fragments.size, _fragments::get)
            setSearchHint(stringsInteractor.getString(R.string.hint_search_items))

            headers = arrayOf(
                stringsInteractor.getString(R.string.contacts),
                stringsInteractor.getString(R.string.recents)
            )
            currentPageIndex = preferencesInteractor.defaultPage.index
        }
    }


    override fun onMenuClick() {
        promptsInteractor.showFragment(fragmentFactory.getSettingsFragment())
    }

    override fun onDialpadFabClick() {
        promptsInteractor.showFragment(choolooFragmentsFactory.getDialerFragment())
    }

    override fun onViewIntent(intent: Intent) {
        val intentText = try {
            URLDecoder.decode(intent.dataString ?: "", "utf-8")
        } catch (e: Exception) {
            view.showError(R.string.error_couldnt_get_number_from_intent)
            return
        }

        if (intentText.contains("tel:")) {
            promptsInteractor.showFragment(DialerFragment.newInstance(intentText.substringAfter("tel:")))
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

    override fun onRecentItemClick(recent: RecentAccount) {
        promptsInteractor.showFragment(RecentFragment.newInstance(recent.id))
    }

    override fun onContactItemClick(contact: ContactAccount) {
        promptsInteractor.showFragment(BriefContactFragment.newInstance(contact.id))
    }
}