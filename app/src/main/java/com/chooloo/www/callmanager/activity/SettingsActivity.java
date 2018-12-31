package com.chooloo.www.callmanager.activity;

import android.os.Bundle;

import com.chooloo.www.callmanager.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

//TODO add more settings
//TODO add icons
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "fragment";

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, mFragment, TAG_FRAGMENT)
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference, rootKey);

            //Init preferences
            ListPreference endCallTimerPref = (ListPreference) findPreference(getString(R.string.pref_end_call_timer_key));
            endCallTimerPref.setSummary(endCallTimerPref.getEntry());
            endCallTimerPref.setOnPreferenceChangeListener((preference, newValue) -> {
                endCallTimerPref.setSummary(endCallTimerPref.getEntry());
                return true;
            });
        }
    }
}
