package com.chooloo.www.chooloolib.di.module

import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactContract
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactController
import com.chooloo.www.chooloolib.ui.call.CallContract
import com.chooloo.www.chooloolib.ui.call.CallController
import com.chooloo.www.chooloolib.ui.callitems.CallItemsContract
import com.chooloo.www.chooloolib.ui.callitems.CallItemsController
import com.chooloo.www.chooloolib.ui.contact.ContactContract
import com.chooloo.www.chooloolib.ui.contact.ContactController
import com.chooloo.www.chooloolib.ui.contacts.ContactsContract
import com.chooloo.www.chooloolib.ui.contacts.ContactsController
import com.chooloo.www.chooloolib.ui.dialer.DialerContract
import com.chooloo.www.chooloolib.ui.dialer.DialerController
import com.chooloo.www.chooloolib.ui.dialpad.DialpadContract
import com.chooloo.www.chooloolib.ui.dialpad.DialpadController
import com.chooloo.www.chooloolib.ui.phones.PhonesContract
import com.chooloo.www.chooloolib.ui.phones.PhonesController
import com.chooloo.www.chooloolib.ui.prompt.PromptContract
import com.chooloo.www.chooloolib.ui.prompt.PromptController
import com.chooloo.www.chooloolib.ui.recent.RecentContract
import com.chooloo.www.chooloolib.ui.recent.RecentController
import com.chooloo.www.chooloolib.ui.recents.RecentsContract
import com.chooloo.www.chooloolib.ui.recents.RecentsController
import com.chooloo.www.chooloolib.ui.settings.SettingsContract
import com.chooloo.www.chooloolib.ui.settings.SettingsController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ControllersModule {
//    @Binds
//    fun bindCallController(callController: CallController): CallContract.Controller
//
//    @Binds
//    fun bindDialerController(dialerController: DialerController): DialerContract.Controller
//
//    @Binds
//    fun bindPhonesController(phonesController: PhonesController): PhonesContract.Controller
//
//    @Binds
//    fun bindPromptController(promptController: PromptController): PromptContract.Controller
//
//    @Binds
//    fun bindrecentController(recentController: RecentController): RecentContract.Controller
//
//    @Binds
//    fun bindContactController(contactController: ContactController): ContactContract.Controller
//
//    @Binds
//    fun bindDialpadController(dialpadController: DialpadController): DialpadContract.Controller
//
//    @Binds
//    fun bindRecentsController(recentsController: RecentsController): RecentsContract.Controller
//
//    @Binds
//    fun bindContactsController(contactsController: ContactsController): ContactsContract.Controller
//
//    @Binds
//    fun bindSettingsController(settingsController: SettingsController): SettingsContract.Controller
//
//    @Binds
//    fun bindCallItemsController(callItemsController: CallItemsController): CallItemsContract.Controller
//
//    @Binds
//    fun bindContactsSuggestionsController(contactsController: ContactsController): ContactsContract.Controller
//
//    @Binds
//    fun bindBriefContactController(briefContactController: BriefContactController): BriefContactContract.Controller
}
