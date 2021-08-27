package com.chooloo.www.koler.ui.recentpreferences

import android.os.Bundle
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment

class RecentPreferencesFragment : BasePreferenceFragment(), RecentPreferencesContract.View {
    private lateinit var _presenter: RecentPreferencesPresenter<RecentPreferencesFragment>

    override val number by lazy { argsSafely.getString(ARG_NUMBER) }
    override val preferenceResource = R.xml.recent_preferences

    override var isBlockNumberVisible: Boolean
        get() = getPreference<Preference>(R.string.pref_key_block_number)?.isVisible == true
        set(value) {
            getPreference<Preference>(R.string.pref_key_block_number)?.isVisible = value
        }

    override var isUnblockNumberVisible: Boolean
        get() = getPreference<Preference>(R.string.pref_key_unblock_number)?.isVisible == true
        set(value) {
            getPreference<Preference>(R.string.pref_key_unblock_number)?.isVisible = value
        }


    override fun onSetup() {
        super.onSetup()
        _presenter = RecentPreferencesPresenter(this)
    }

    override fun onPreferenceClickListener(preference: Preference) {
        when (preference.key) {
            getString(R.string.pref_key_block_number) -> _presenter.onBlockNumberClick()
            getString(R.string.pref_key_unblock_number) -> _presenter.onUnblockNumberClick()
        }
    }


    companion object {
        const val ARG_NUMBER = "number"

        fun newInstance(number: String) = RecentPreferencesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NUMBER, number)
            }
        }
    }
}