package com.chooloo.www.callmanager.ui.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class AppBarActivity extends AppCompatActivity {

    SearchBarFragment mSearchBarFragment;

    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mTextTitle;

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);

        // Set text
        mTextTitle.setText(getTitle());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(20);
        // Insert the search bar fragment instead of the placeholder
        mSearchBarFragment = new SearchBarFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_bar_container, mSearchBarFragment).commit();
    }
}
