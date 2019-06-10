package com.chooloo.www.callmanager.ui.activity;

import android.os.Bundle;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.ThemeUtils;

import butterknife.ButterKnife;

public class AboutActivity extends AbsThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}