package com.chooloo.www.koler.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.main.MainActivity
import dev.sasikanth.colorsheet.ColorSheet
import timber.log.Timber
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    private val _presenter by lazy { SettingsPresenter<SettingsContract.View>() }

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

        getPreference<ListPreference>(R.string.pref_key_sim_select)?.setOnPreferenceChangeListener { _, newValue ->
            _presenter.onSimSelectionChanged(newValue)
            true
        }
        getPreference<Preference>(R.string.pref_key_app_color)?.setOnPreferenceClickListener {
            ColorSheet().colorPicker(
                colors = resources.getIntArray(R.array.accent_colors),
                listener = _presenter::onAppThemeSelectionChanged,
                noColorOption = true
            ).show(childFragmentManager)
            true
        }
        setupSimSelection()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun setupSimSelection() {
        val simSelectionPreference =
            findPreference<ListPreference>(getString(R.string.pref_key_sim_select))

        @SuppressLint("MissingPermission")
        val subscriptionInfoList =
            activity?.getSystemService(SubscriptionManager::class.java)?.activeSubscriptionInfoList
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

    override fun setAppColor(color: Int) {
        activity?.theme?.resolveAttribute(R.attr.colorSecondary, TypedValue().apply { }, true)
        when (color) {
            getColor(R.color.blue_light) -> {

            }
            getColor(R.color.green_light) -> {

            }
            getColor(R.color.orange_light) -> {

            }
            getColor(R.color.purple_light) -> {

            }
            getColor(R.color.red_light) -> {

            }
        }
    }

    private fun <T : Preference> getPreference(@StringRes keyString: Int) =
        findPreference<T>(getString(keyString))

    override fun goToMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
    }

    override fun hasPermission(permission: String) = true
    override fun hasPermissions(permissions: Array<String>) = true
    override fun showMessage(stringResId: Int) = showMessage(getString(stringResId))
    override fun showError(message: String) = showMessage(message)
    override fun showError(stringResId: Int) = showMessage(stringResId)
    override fun getColor(color: Int) = resources.getColor(color)
    override fun showMessage(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}