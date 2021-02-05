package com.chooloo.www.callmanager.ui.settings_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.view.View
import android.widget.Toast
import androidx.preference.*
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.ui.main.MainActivity
import timber.log.Timber
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), SettingsMvpView {
    private lateinit var _presenter: SettingsPresenter<SettingsMvpView>

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        findPreference<Preference>(getString(R.string.pref_app_color_key))?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            _presenter.refresh()
            _presenter.onListPreferenceChange(preference, newValue)
        }
        findPreference<Preference>(getString(R.string.pref_app_theme_key))?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            _presenter.refresh()
            _presenter.onListPreferenceChange(preference, newValue)
        }
        findPreference<Preference>(getString(R.string.pref_reject_call_timer_key))?.setOnPreferenceChangeListener { preference, newValue -> _presenter.onListPreferenceChange(preference, newValue) }
        findPreference<Preference>(getString(R.string.pref_answer_call_timer_key))?.setOnPreferenceChangeListener { preference, newValue -> _presenter.onListPreferenceChange(preference, newValue) }
        findPreference<Preference>(getString(R.string.pref_default_page_key))?.setOnPreferenceChangeListener { preference, newValue -> _presenter.onListPreferenceChange(preference, newValue) }
        findPreference<Preference>(getString(R.string.pref_sim_select_key))?.setOnPreferenceChangeListener { preference, newValue -> _presenter.onListPreferenceChange(preference, newValue) }
        findPreference<Preference>(getString(R.string.pref_is_biometric_key))?.setOnPreferenceChangeListener { preference, newValue -> _presenter.onSwitchPreferenceChange(preference, newValue) }

        setupSimSelection()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        _presenter.onRequestPermissionResult(requestCode, grantResults)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = SettingsPresenter()
        _presenter.attach(this)
    }

    override fun setListPreferenceSummary(preference: Preference, newValue: Any) {
        val listPreference = preference as ListPreference
        val entries = listPreference.entries
        listPreference.summary = entries[listPreference.findIndexOfValue(newValue as String)]
    }

    override fun setCheckBoxPreferenceSummary(preferenece: Preference, newValue: Any) {
        val checkBoxPreference = preferenece as CheckBoxPreference
        checkBoxPreference.summary = checkBoxPreference.summary
    }

    override fun setSwitchPreferenceSummary(preference: Preference, newValue: Any) {
        val switchPreference = preference as SwitchPreference
        switchPreference.summary = switchPreference.summary
    }

    override fun setupSimSelection() {
        TODO("implement normally")
        val simSelectionPreference = findPreference<ListPreference>(getString(R.string.pref_sim_select_key))

        @SuppressLint("MissingPermission")
        val subscriptionInfoList = activity?.getSystemService(SubscriptionManager::class.java)?.activeSubscriptionInfoList
        val simCount = subscriptionInfoList?.size

        if (simCount == 1) {
            simSelectionPreference?.summary = getString(R.string.pref_sim_select_disabled)
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

    override fun goToMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
    }

    override fun hasPermission(permission: String): Boolean {
        return true
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return true
    }

    override fun askForPermission(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

    override fun askForPermissions(permissions: Array<String>, requestCode: Int) {
        requestPermissions(permissions, requestCode)
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(stringResId: Int) {
        showMessage(getString(stringResId))
    }

    override fun showError(message: String) {
        showMessage(message)
    }

    override fun showError(stringResId: Int) {
        showMessage(stringResId)
    }
}