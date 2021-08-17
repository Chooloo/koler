package com.chooloo.www.koler.ui.contact

import android.Manifest
import android.net.Uri
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPresenter<V : ContactContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
    ContactContract.Presenter<V> {

    private val contact by lazy { boundComponent.contactsInteractor.getContact(mvpView.contactId) }
    private val firstPhone by lazy {
        boundComponent.phoneAccountsInteractor.getContactAccounts(mvpView.contactId).getOrNull(0)
    }

    override fun onLoadContact() {
        view.apply {
            contactName = contact?.name
            isStarIconVisible = contact?.starred == true
            contact?.photoUri?.let { contactImage = Uri.parse(it) }
        }
    }

    override fun onActionCall() {
        view.callContact()
    }

    override fun onActionSms() {
        firstPhone?.number?.let { boundComponent.contactsInteractor.openSmsView(it) }
    }

    override fun onActionEdit() {
        boundComponent.contactsInteractor.openEditContactView(view.contactId)
    }

    override fun onActionInfo() {
        boundComponent.contactsInteractor.openContactView(view.contactId)
    }

    override fun onActionDelete() {
        boundComponent.permissionInteractor.runWithPrompt(R.string.warning_delete_contact) {
            boundComponent.permissionInteractor.runWithPermissions(
                arrayOf(Manifest.permission.WRITE_CONTACTS),
                {
                    boundComponent.contactsInteractor.deleteContact(view.contactId)
                },
                null,
                null,
                null
            )
        }
    }

    override fun onActionMenu() {
        view.showMenu()
    }

    override fun onActionFav() {
        boundComponent.permissionInteractor.runWithPermissions(
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            {
                boundComponent.contactsInteractor.toggleContactFavorite(
                    view.contactId,
                    contact?.starred == true
                )
            },
            null,
            null,
            null
        )
        view.isStarIconVisible = contact?.starred == true
    }
}