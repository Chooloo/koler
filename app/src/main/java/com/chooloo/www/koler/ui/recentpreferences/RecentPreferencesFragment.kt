package com.chooloo.www.koler.ui.recentpreferences

import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment
import com.chooloo.www.koler.util.blockNumber
import com.chooloo.www.koler.util.isNumberBlocked
import com.chooloo.www.koler.util.permissions.runWithPrompt
import com.chooloo.www.koler.util.unblockNumber

class RecentPreferencesFragment : BasePreferenceFragment(), RecentPreferencesContract.View {
    private val _number by lazy { argsSafely.getString(ARG_NUMBER) }
    private val _presenter by lazy { RecentPreferencesPresenter<RecentPreferencesContract.View>(this) }

    override val preferenceResource = R.xml.recent_preferences


    override fun onSetup() {
        super.onSetup()

        getPreference<Preference>(R.string.pref_key_block_number)?.isVisible =
            _number?.let { !baseActivity.isNumberBlocked(it) } ?: false
        getPreference<Preference>(R.string.pref_key_unblock_number)?.isVisible =
            _number?.let { baseActivity.isNumberBlocked(it) } ?: false
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
                baseActivity.runWithPrompt(R.string.warning_block_number) {
                    baseActivity.blockNumber(it)
                    showMessage(R.string.number_blocked)
                }
            }
        } else {
            _number?.let {
                baseActivity.unblockNumber(it)
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