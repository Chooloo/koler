package com.chooloo.www.chooloolib.di.factory.fragment

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.ui.base.BaseChoicesFragment
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment
import com.chooloo.www.chooloolib.ui.callitems.CallItemsFragment
import com.chooloo.www.chooloolib.ui.accounts.AccountsFragment
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

interface FragmentFactory {
    fun getDialpadFragment(): DialpadFragment
    fun getSettingsFragment(): SettingsFragment
    fun getContactsFragment(): ContactsFragment
    fun getCallItemsFragment(): CallItemsFragment
    fun getRecentFragment(recentId: Long): RecentFragment
    fun getDialerFragment(text: String? = null): DialerFragment
    fun getPhonesFragment(contactId: Long? = null): PhonesFragment
    fun getContactsSuggestionsFragment(): ContactsSuggestionsFragment
    fun getBriefContactFragment(contactId: Long): BriefContactFragment
    fun getAccountsFragment(contactId: Long? = null): AccountsFragment
    fun getPromptFragment(title: String, subtitle: String): PromptFragment
    fun getRecentsHistoryFragment(filter: String? = null): RecentsHistoryFragment
    fun getRecentsFragment(filter: String? = null, isGrouped: Boolean? = null): RecentsFragment
    fun getChoicesFragment(
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        choices: List<String>
    ): BaseChoicesFragment
}