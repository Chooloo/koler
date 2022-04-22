package com.chooloo.www.chooloolib.di.factory.fragment

import com.chooloo.www.chooloolib.ui.accounts.AccountsFragment
import com.chooloo.www.chooloolib.ui.base.BaseChoicesFragment
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment
import com.chooloo.www.chooloolib.ui.callitems.CallItemsFragment
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.chooloolib.ui.contacts.ContactsSuggestionsFragment
import com.chooloo.www.chooloolib.ui.dialer.DialerFragment
import com.chooloo.www.chooloolib.ui.dialpad.DialpadFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesFragment
import com.chooloo.www.chooloolib.ui.prompt.PromptFragment
import com.chooloo.www.chooloolib.ui.recent.RecentFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment
import com.chooloo.www.chooloolib.ui.recentshistory.RecentsHistoryFragment
import com.chooloo.www.chooloolib.ui.settings.SettingsFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FragmentFactoryImpl @Inject constructor() : FragmentFactory {
    override fun getDialpadFragment() = DialpadFragment()
    override fun getSettingsFragment() = SettingsFragment()
    override fun getContactsFragment() = ContactsFragment()
    override fun getCallItemsFragment() = CallItemsFragment()
    override fun getContactsSuggestionsFragment() = ContactsSuggestionsFragment()
    override fun getDialerFragment(text: String?) = DialerFragment.newInstance(text)
    override fun getRecentFragment(recentId: Long) = RecentFragment.newInstance(recentId)
    override fun getAccountsFragment(contactId: Long?) =
        AccountsFragment.newInstance(contactId)

    override fun getBriefContactFragment(contactId: Long) =
        BriefContactFragment.newInstance(contactId)

    override fun getPhonesFragment(contactId: Long?) =
        PhonesFragment.newInstance(contactId)

    override fun getPromptFragment(title: String, subtitle: String) =
        PromptFragment.newInstance(title, subtitle)

    override fun getRecentsHistoryFragment(filter: String?) =
        RecentsHistoryFragment.newInstance(filter)

    override fun getRecentsFragment(filter: String?, isGrouped: Boolean?): RecentsFragment =
        RecentsFragment.newInstance(filter, isGrouped)

    override fun getChoicesFragment(
        titleRes: Int,
        subtitleRes: Int?,
        choices: List<String>
    ) = BaseChoicesFragment.newInstance(titleRes, subtitleRes, choices)

}