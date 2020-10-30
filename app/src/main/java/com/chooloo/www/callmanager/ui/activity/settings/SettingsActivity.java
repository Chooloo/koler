package com.chooloo.www.callmanager.ui.activity.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.activity.base.BaseThemeActivity;
import com.chooloo.www.callmanager.util.ThemeUtils;

import java.util.Objects;

public class SettingsActivity extends BaseThemeActivity implements SettingsContract.View {
    private static final String TAG_FRAGMENT = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void setUp() {
        Fragment fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, TAG_FRAGMENT)
                .commit();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
