package com.chooloo.www.koler.ui.contactspreferences

import ContactsManager
import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment
import com.chooloo.www.koler.util.permissions.PermissionsManager

class ContactPreferencesFragment : BasePreferenceFragment(), ContactPreferencesContract.View {
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _contactsManager by lazy { ContactsManager(baseActivity) }
    private val _contact by lazy { _contactsManager.queryContact(_contactId) }
    private val _permissionsManager by lazy { PermissionsManager(baseActivity) }
    private val _presenter by lazy {
        ContactPreferencesPresenter<ContactPreferencesContract.View>(this)
    }


    override val preferenceResource = R.xml.contact_preferences


    override fun onSetup() {
        super.onSetup()

        getPreference<Preference>(R.string.pref_key_unset_favorite)?.isVisible = _contact.starred
        getPreference<Preference>(R.string.pref_key_set_favorite)?.isVisible = !_contact.starred
        getPreference<Preference>(R.string.pref_key_block_contact)?.isVisible =
            !_contactsManager.queryIsContactBlocked(_contact)
        getPreference<Preference>(R.string.pref_key_unblock_contact)?.isVisible =
            _contactsManager.queryIsContactBlocked(_contact)
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
        _permissionsManager.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            if (isBlock) {
                _permissionsManager.runWithPrompt(R.string.warning_block_contact) {
                    _contactsManager.blockContact(_contactId)
                    showMessage(R.string.contact_blocked)
                }
            } else {
                _contactsManager.unblockContact(_contactId)
                showMessage(R.string.contact_unblocked)
            }
        }
    }

    override fun toggleContactFavorite(isFavorite: Boolean) {
        _contactsManager.toggleContactFavorite(_contactId, isFavorite)
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