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
    @BindView(R.id.toolbar_title) TextView mTextTitle;

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
        mTextTitle.setText(getTitle());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
    }

    /**
     * Set the label of the appbar by a given string
     *
     * @param label label to be displayed in the appbar
     */
    protected void setLabel(String label) {
        mTextTitle.setText(label);
    }
}
