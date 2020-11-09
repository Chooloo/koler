package com.chooloo.www.callmanager.ui.settings;

import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseThemeActivity;
import com.chooloo.www.callmanager.ui.settings_fragment.SettingsFragment;
import com.chooloo.www.callmanager.util.ThemeUtils;

import java.util.Objects;

import butterknife.ButterKnife;

public class SettingsActivity extends BaseThemeActivity implements SettingsMvpView {
    private static final String TAG_FRAGMENT = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_settings);

        setUp();
    }

    @Override
    public void setUp() {
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, SettingsFragment.newInstance(), TAG_FRAGMENT)
                .commit();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
