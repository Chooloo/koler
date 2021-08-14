package com.chooloo.www.koler.ui.recentpreferences

import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment

class RecentPreferencesFragment : BasePreferenceFragment(), RecentPreferencesContract.View {
    private val _number by lazy { argsSafely.getString(ARG_NUMBER) }
    private val _contactsManager by lazy { ContactsUtils(baseActivity) }
    private val _permissionsManager by lazy { PermissionsManager(baseActivity) }
    private val _presenter by lazy { RecentPreferencesPresenter<RecentPreferencesContract.View>(this) }

    override val preferenceResource = R.xml.recent_preferences


    override fun onSetup() {
        super.onSetup()

        getPreference<Preference>(R.string.pref_key_block_number)?.isVisible =
            _number?.let { !_contactsManager.queryIsNumberBlocked(it) } ?: false
        getPreference<Preference>(R.string.pref_key_unblock_number)?.isVisible =
            _number?.let { _contactsManager.queryIsNumberBlocked(it) } ?: false
    }

    override fun onPreferenceClickListener(preference: Preference) {
        when (preference.key) {
            getString(R.string.pref_key_block_number) -> _presenter.onBlockNumberClick()
            getString(R.string.pref_key_unblock_number) -> _presenter.onUnblockNumberClick()
        }
    }

    //region recent preferences view

    override fun toggleNumberBlocked(isBlocked: Boolean) {
        if (isBlocked) {
            _number?.let {
                _permissionsManager.runWithPrompt(R.string.warning_block_number) {
                    _contactsManager.blockNumber(it)
                    showMessage(R.string.number_blocked)
                }
            }
        } else {
            _number?.let {
                _contactsManager.unblockNumber(it)
                showMessage(R.string.number_unblocked)
            }
        }
    }

    //endregion


    companion object {
        const val ARG_NUMBER = "number"

        fun newInstance(number: String) = RecentPreferencesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NUMBER, number)
            }
        }
    }
}