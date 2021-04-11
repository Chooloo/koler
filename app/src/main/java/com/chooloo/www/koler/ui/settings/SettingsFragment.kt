package com.chooloo.www.koler.ui.settings

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.main.MainActivity
import com.chooloo.www.koler.util.preferences.KolerPreferences
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Page
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.RecordFormat
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Sim
import dev.sasikanth.colorsheet.ColorSheet

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    private val _presenter by lazy { SettingsPresenter<SettingsContract.View>() }
    private val _kolerPreferences by lazy { context?.let { KolerPreferences(it) } }

    private val ratePreference by lazy { getPreference<Preference>(R.string.pref_key_rate) }
    private val colorPreference by lazy { getPreference<Preference>(R.string.pref_key_color) }
    private val emailPreference by lazy { getPreference<Preference>(R.string.pref_key_email) }
    private val donatePreference by lazy { getPreference<Preference>(R.string.pref_key_donate) }
    private val reportPreference by lazy { getPreference<Preference>(R.string.pref_key_report_bugs) }
    private val simPreference by lazy { getPreference<ListPreference>(R.string.pref_key_sim_select) }
    private val recordPreference by lazy { getPreference<ListPreference>(R.string.pref_key_record_format) }
    private val compactPreference by lazy { getPreference<SwitchPreference>(R.string.pref_key_compact) }
    private val animationsPreference by lazy { getPreference<SwitchPreference>(R.string.pref_key_animations) }
    private val defaultPagePreference by lazy { getPreference<ListPreference>(R.string.pref_key_default_page) }

    companion object {
        const val TAG = "settings_fragment"

        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
        setDividerHeight(0)
        onSetup()
    }

    override fun onSetup() {
        _presenter.attach(this)

        ratePreference?.setOnPreferenceClickListener { _presenter.onClickedRate() }
        donatePreference?.setOnPreferenceClickListener { _presenter.onClickedDonate() }
        emailPreference?.setOnPreferenceClickListener { _presenter.onClickedEmail() }
        reportPreference?.setOnPreferenceClickListener { _presenter.onClickedReport() }
        colorPreference?.setOnPreferenceClickListener { _presenter.onClickedColor() }
        simPreference?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onSelectedSim(newValue)
        }
        recordPreference?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onSelectedRecordFormat(newValue)
        }
        compactPreference?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onToggledCompactMode(newValue as Boolean)
        }
        animationsPreference?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onToggledAnimation(newValue as Boolean)
        }
        defaultPagePreference?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onSelectedDefaultPage(newValue as String)
        }

        setupSimPreference()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun setPrefSim(sim: Sim) {
        _kolerPreferences?.sim = sim
    }

    override fun setPrefDefaultPage(page: Page) {
        _kolerPreferences?.defaultPage = page
    }

    override fun setPrefCompact(isCompact: Boolean) {
        _kolerPreferences?.isCompact = isCompact
    }

    override fun setPrefAnimations(isAnimations: Boolean) {
        _kolerPreferences?.isAnimations = isAnimations
    }

    override fun setPrefAccentTheme(accentTheme: AccentTheme) {
        _kolerPreferences?.accentTheme = accentTheme
    }


    override fun setPrefRecordFormat(recordFormat: RecordFormat) {
        _kolerPreferences?.recordFormat = recordFormat
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
        startActivity(Intent(activity, MainActivity::class.java))
    }

    override fun setupSimPreference() {
    }

    private fun <T : Preference> getPreference(@StringRes keyString: Int) =
        findPreference<T>(getString(keyString))

    //region base view
    override fun hasPermission(permission: String) = true
    override fun hasPermissions(permissions: Array<String>) = true
    override fun showMessage(stringResId: Int) = showMessage(getString(stringResId))
    override fun showError(message: String) = showMessage(message)
    override fun showError(stringResId: Int) = showMessage(stringResId)
    override fun getColor(color: Int) = resources.getColor(color)
    override fun showMessage(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    //endregion
}