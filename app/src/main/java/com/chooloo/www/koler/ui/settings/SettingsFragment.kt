package com.chooloo.www.koler.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.main.MainActivity
import com.chooloo.www.koler.util.preferences.PreferencesManager
import dev.sasikanth.colorsheet.ColorSheet
import timber.log.Timber
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    private val _presenter by lazy { SettingsPresenter<SettingsContract.View>() }
    private val _preferencesManager by lazy { context?.let { PreferencesManager(it) } }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun setPrefSim(value: String?) {
        _preferencesManager?.putString(R.string.pref_key_sim_select, value)
    }

    override fun setPrefColor(value: String?) {
        _preferencesManager?.putString(R.string.pref_key_color, value)
    }

    override fun setPrefRecordFormat(value: String?) {
        _preferencesManager?.putString(R.string.pref_key_record_format, value)
    }

    override fun setupSimPreference() {
        val simSelectionPreference =
            findPreference<ListPreference>(getString(R.string.pref_key_sim_select))

        @SuppressLint("MissingPermission")
        val subscriptionInfoList =
            activity?.getSystemService(SubscriptionManager::class.java)?.activeSubscriptionInfoList
        val simCount = subscriptionInfoList?.size

        if (simCount == 1) {
//            simSelectionPreference?.summary = getString(R.string.pref_sim_select_disabled)
            simSelectionPreference?.isEnabled = false
        } else if (simCount != null) {
            val simsEntries: MutableList<CharSequence> = ArrayList()
            for (i in 0 until simCount) {
                val si = subscriptionInfoList[i]
                Timber.i("Sim info " + i + " : " + si.displayName)
                simsEntries.add(si.displayName)
            }
            val simsEntriesList = simsEntries.toTypedArray()
            simSelectionPreference?.entries = simsEntriesList
            // simsEntries.add(getString(R.string.pref_sim_select_ask_entry));
            val simsEntryValues = arrayOf<CharSequence>("0", "1")
            simSelectionPreference?.entryValues = simsEntryValues
        }
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