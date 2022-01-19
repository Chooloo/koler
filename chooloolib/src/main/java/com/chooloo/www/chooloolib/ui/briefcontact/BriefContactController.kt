package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.ui.base.BaseController

class BriefContactController<V : BriefContactContract.View>(view: V) :
    BaseController<V>(view),
    BriefContactContract.Controller<V> {

    private var _contact: ContactAccount? = null
    private var _firstPhone: PhoneAccount? = null

    override fun onStart() {
        component.contacts.queryContact(view.contactId) { contact ->
            _contact = contact
            view.apply {
                contactName = contact?.name
                isStarIconVisible = contact?.starred == true
                contact?.photoUri?.let { contactImage = Uri.parse(it) }
            }
        }
        component.phones.getContactAccounts(view.contactId) {
            _firstPhone = it?.getOrNull(0)
        }
    }

    override fun onActionCall() {
        _firstPhone?.number?.let { component.navigations.call(it) } ?: run {
            view.showError(R.string.error_no_number_to_call)
        }
    }

    override fun onActionSms() {
        _firstPhone?.number?.let { component.navigations.sendSMS(it) }
    }

    override fun onActionEdit() {
        component.navigations.editContact(view.contactId)
    }

    override fun onActionInfo() {
        component.navigations.viewContact(view.contactId)
    }

    override fun onActionDelete() {
        component.permissions.runWithWriteContactsPermissions {
            if (it) {
                component.dialogs.askForValidation(R.string.explain_delete_contact) {
                    if (it) {
                        component.contacts.deleteContact(view.contactId)
                        view.finish()
                    }
                }
            }
        }
    }
}