package com.chooloo.www.callmanager.ui.settings;

import android.os.Bundle;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.settings_fragment.SettingsFragment;
import com.chooloo.www.callmanager.util.ThemeUtils;

import java.util.Objects;

import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements SettingsMvpView {
    private static final String TAG_FRAGMENT = "fragment";

    @Override
    public int getContentView() {
        return R.layout.activity_settings;
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
