package com.chooloo.www.callmanager.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.ActivitySettingsBinding;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.settings_fragment.SettingsFragment;
import com.chooloo.www.callmanager.util.ThemeUtils;

import java.util.Objects;

import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements SettingsMvpView {
    private static final String TAG_FRAGMENT = "fragment";
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void setUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, SettingsFragment.newInstance(), TAG_FRAGMENT)
                .commit();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
