package com.chooloo.www.chooloolib.di.factory.fragment

import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment
import com.chooloo.www.chooloolib.ui.callitems.CallItemsFragment
import com.chooloo.www.chooloolib.ui.contact.ContactFragment
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.chooloolib.ui.contacts.ContactsSuggestionsFragment
import com.chooloo.www.chooloolib.ui.dialer.DialerFragment
import com.chooloo.www.chooloolib.ui.dialpad.DialpadFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesFragment
import com.chooloo.www.chooloolib.ui.prompt.PromptFragment
import com.chooloo.www.chooloolib.ui.recent.RecentFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment
import com.chooloo.www.chooloolib.ui.settings.SettingsFragment

interface FragmentFactory {
    fun getContactFragment(): ContactFragment
    fun getDialpadFragment(): DialpadFragment
    fun getSettingsFragment(): SettingsFragment
    fun getContactsFragment(): ContactsFragment
    fun getCallItemsFragment(): CallItemsFragment
    fun getRecentFragment(recentId: Long): RecentFragment
    fun getDialerFragment(text: String? = null): DialerFragment
    fun getPhonesFragment(contactId: Long? = null): PhonesFragment
    fun getRecentsFragment(filter: String? = null): RecentsFragment
    fun getContactsSuggestionsFragment(): ContactsSuggestionsFragment
    fun getBriefContactFragment(contactId: Long): BriefContactFragment
    fun getPromptFragment(title: String, subtitle: String): PromptFragment
}