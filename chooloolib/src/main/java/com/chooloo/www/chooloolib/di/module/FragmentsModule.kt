package com.chooloo.www.chooloolib.di.module

import com.chooloo.www.chooloolib.ui.callitems.CallItemsContract
import com.chooloo.www.chooloolib.ui.callitems.CallItemsFragment
import com.chooloo.www.chooloolib.ui.contact.ContactContract
import com.chooloo.www.chooloolib.ui.contact.ContactFragment
import com.chooloo.www.chooloolib.ui.contacts.ContactsContract
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import com.chooloo.www.chooloolib.ui.contacts.ContactsSuggestionsFragment
import com.chooloo.www.chooloolib.ui.dialer.DialerContract
import com.chooloo.www.chooloolib.ui.dialer.DialerFragment
import com.chooloo.www.chooloolib.ui.dialpad.DialpadContract
import com.chooloo.www.chooloolib.ui.dialpad.DialpadFragment
import com.chooloo.www.chooloolib.ui.phones.PhonesContract
import com.chooloo.www.chooloolib.ui.phones.PhonesFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsContract
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment
import com.chooloo.www.chooloolib.ui.settings.SettingsContract
import com.chooloo.www.chooloolib.ui.settings.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface FragmentsModule {
    @Binds
    fun bindDialerFragment(dialerFragment: DialerFragment): DialerContract.View

    @Binds
    fun bindPhonesFragment(phonesFragment: PhonesFragment): PhonesContract.View

    @Binds
    fun bindRecentsFragment(recentsFragment: RecentsFragment): RecentsContract.View

    @Binds
    fun bindContactFragment(contactFragment: ContactFragment): ContactContract.View

    @Binds
    fun bindDialpadFragment(dialpadFragment: DialpadFragment): DialpadContract.View

    @Binds
    fun bindSettingsFragment(settingsFragment: SettingsFragment): SettingsContract.View

    @Binds
    fun bindContactsFragment(contactsFragment: ContactsFragment): ContactsContract.View

    @Binds
    fun bindCallItemsFragment(callItemsFragment: CallItemsFragment): CallItemsContract.View

    @Binds
    fun bindContactsSuggestionsFragment(contactsSuggestionsFragment: ContactsSuggestionsFragment): ContactsSuggestionsFragment
}