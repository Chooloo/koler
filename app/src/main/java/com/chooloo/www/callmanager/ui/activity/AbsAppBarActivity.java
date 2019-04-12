package com.chooloo.www.callmanager.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public abstract class AbsAppBarActivity extends AppCompatActivity {

    SearchBarFragment mSearchBarFragment;

    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mTextTitle;
    @BindView(R.id.search_bar_container) FrameLayout mSearchBarContainer;

    @Override
    @CallSuper
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
     */
    public void toggleSearchBar(boolean isShow) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        if (isShow) {
            mSearchBarContainer.setVisibility(View.VISIBLE);
            ft.show(mSearchBarFragment);
            mSearchBarFragment.setFocus();
            Utilities.toggleKeyboard(this, mSearchBarFragment.mSearchInput, true);
        } else {
            mSearchBarContainer.setVisibility(View.GONE);
            ft.hide(mSearchBarFragment);
            Utilities.toggleKeyboard(this, mSearchBarFragment.mSearchInput, false);
        }
        ft.commit();
    }

    public boolean isSearchBarVisible() {
        return mSearchBarContainer.getVisibility() == View.VISIBLE;
    }
}
