package com.chooloo.www.koler.ui.contact

import android.Manifest
import android.net.Uri
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPresenter<V : ContactContract.View>(view: V) :
    BasePresenter<V>(view),
    ContactContract.Presenter<V> {

    private var contact: Contact? = null
    private var _contact: Contact? = null
    private var _firstPhone: PhoneAccount? = null

    override fun onStart() {
        boundComponent.contactsInteractor.getContact(view.contactId) { contact ->
            _contact = contact
            view.apply {
                contactName = contact?.name
                isStarIconVisible = contact?.starred == true
                contact?.photoUri?.let { contactImage = Uri.parse(it) }
            }
        }
        boundComponent.phoneAccountsInteractor.getContactAccounts(view.contactId) {
            _firstPhone = it?.getOrNull(0)
        }
    }

    override fun onActionCall() {
        _firstPhone?.number?.let { boundComponent.navigationInteractor.call(it) } ?: run {
            view.showError(R.string.error_no_number_to_call)
        }
    }

    override fun onActionSms() {
        _firstPhone?.number?.let { boundComponent.contactsInteractor.openSmsView(it) }
    }

    override fun onActionEdit() {
        boundComponent.contactsInteractor.openEditContactView(view.contactId)
    }

    override fun onActionInfo() {
        boundComponent.contactsInteractor.openContactView(view.contactId)
    }

    override fun onActionDelete() {
        boundComponent.permissionInteractor.runWithPrompt(R.string.warning_delete_contact) {
            boundComponent.permissionInteractor.runWithPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS),
                {
                    boundComponent.contactsInteractor.deleteContact(view.contactId)
                })
        }
    }

    override fun onActionMenu() {
        view.showMenu()
    }

    override fun onActionFav() {
        boundComponent.contactsInteractor.toggleContactFavorite(
            view.contactId,
            contact?.starred == true
        )
        view.isStarIconVisible = contact?.starred == true
    }
}