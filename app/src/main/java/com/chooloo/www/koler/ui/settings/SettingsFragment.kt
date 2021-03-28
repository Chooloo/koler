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
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.main.MainActivity
import com.chooloo.www.koler.util.preferences.KolerPreferences
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.RecordFormat
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Sim
import dev.sasikanth.colorsheet.ColorSheet

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    private val _presenter by lazy { SettingsPresenter<SettingsContract.View>() }
    private val _kolerPreferences by lazy { context?.let { KolerPreferences(it) } }

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

        getPreference<Preference>(R.string.pref_key_rate)?.setOnPreferenceClickListener { _presenter.onClickedRate() }
        getPreference<Preference>(R.string.pref_key_donate)?.setOnPreferenceClickListener { _presenter.onClickedDonate() }
        getPreference<Preference>(R.string.pref_key_email)?.setOnPreferenceClickListener { _presenter.onClickedEmail() }
        getPreference<Preference>(R.string.pref_key_report_bugs)?.setOnPreferenceClickListener { _presenter.onClickedReport() }
        getPreference<Preference>(R.string.pref_key_color)?.setOnPreferenceClickListener { _presenter.onClickedColor() }
        getPreference<ListPreference>(R.string.pref_key_sim_select)?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onSelectedSim(newValue)
        }
        getPreference<ListPreference>(R.string.pref_key_record_format)?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onSelectedRecordFormat(newValue)
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

    override fun setPrefAccentTheme(accentTheme: AccentTheme) {
        _kolerPreferences?.accentTheme = accentTheme
    }

    override fun setPrefRecordFormat(recordFormat: RecordFormat) {
        _kolerPreferences?.recordFormat = recordFormat
    }

    override fun setupSimPreference() {
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

    override fun openSource() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.app_source_url))))
    }

    override fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
        })
    }

    override fun reportBug() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.app_bug_reporting_url))))
    }

    override fun rateApp() {
        activity?.let {
            startActivity(Intent(ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=" + it.application.packageName)
            })
        }
    }

    override fun donate() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.donation_link))))
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