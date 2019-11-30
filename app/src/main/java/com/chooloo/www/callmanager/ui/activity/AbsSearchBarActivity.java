package com.chooloo.www.callmanager.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodels.SharedSearchViewModel;

import butterknife.BindView;

public abstract class AbsSearchBarActivity extends AbsAppBarActivity {

    SharedSearchViewModel mSharedSearchViewModel;
    SearchBarFragment mSearchBarFragment;

    @BindView(R.id.search_bar_container) FrameLayout mSearchBarContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new search bar fragment
        mSearchBarFragment = new SearchBarFragment();
        // Replace the placeholder with the new fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.search_bar_container, mSearchBarFragment).commit();

        mSharedSearchViewModel = ViewModelProviders.of(this).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getIsFocused().observe(this, f -> {
            if (!f && mSharedSearchViewModel.getText().getValue().length() == 0) {
                toggleSearchBar(false);
            }
        });
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

    /**
     * Toggles the search bar according to the current state
     */
    public void toggleSearchBar() {
        toggleSearchBar(!isSearchBarVisible());
    }

    /**
     * Whether the search bar is visible or not
     *
     * @return
     */
    public boolean isSearchBarVisible() {
        return mSearchBarContainer.getVisibility() == View.VISIBLE;
    }
}
