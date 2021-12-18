package com.chooloo.www.koler.ui.contactspreferences

import android.Manifest
import android.annotation.SuppressLint
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseController

class ContactPreferencesController<V : ContactPreferencesContract.View>(view: V) :
    BaseController<V>(view),
    ContactPreferencesContract.Controller<V> {

    override fun onStart() {
        view.apply {
            isBlockContactVisible = false
            isUnblockContactVisible = false
            component.contacts.queryContact(view.contactId) { contact ->
                isFavoriteContactVisible = contact?.starred == false
                isUnfavoriteContactVisible = contact?.starred == true
            }
        }

        component.permissions.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            component.contacts.getIsContactBlocked(view.contactId) {
                view.isBlockContactVisible = !it
                view.isUnblockContactVisible = it
            }
        }
    }

    override fun onBlockClick() {
        toggleContactBlocked(true)
    }

    override fun onUnblockClick() {
        toggleContactBlocked(false)
    }

    override fun onFavoriteClick() {
        toggleContactFavorite(true)
    }

    override fun onUnFavoriteClick() {
        toggleContactFavorite(false)
    }

    private fun toggleContactBlocked(isBlock: Boolean) {
        component.permissions.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            if (isBlock) {
                component.permissions.runWithPrompt(R.string.explain_block_contact) {
                    if(it) {
                        component.contacts.blockContact(view.contactId) {
                            view.showMessage(R.string.contact_blocked)
                        }
                    }
                }
            } else {
                component.contacts.unblockContact(view.contactId) {
                    view.showMessage(
                        R.string.contact_unblocked
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun toggleContactFavorite(isFavorite: Boolean) {
        component.permissions.runWithPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS),
            {
                component.contacts.toggleContactFavorite(view.contactId, isFavorite)
            })
    }
}