package com.chooloo.www.koler.ui.contactspreferences

import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment

class ContactPreferencesFragment : BasePreferenceFragment(), ContactPreferencesContract.View {
    override val preferenceResource = R.xml.contact_preferences
    override val contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private lateinit var _presenter: ContactPreferencesPresenter<ContactPreferencesFragment>

    override var isBlockContactVisible: Boolean
        get() = getPreference<Preference>(R.string.pref_key_block_contact)?.isVisible == true
        set(value) {
            getPreference<Preference>(R.string.pref_key_block_contact)?.isVisible = value
        }

    override var isUnblockContactVisible: Boolean
        get() = getPreference<Preference>(R.string.pref_key_unblock_contact)?.isVisible == true
        set(value) {
            getPreference<Preference>(R.string.pref_key_unblock_contact)?.isVisible = value
        }

    override var isFavoriteContactVisible: Boolean
        get() = getPreference<Preference>(R.string.pref_key_set_favorite)?.isVisible == true
        set(value) {
            getPreference<Preference>(R.string.pref_key_set_favorite)?.isVisible = value
        }

    override var isUnfavoriteContactVisible: Boolean
        get() = getPreference<Preference>(R.string.pref_key_unset_favorite)?.isVisible == true
        set(value) {
            getPreference<Preference>(R.string.pref_key_unset_favorite)?.isVisible = value
        }


    override fun onSetup() {
        _presenter = ContactPreferencesPresenter(this)
        super.onSetup()
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


    companion object {
        const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactPreferencesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}