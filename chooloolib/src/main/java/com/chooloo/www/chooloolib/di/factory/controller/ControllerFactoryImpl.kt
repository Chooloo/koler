package com.chooloo.www.chooloolib.di.factory.controller

import android.content.ClipboardManager
import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.adapter.CallItemsAdapter
import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
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
import javax.inject.Inject

class ControllerFactoryImpl @Inject constructor(
    private val phonesAdapter: PhonesAdapter,
    private val recentsAdapter: RecentsAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val contactsAdapter: ContactsAdapter,
    private val liveDataFactory: LiveDataFactory,
    private val callsInteractor: CallsInteractor,
    private val fragmentFactory: FragmentFactory,
    private val audiosInteractor: AudiosInteractor,
    private val colorsInteractor: ColorsInteractor,
    private val clipboardManager: ClipboardManager,
    private val phonesInteractor: PhonesInteractor,
    private val callItemsAdapter: CallItemsAdapter,
    private val promptsInteractor: PromptsInteractor,
    private val stringsInteractor: StringsInteractor,
    private val recentsInteractor: RecentsInteractor,
    private val screensInteractor: ScreensInteractor,
    private val blockedInteractor: BlockedInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val contactsInteractor: ContactsInteractor,
    private val drawablesInteractor: DrawablesInteractor,
    private val callAudiosInteractor: CallAudiosInteractor,
    private val proximitiesInteractor: ProximitiesInteractor,
    private val preferencesInteractor: PreferencesInteractor,
    private val navigationsInteractor: NavigationsInteractor,
    private val permissionsInteractor: PermissionsInteractor
) : ControllerFactory {
    override fun getCallController(view: CallContract.View): CallContract.Controller {
        return CallController(
            view,
            callsInteractor,
            fragmentFactory,
            audiosInteractor,
            colorsInteractor,
            phonesInteractor,
            promptsInteractor,
            stringsInteractor,
            screensInteractor,
            dialogsInteractor,
            callAudiosInteractor,
            proximitiesInteractor
        )
    }

    override fun getDialerController(view: DialerContract.View): DialerContract.Controller {
        return DialerController(
            view,
            audiosInteractor,
            recentsInteractor,
            navigationsInteractor
        )
    }

    override fun getPhonesController(view: PhonesContract.View): PhonesContract.Controller {
        return PhonesController(
            view,
            phonesAdapter,
            lifecycleOwner,
            liveDataFactory,
            clipboardManager,
            navigationsInteractor,
            permissionsInteractor
        )
    }

    override fun getPromptController(view: PromptContract.View): PromptContract.Controller {
        return PromptController(view)
    }

    override fun getRecentController(view: RecentContract.View): RecentContract.Controller {
        return RecentController(
            view,
            phonesInteractor,
            recentsInteractor,
            dialogsInteractor,
            promptsInteractor,
            blockedInteractor,
            drawablesInteractor,
            preferencesInteractor,
            navigationsInteractor,
            permissionsInteractor
        )
    }

    override fun getContactController(view: ContactContract.View): ContactContract.Controller {
        return ContactController(view)
    }

    override fun getDialpadController(view: DialpadContract.View): DialpadContract.Controller {
        return DialpadController(
            view,
            audiosInteractor
        )
    }

    override fun getRecentsController(view: RecentsContract.View): RecentsContract.Controller {
        return RecentsController(
            view,
            recentsAdapter,
            lifecycleOwner,
            liveDataFactory,
            permissionsInteractor
        )
    }

    override fun getSettingsController(view: SettingsContract.View): SettingsContract.Controller {
        return SettingsController(
            view,
            colorsInteractor,
            dialogsInteractor,
            navigationsInteractor,
            preferencesInteractor
        )
    }

    override fun getContactsController(view: ContactsContract.View): ContactsContract.Controller {
        return ContactsController(
            view,
            contactsAdapter,
            lifecycleOwner,
            liveDataFactory,
            permissionsInteractor
        )
    }

    override fun getCallItemsController(view: CallItemsContract.View): CallItemsContract.Controller {
        return CallItemsController(
            view,
            callItemsAdapter
        )
    }

    override fun getBriefContactController(view: BriefContactContract.View): BriefContactContract.Controller {
        return BriefContactController(
            view,
            phonesInteractor,
            dialogsInteractor,
            contactsInteractor,
            navigationsInteractor,
            permissionsInteractor
        )
    }
}