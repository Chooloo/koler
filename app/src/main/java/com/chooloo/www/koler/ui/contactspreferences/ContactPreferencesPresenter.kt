package com.chooloo.www.koler.ui.contactspreferences

import android.Manifest
import android.annotation.SuppressLint
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPreferencesPresenter<V : ContactPreferencesContract.View>(view: V) :
    BasePresenter<V>(view),
    ContactPreferencesContract.Presenter<V> {

    override fun onStart() {
        view.apply {
            isBlockContactVisible = false
            isUnblockContactVisible = false
            boundComponent.contactsInteractor.getContact(view.contactId) { contact ->
                isFavoriteContactVisible = contact?.starred == false
                isUnfavoriteContactVisible = contact?.starred == true
            }
        }

        boundComponent.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            boundComponent.contactsInteractor.getIsContactBlocked(view.contactId) {
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
        boundComponent.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            if (isBlock) {
                boundComponent.permissionInteractor.runWithPrompt(R.string.warning_block_contact) {
                    boundComponent.contactsInteractor.blockContact(view.contactId) {
                        view.showMessage(R.string.contact_blocked)
                    }
                }
            } else {
                boundComponent.contactsInteractor.unblockContact(view.contactId) {
                    view.showMessage(
                        R.string.contact_unblocked
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun toggleContactFavorite(isFavorite: Boolean) {
        boundComponent.permissionInteractor.runWithPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS),
            {
                boundComponent.contactsInteractor.toggleContactFavorite(view.contactId, isFavorite)
            })
    }
}