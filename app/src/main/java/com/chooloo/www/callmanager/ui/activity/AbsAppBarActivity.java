package com.chooloo.www.callmanager.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.chooloo.www.callmanager.R;
import com.google.android.material.appbar.AppBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public abstract class AbsAppBarActivity extends AbsThemeActivity {

    public @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);

        // Set text
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
    }
}
