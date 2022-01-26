package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

class BriefContactController<V : BriefContactContract.View> @Inject constructor(
    view: V,
    private val phonesInteractor: PhonesInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val contactsInteractor: ContactsInteractor,
    private val navigationsInteractor: NavigationsInteractor,
    private val permissionsInteractor: PermissionsInteractor
) :
    BaseController<V>(view),
    BriefContactContract.Controller<V> {

    private var _contact: ContactAccount? = null
    private var _firstPhone: PhoneAccount? = null


    override fun onStart() {
        contactsInteractor.queryContact(view.contactId) { contact ->
            _contact = contact
            view.apply {
                contactName = contact?.name
                isStarIconVisible = contact?.starred == true
                contact?.photoUri?.let { contactImage = Uri.parse(it) }
            }
        }
        phonesInteractor.getContactAccounts(view.contactId) {
            _firstPhone = it?.getOrNull(0)
        }
    }

    override fun onActionCall() {
        _firstPhone?.number?.let { navigationsInteractor.call(it) } ?: run {
            view.showError(R.string.error_no_number_to_call)
        }
    }

    override fun onActionSms() {
        _firstPhone?.number?.let { navigationsInteractor.sendSMS(it) }
    }

    override fun onActionEdit() {
        navigationsInteractor.editContact(view.contactId)
    }

    override fun onActionInfo() {
        navigationsInteractor.viewContact(view.contactId)
    }

    override fun onActionDelete() {
        permissionsInteractor.runWithWriteContactsPermissions {
            if (it) {
                dialogsInteractor.askForValidation(R.string.explain_delete_contact) {
                    if (it) {
                        contactsInteractor.deleteContact(view.contactId)
                        view.finish()
                    }
                }
            }
        }
    }
}