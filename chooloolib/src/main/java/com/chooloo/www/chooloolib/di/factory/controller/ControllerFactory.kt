package com.chooloo.www.chooloolib.di.factory.controller

import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactContract
import com.chooloo.www.chooloolib.ui.call.CallContract
import com.chooloo.www.chooloolib.ui.callitems.CallItemsContract
import com.chooloo.www.chooloolib.ui.contact.ContactContract
import com.chooloo.www.chooloolib.ui.contacts.ContactsContract
import com.chooloo.www.chooloolib.ui.dialer.DialerContract
import com.chooloo.www.chooloolib.ui.dialpad.DialpadContract
import com.chooloo.www.chooloolib.ui.phones.PhonesContract
import com.chooloo.www.chooloolib.ui.prompt.PromptContract
import com.chooloo.www.chooloolib.ui.recent.RecentContract
import com.chooloo.www.chooloolib.ui.recents.RecentsContract
import com.chooloo.www.chooloolib.ui.settings.SettingsContract

interface ControllerFactory {
    fun getCallController(view: CallContract.View): CallContract.Controller
    fun getDialerController(view: DialerContract.View): DialerContract.Controller
    fun getPhonesController(view: PhonesContract.View): PhonesContract.Controller
    fun getPromptController(view: PromptContract.View): PromptContract.Controller
    fun getRecentController(view: RecentContract.View): RecentContract.Controller
    fun getContactController(view: ContactContract.View): ContactContract.Controller
    fun getDialpadController(view: DialpadContract.View): DialpadContract.Controller
    fun getRecentsController(view: RecentsContract.View): RecentsContract.Controller
    fun getSettingsController(view: SettingsContract.View): SettingsContract.Controller
    fun getContactsController(view: ContactsContract.View): ContactsContract.Controller
    fun getCallItemsController(view: CallItemsContract.View): CallItemsContract.Controller
    fun getBriefContactController(view: BriefContactContract.View): BriefContactContract.Controller
}