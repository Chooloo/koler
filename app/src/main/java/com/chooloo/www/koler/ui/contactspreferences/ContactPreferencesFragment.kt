package com.chooloo.www.koler.ui.contactspreferences

import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.permissions.runWithDefaultDialer
import com.chooloo.www.koler.util.permissions.runWithPrompt

class ContactPreferencesFragment : BasePreferenceFragment(), ContactPreferencesContract.View {
    private val _contact by lazy { baseActivity.lookupContactId(argsSafely.getLong(ARG_CONTACT_ID)) }
    private val _presenter by lazy {
        ContactPreferencesPresenter<ContactPreferencesContract.View>(this)
    }


    override val preferenceResource = R.xml.contact_preferences

    
    override fun onSetup() {
        super.onSetup()

        getPreference<Preference>(R.string.pref_key_unset_favorite)?.isVisible = _contact.starred
        getPreference<Preference>(R.string.pref_key_set_favorite)?.isVisible = !_contact.starred
        getPreference<Preference>(R.string.pref_key_block_contact)?.isVisible =
            !baseActivity.isContactBlocked(_contact)
        getPreference<Preference>(R.string.pref_key_unblock_contact)?.isVisible =
            baseActivity.isContactBlocked(_contact)
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
        baseActivity.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
            if (isBlock) {
                baseActivity.runWithPrompt(R.string.warning_block_contact) {
                    _contact.phoneAccounts.forEach { baseActivity.blockNumber(it.number) }
                    showMessage(R.string.contact_blocked)
                }
            } else {
                _contact.phoneAccounts.forEach { baseActivity.unblockNumber(it.number) }
                showMessage(R.string.contact_unblocked)
            }
        }
    }

    override fun toggleContactFavorite(isFavorite: Boolean) {
        baseActivity.setContactFavorite(_contact.id, isFavorite)
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