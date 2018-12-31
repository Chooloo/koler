package com.chooloo.www.callmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;

import com.chooloo.www.callmanager.R;

import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class MainActivity extends ToolbarActivity {

    private static final String TAG_FRAGMENT = "fragment";

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();
        //noinspection ConstantConditions
        if (!getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }

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
