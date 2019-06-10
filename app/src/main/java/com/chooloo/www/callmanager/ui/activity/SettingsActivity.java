package com.chooloo.www.callmanager.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.ThemeUtils;

//TODO add more settings
//TODO add icons
public class SettingsActivity extends AbsThemeActivity {

    private static final String TAG_FRAGMENT = "fragment";

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_settings);

        mFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, mFragment, TAG_FRAGMENT)
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference, rootKey);

            //Init preferences
            Preference.OnPreferenceChangeListener themeChangeListener = (preference, newValue) -> {
                ListPreference listPreference = (ListPreference) preference;
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[listPreference.findIndexOfValue((String) newValue)]);

                getActivity().finish();
                startActivity(getActivity().getIntent());
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

            ListPreference appThemePreference = (ListPreference) findPreference(getString(R.string.pref_app_theme_key));
            appThemePreference.setOnPreferenceChangeListener(themeChangeListener);
            appThemePreference.setSummary(appThemePreference.getEntry());

            ListPreference rejectCallTimerPreference = (ListPreference) findPreference(getString(R.string.pref_reject_call_timer_key));
            rejectCallTimerPreference.setOnPreferenceChangeListener(listChangeListener);
            rejectCallTimerPreference.setSummary(rejectCallTimerPreference.getEntry());

            ListPreference answerCallTimerPreference = (ListPreference) findPreference(getString(R.string.pref_answer_call_timer_key));
            answerCallTimerPreference.setOnPreferenceChangeListener(listChangeListener);
            answerCallTimerPreference.setSummary(answerCallTimerPreference.getEntry());

            ListPreference defaultPagePreference = (ListPreference) findPreference(getString(R.string.pref_default_page_key));
            defaultPagePreference.setOnPreferenceChangeListener(listChangeListener);
            defaultPagePreference.setSummary(defaultPagePreference.getEntry());

//            SwitchPreference isSilentPreference = (SwitchPreference) findPreference(getString(R.string.pref_is_silent_key));
//            isSilentPreference.setOnPreferenceChangeListener(switchChangeListener);
//
//            SwitchPreference isNoVibratePreference = (SwitchPreference) findPreference(getString(R.string.pref_is_no_vibrate_key));
//            isNoVibratePreference.setOnPreferenceChangeListener(switchChangeListener);

            SwitchPreference isBiometricPreference = (SwitchPreference) findPreference(getString(R.string.pref_is_biometric_key));
            isBiometricPreference.setOnPreferenceChangeListener(switchChangeListener);
        }
    }
}
