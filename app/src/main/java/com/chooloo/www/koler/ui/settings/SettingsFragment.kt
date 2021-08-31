package com.chooloo.www.koler.ui.settings

import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment
import dev.sasikanth.colorsheet.ColorSheet

class SettingsFragment : BasePreferenceFragment(), SettingsContract.View {
    private lateinit var _presenter: SettingsPresenter<SettingsFragment>

    override val preferenceResource = R.xml.preferences_main


    override fun onSetup() {
        _presenter = SettingsPresenter(this)
    }

    override fun onPreferenceClickListener(preference: Preference) {
        when (preference.key) {
            getString(R.string.pref_key_rate) -> _presenter.onClickedRate()
            getString(R.string.pref_key_email) -> _presenter.onClickedEmail()
            getString(R.string.pref_key_donate) -> _presenter.onClickedDonate()
            getString(R.string.pref_key_report_bugs) -> _presenter.onClickedReport()
            getString(R.string.pref_key_color) -> _presenter.onClickedColor()
            getString(R.string.pref_key_manage_blocked) -> _presenter.onClickedManageBlocked()
        }
    }

    override fun onPreferenceChangeListener(preference: Preference, newValue: Any) {
        when (preference.key) {
            getString(R.string.pref_key_compact) -> _presenter.onToggledCompactMode(newValue as Boolean)
            getString(R.string.pref_key_animations) -> _presenter.onToggledAnimation(newValue as Boolean)
            getString(R.string.pref_key_default_page) -> _presenter.onSelectedDefaultPage(newValue as String)
            getString(R.string.pref_key_records_enabled) -> _presenter.onToggledRecords(newValue as Boolean)
            getString(R.string.pref_key_scroll_indicator) -> _presenter.onToggledScrollIndicator(
                newValue as Boolean
            )
        }
    }


    override fun openColorPicker() {
        ColorSheet().colorPicker(
            colors = resources.getIntArray(R.array.accent_colors),
            listener = _presenter::onSelectedColor,
            noColorOption = true
        ).show(childFragmentManager)
    }


    companion object {
        const val TAG = "settings_fragment"

        fun newInstance() = SettingsFragment()
    }
}