package com.chooloo.www.callmanager.ui2.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui2.FABCoordinator;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;

public class MainActivity extends AbsSearchBarActivity {

    //Coordinator
    FABCoordinator mFABCoordinator;

    // Other
    BottomSheetBehavior mBottomSheetBehavior;
    // - View Binds - //

    // Views
    @BindView(R.id.appbar) View mAppBar;
    @BindView(R.id.dialer_fragment) View mDialerView;

    // Layouts
    @BindView(R.id.root_view) CoordinatorLayout mMainLayout;

    // Buttons
    @BindView(R.id.right_button) FloatingActionButton mRightButton;
    @BindView(R.id.left_button) FloatingActionButton mLeftButton;
    @BindView(R.id.add_contact_fab_button) FloatingActionButton mAddContactButton;

    // Other
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.view_pager_tab) SmartTabLayout mSmartTabLayout;

    // -- Overrides -- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Pager
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (isSearchBarVisible()) toggleSearchBar();
                syncFABAndFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });//


        // Bottom Sheet Behavior
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                updateButtons(i);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        // Initialize FABCoordinator
        mFABCoordinator = new FABCoordinator(mRightButton, mLeftButton, this);
        syncFABAndFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();
        syncFABAndFragment();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        syncFABAndFragment();
    }

    // -- OnClicks -- //



    // -- Fragments -- //

    /**
     * Apply the FABCoordinator to the current fragment
     */
    public void syncFABAndFragment() {
        Fragment fragment = getCurrentFragment();
        mFABCoordinator.setListener(fragment);
        updateButtons();
    }

    // -- UI -- //

    /**
     * Change the dialer status (collapse/expand)
     *
     * @param isExpand should expend dialer or not
     */
    public void expandDialer(boolean isExpand) {
        if (isExpand) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    public void updateButtons() {
        updateButtons(mBottomSheetBehavior.getState());
    }

    public void updateButtons(int bottomSheetState) {
        boolean isShow = !isBottomSheetOpen(bottomSheetState);
        showButtons(isShow);
        if (mViewPager.getCurrentItem() == 1) showView(mAddContactButton, isShow);
    }

    /**
     * Animate action buttons
     *
     * @param isShow animate to visible/invisible
     */
    public void showButtons(boolean isShow) {
        View[] buttons = {mRightButton, mLeftButton};
        for (View v : buttons) showView(v, isShow);
    }

    /**
     * Animate view
     *
     * @param isShow show view or not
     * @param v      view to handle
     */
    public void showView(View v, boolean isShow) {
        if (isShow && v.isEnabled()) {
            v.animate().scaleX(1).scaleY(1).setDuration(100).start();
            v.setClickable(true);
            v.setFocusable(true);
        } else {
            v.animate().scaleX(0).scaleY(0).setDuration(100).start();
            v.setClickable(false);
            v.setFocusable(false);
        }
    }

}
