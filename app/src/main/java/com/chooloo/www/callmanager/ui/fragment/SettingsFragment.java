package com.chooloo.www.callmanager.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.Manifest.permission.READ_PHONE_STATE;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);

        //Init preferences
        Preference.OnPreferenceChangeListener colorChangeListener = (preference, newValue) -> {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence[] entries = listPreference.getEntries();
            listPreference.setSummary(entries[listPreference.findIndexOfValue((String) newValue)]);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return true;
        };

        Preference.OnPreferenceChangeListener themeChangeListener = (preference, newValue) -> {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence[] entries = listPreference.getEntries();
            listPreference.setSummary(entries[listPreference.findIndexOfValue((String) newValue)]);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return true;
        };

        Preference.OnPreferenceChangeListener excelEnableChangeListener = (preference, newValue) -> {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
            checkBoxPreference.setSummary(checkBoxPreference.getSummary());

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return true;
        };

        Preference.OnPreferenceChangeListener listChangeListener = (preference, newValue) -> {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence[] entries = listPreference.getEntries();
            listPreference.setSummary(entries[listPreference.findIndexOfValue((String) newValue)]);
            return true;
        };

        Preference.OnPreferenceChangeListener switchChangeListener = (preference, newValue) -> {
            SwitchPreference switchPreference = (SwitchPreference) preference;
            switchPreference.setSummary(switchPreference.getSummary());
            return true;
        };

        // App Color
        ListPreference appColorPreference = (ListPreference) findPreference(getString(R.string.pref_app_color_key));
        appColorPreference.setOnPreferenceChangeListener(colorChangeListener);
        appColorPreference.setSummary(appColorPreference.getEntry());

        // App theme
        ListPreference appThemePreference = (ListPreference) findPreference(getString(R.string.pref_app_theme_key));
        appThemePreference.setOnPreferenceChangeListener(themeChangeListener);
        appThemePreference.setSummary(appThemePreference.getEntry());

        // End call timer
        ListPreference rejectCallTimerPreference = (ListPreference) findPreference(getString(R.string.pref_reject_call_timer_key));
        rejectCallTimerPreference.setOnPreferenceChangeListener(listChangeListener);
        rejectCallTimerPreference.setSummary(rejectCallTimerPreference.getEntry());

        // Answer call timer
        ListPreference answerCallTimerPreference = (ListPreference) findPreference(getString(R.string.pref_answer_call_timer_key));
        answerCallTimerPreference.setOnPreferenceChangeListener(listChangeListener);
        answerCallTimerPreference.setSummary(answerCallTimerPreference.getEntry());

        // Default page
        ListPreference defaultPagePreference = (ListPreference) findPreference(getString(R.string.pref_default_page_key));
        defaultPagePreference.setOnPreferenceChangeListener(listChangeListener);
        defaultPagePreference.setSummary(defaultPagePreference.getEntry());

        // Excel enable selection
        CheckBoxPreference excelEnablePreference = (CheckBoxPreference) findPreference(getString(R.string.pref_excel_enable_key));
        excelEnablePreference.setOnPreferenceChangeListener(excelEnableChangeListener);

        // Biometrics
        SwitchPreference isBiometricPreference = (SwitchPreference) findPreference(getString(R.string.pref_is_biometric_key));
        isBiometricPreference.setOnPreferenceChangeListener(switchChangeListener);

        // Sim selection
        ListPreference simSelectionPreference = (ListPreference) findPreference(getString(R.string.pref_sim_select_key));
        simSelectionPreference.setOnPreferenceChangeListener(listChangeListener);

        PermissionUtils.checkPermissionsGranted(getContext(), new String[]{READ_PHONE_STATE}, true);
        setupSimSelection();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.PERMISSION_RC && PermissionUtils.checkPermissionsGranted(grantResults)) {
            setupSimSelection();
        }
    }

    private void setupSimSelection() {
        if (!PermissionUtils.checkPermissionsGranted(getContext(), new String[]{READ_PHONE_STATE}, true)) {
            Toast.makeText(getContext(), "No permission, please give permission to read phone state", Toast.LENGTH_LONG).show();
            return;
        }

        ListPreference simSelectionPreference = (ListPreference) findPreference(getString(R.string.pref_sim_select_key));

        @SuppressLint("MissingPermission")
        List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.from(getContext()).getActiveSubscriptionInfoList();
        int simCount = subscriptionInfoList.size();

        if (simCount == 1) {
            simSelectionPreference.setSummary(getString(R.string.pref_sim_select_disabled));
            simSelectionPreference.setEnabled(false);
        } else {
            List<CharSequence> simsEntries = new ArrayList<>();

            for (int i = 0; i < simCount; i++) {
                SubscriptionInfo si = subscriptionInfoList.get(i);
                Timber.i("Sim info " + i + " : " + si.getDisplayName());
                simsEntries.add(si.getDisplayName());
            }

            CharSequence[] simsEntriesList = simsEntries.toArray(new CharSequence[simsEntries.size()]);
            simSelectionPreference.setEntries(simsEntriesList);
            // simsEntries.add(getString(R.string.pref_sim_select_ask_entry));
            CharSequence[] simsEntryValues = {"0", "1"};
            simSelectionPreference.setEntryValues(simsEntryValues);
        }
    }
}