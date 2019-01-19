package com.chooloo.www.callmanager.activity;

import android.os.Bundle;
import android.widget.Button;

import com.chooloo.www.callmanager.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import butterknife.OnClick;

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
            Preference.OnPreferenceChangeListener listChangeListener = (preference, newValue) -> {
                String[] entries = getResources().getStringArray(R.array.pref_call_timer_entries);
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setSummary(entries[listPreference.findIndexOfValue((String) newValue)]);
                return true;
            };

            ListPreference rejectCallTimerPreference = (ListPreference) findPreference(getString(R.string.pref_reject_call_timer_key));
            ListPreference answerCallTimerPreference = (ListPreference) findPreference(getString(R.string.pref_answer_call_timer_key));
            rejectCallTimerPreference.setOnPreferenceChangeListener(listChangeListener);
            rejectCallTimerPreference.setSummary(rejectCallTimerPreference.getEntry());
            answerCallTimerPreference.setOnPreferenceChangeListener(listChangeListener);
            answerCallTimerPreference.setSummary(answerCallTimerPreference.getEntry());
        }
    }
}
