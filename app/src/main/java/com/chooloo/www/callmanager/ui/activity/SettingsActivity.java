package com.chooloo.www.callmanager.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.SettingsFragment;
import com.chooloo.www.callmanager.util.ThemeUtils;

import java.util.Objects;

//TODO add icons
public class SettingsActivity extends AbsThemeActivity {

    private static final String TAG_FRAGMENT = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_settings);

        Fragment fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, TAG_FRAGMENT)
                .commit();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
