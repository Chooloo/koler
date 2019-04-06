package com.chooloo.www.callmanager.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class AppBarActivity extends AppCompatActivity {

    SearchBarFragment mSearchBarFragment;

    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mTextTitle;
    @BindView(R.id.search_bar_container) FrameLayout mSearchBarContainer;

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);

        // Set text
        mTextTitle.setText(getTitle());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        // Create a new search bar fragment
        mSearchBarFragment = new SearchBarFragment();
        // Replace the placeholder with the new fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.search_bar_container, mSearchBarFragment).commit();
    }

    /**
     * Toggles the search bar according to it's current state
     * If the bar is visible -> hide it
     * If the bar is hidden  -> show it
     */
    public boolean toggleSearchBar() {
        boolean isOpened;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        if (mSearchBarContainer.getVisibility() == View.GONE) {
            mSearchBarContainer.setVisibility(View.VISIBLE);
            ft.show(mSearchBarFragment);
            mSearchBarFragment.setFocus();
            Utilities.toggleKeyboard(this, mSearchBarFragment.mSearchInput, true);
            isOpened = true;
        } else {
            mSearchBarContainer.setVisibility(View.GONE);
            ft.hide(mSearchBarFragment);
            Utilities.toggleKeyboard(this, mSearchBarFragment.mSearchInput, false);
            isOpened = false;
        }
        ft.commit();
        return isOpened;
    }
}
