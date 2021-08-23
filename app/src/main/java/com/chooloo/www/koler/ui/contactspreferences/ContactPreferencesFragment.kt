package com.chooloo.www.koler.ui.contactspreferences

import android.Manifest.permission.WRITE_CONTACTS
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment

class ContactPreferencesFragment : BasePreferenceFragment(), ContactPreferencesContract.View {
    override val preferenceResource = R.xml.contact_preferences

    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _contact by lazy { componentRoot.contactsInteractor.getContact(_contactId) }

    private val _presenter by lazy {
        ContactPreferencesPresenter<ContactPreferencesContract.View>(this)
    }

    override fun onSetup() {
        super.onSetup()

        getPreference<Preference>(R.string.pref_key_unset_favorite)?.isVisible = _contact!!.starred
        getPreference<Preference>(R.string.pref_key_set_favorite)?.isVisible = !_contact!!.starred

        boundComponent.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            getPreference<Preference>(R.string.pref_key_block_contact)?.isVisible =
                !componentRoot.contactsInteractor.isContactBlocked(_contactId)
            getPreference<Preference>(R.string.pref_key_unblock_contact)?.isVisible =
                componentRoot.contactsInteractor.isContactBlocked(_contactId)
        }
    }

    override fun onPreferenceClickListener(preference: Preference) {
        super.onPreferenceClickListener(preference)
        when (preference.key) {
            getString(R.string.pref_key_block_contact) -> _presenter.onBlockClick()
            getString(R.string.pref_key_unblock_contact) -> _presenter.onUnblockClick()
            getString(R.string.pref_key_set_favorite) -> _presenter.onFavoriteClick()
            getString(R.string.pref_key_unset_favorite) -> _presenter.onUnFavoriteClick()
        }
    }


    //region contact preferences view

    override fun toggleContactBlocked(isBlock: Boolean) {
        componentRoot.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            if (isBlock) {
                componentRoot.permissionInteractor.runWithPrompt(R.string.warning_block_contact) {
                    componentRoot.contactsInteractor.blockContact(_contactId)
                    showMessage(R.string.contact_blocked)
                }
            } else {
                componentRoot.contactsInteractor.unblockContact(_contactId)
                showMessage(R.string.contact_unblocked)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun toggleContactFavorite(isFavorite: Boolean) {
        componentRoot.permissionInteractor.runWithPermissions(arrayOf(WRITE_CONTACTS), {
            componentRoot.contactsInteractor.toggleContactFavorite(_contactId, isFavorite)
        }, null, null, null)
    }

    //endregion


    companion object {
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactPreferencesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}