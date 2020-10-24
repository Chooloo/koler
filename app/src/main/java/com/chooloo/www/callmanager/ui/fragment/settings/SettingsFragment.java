package com.chooloo.www.callmanager.ui.fragment.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui2.activity.MainActivity;
import com.chooloo.www.callmanager.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.Manifest.permission.READ_PHONE_STATE;

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {

    private SettingsContract.Presenter<SettingsContract.View> mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mPresenter = new SettingsPresenter<>();
        mPresenter.bind(this, getLifecycle());
        mPresenter.subscribe(getContext());

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);

        findPreference(getString(R.string.pref_app_color_key)).setOnPreferenceChangeListener((preference, newValue) -> {
            mPresenter.refresh();
            return mPresenter.onListPreferenceChange(preference, newValue);
        });

        findPreference(getString(R.string.pref_app_theme_key)).setOnPreferenceChangeListener((preference, newValue) -> {
            mPresenter.refresh();
            return mPresenter.onListPreferenceChange(preference, newValue);
        });

        findPreference(getString(R.string.pref_excel_enable_key)).setOnPreferenceChangeListener((preference, newValue) -> {
            mPresenter.refresh();
            return mPresenter.onCheckBoxPreferenceChange(preference, newValue);
        });

        findPreference(getString(R.string.pref_reject_call_timer_key)).setOnPreferenceChangeListener((preference, newValue) -> mPresenter.onListPreferenceChange(preference, newValue));
        findPreference(getString(R.string.pref_answer_call_timer_key)).setOnPreferenceChangeListener(((preference, newValue) -> mPresenter.onListPreferenceChange(preference, newValue)));
        findPreference(getString(R.string.pref_default_page_key)).setOnPreferenceChangeListener((preference, newValue) -> mPresenter.onListPreferenceChange(preference, newValue));
        findPreference(getString(R.string.pref_sim_select_key)).setOnPreferenceChangeListener((preference, newValue) -> mPresenter.onListPreferenceChange(preference, newValue));
        findPreference(getString(R.string.pref_is_biometric_key)).setOnPreferenceChangeListener((preference, newValue) -> mPresenter.onSwitchPreferenceChange(preference, newValue));

        PermissionUtils.checkPermissionsGranted(getContext(), new String[]{READ_PHONE_STATE}, true);
        setupSimSelection();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionResult(requestCode, grantResults);
    }

    @Override
    public void setListPreferenceSummary(Preference preference, Object newValue) {
        ListPreference listPreference = (ListPreference) preference;
        CharSequence[] entries = listPreference.getEntries();
        listPreference.setSummary(entries[listPreference.findIndexOfValue((String) newValue)]);
    }

    @Override
    public void setCheckBoxPreferenceSummary(Preference preference, Object newValue) {
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
        checkBoxPreference.setSummary(checkBoxPreference.getSummary());
    }

    @Override
    public void setSwitchPreferenceSummary(Preference preference, Object newValue) {
        SwitchPreference switchPreference = (SwitchPreference) preference;
        switchPreference.setSummary(switchPreference.getSummary());
    }

    @Override
    public void setupSimSelection() {
        if (!PermissionUtils.checkPermissionsGranted(getContext(), new String[]{READ_PHONE_STATE}, true)) {
            Toast.makeText(getContext(), "No permission, please give permission to read phone state", Toast.LENGTH_LONG).show();
            return;
        }

        ListPreference simSelectionPreference = findPreference(getString(R.string.pref_sim_select_key));

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

    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
