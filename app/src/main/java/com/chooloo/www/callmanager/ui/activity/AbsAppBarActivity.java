package com.chooloo.www.callmanager.ui.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public abstract class AbsAppBarActivity extends AppCompatActivity {

    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mTextTitle;

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
     * @param label
     */
    protected void setLabel(String label) {
        mTextTitle.setText(label);
    }
}
