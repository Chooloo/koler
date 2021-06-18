package com.chooloo.www.koler.ui.settings

import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.telecom.TelecomManager
import androidx.preference.Preference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePreferenceFragment
import com.chooloo.www.koler.ui.main.MainActivity
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Page
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.RecordFormat
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Sim
import dev.sasikanth.colorsheet.ColorSheet

class SettingsFragment : BasePreferenceFragment(), SettingsContract.View {
    private val _presenter by lazy { SettingsPresenter<SettingsContract.View>() }

    override val preferenceResource = R.xml.main_preferences

    
    override fun onSetup() {
        setupSimPreference()
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
            getString(R.string.pref_key_sim_select) -> _presenter.onSelectedSim(newValue)
            getString(R.string.pref_key_record_format) -> _presenter.onSelectedRecordFormat(newValue)
            getString(R.string.pref_key_compact) -> _presenter.onToggledCompactMode(newValue as Boolean)
            getString(R.string.pref_key_animations) -> _presenter.onToggledAnimation(newValue as Boolean)
            getString(R.string.pref_key_default_page) -> _presenter.onSelectedDefaultPage(newValue as String)
            getString(R.string.pref_key_records_enabled) -> _presenter.onToggledRecords(newValue as Boolean)
            getString(R.string.pref_key_scroll_indicator) -> _presenter.onToggledScrollIndicator(
                newValue as Boolean
            )
        }
    }


    //region settings view

    override fun setPrefSim(sim: Sim) {
        kolerPreferences?.sim = sim
    }

    override fun setPrefDefaultPage(page: Page) {
        kolerPreferences?.defaultPage = page
    }

    override fun setPrefCompact(isCompact: Boolean) {
        kolerPreferences?.compact = isCompact
    }

    override fun setPrefAnimations(isAnimations: Boolean) {
        kolerPreferences?.animations = isAnimations
    }

    override fun setPrefRecordsEnabled(isEnabled: Boolean) {
        kolerPreferences?.recordsEnabled = isEnabled
    }

    override fun setPrefScrollIndicator(isEnabled: Boolean) {
        kolerPreferences?.scrollIndicator = isEnabled
    }


    override fun setPrefAccentTheme(accentTheme: AccentTheme) {
        kolerPreferences?.accentTheme = accentTheme
    }

    override fun setPrefRecordFormat(recordFormat: RecordFormat) {
        kolerPreferences?.recordFormat = recordFormat
    }

    override fun donate() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.donation_link))))
    }

    override fun rateApp() {
        activity?.let {
            startActivity(Intent(ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=" + it.application.packageName)
            })
        }
    }

    override fun reportBug() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.app_bug_reporting_url))))
    }

    override fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
        })
    }

    override fun openSource() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.app_source_url))))
    }

    override fun openColorPicker() {
        ColorSheet().colorPicker(
            colors = resources.getIntArray(R.array.accent_colors),
            listener = _presenter::onSelectedColor,
            noColorOption = true
        ).show(childFragmentManager)
    }

    override fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun setupSimPreference() {
    }

    override fun manageBlockedNumbers() {
        context?.let {
            startActivity(
                (it.getSystemService(TELECOM_SERVICE) as TelecomManager).createManageBlockedNumbersIntent(),
                null
            )
        }
    }

    //endregion


    companion object {
        const val TAG = "settings_fragment"

        fun newInstance() = SettingsFragment()
    }
}