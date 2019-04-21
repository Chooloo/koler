package com.chooloo.www.callmanager.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CustomPagerAdapter;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.fragment.DialFragment;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.ui.fragment.SharedDialViewModel;
import com.chooloo.www.callmanager.ui.fragment.SharedSearchViewModel;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static com.chooloo.www.callmanager.util.Utilities.askForPermissions;
import static com.chooloo.www.callmanager.util.Utilities.checkPermissionGranted;

public class MainActivity extends AbsSearchBarActivity {

    // View Models
    SharedDialViewModel mSharedDialViewModel;
    SharedSearchViewModel mSharedSearchViewModel;

    //Coordinator
    FABCoordinator mFABCoordinator;

    // Fragments
    DialFragment mDialFragment;
    SearchBarFragment mSearchBarFragment;

    BottomSheetBehavior mBottomSheetBehaviour;

    FragmentPagerAdapter mAdapterViewPager;

    // - View Binds - //

    // Views
    @BindView(R.id.appbar) View mAppBar;
    @BindView(R.id.main_dialer_fragment) View mDialerFragmentLayout;
    // Layouts
    @BindView(R.id.top_dialer) RelativeLayout mTopDialer;
    @BindView(R.id.root_view) CoordinatorLayout mMainLayout;
    // Buttons
    @BindView(R.id.right_button) FloatingActionButton mRightButton;
    @BindView(R.id.left_button) FloatingActionButton mLeftButton;
    // Other
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.view_pager_tab) SmartTabLayout mSmartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceUtils.getInstance(this);

        // Init timber
        Timber.plant(new Timber.DebugTree());

        // Bind variables
        ButterKnife.bind(this);

        // Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();
        if (!Objects.requireNonNull(getSystemService(TelecomManager.class)).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Utilities.sLocale = getResources().getSystem().getConfiguration().getLocales().get(0);
        } else {
            Utilities.sLocale = getResources().getSystem().getConfiguration().locale;
        }

        // Ask for permissions
        if (!checkPermissionGranted(this, CALL_PHONE) || !checkPermissionGranted(this, SEND_SMS) || !checkPermissionGranted(this, READ_CALL_LOG)) {
            askForPermissions(this, new String[]{CALL_PHONE, READ_CONTACTS, SEND_SMS, READ_CALL_LOG});
        }

        mAdapterViewPager = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                syncFABAndFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSmartTabLayout.setViewPager(mViewPager);

        // Search Bar View Model
        mSharedSearchViewModel = ViewModelProviders.of(this).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getIsFocused().observe(this, f -> {
            if (f) {
                expandAppBar(true);
            }
        });

        // Dial View Model
        mSharedDialViewModel = ViewModelProviders.of(this).get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsOutOfFocus().observe(this, b -> {
            if (b) {
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // - Bottom Sheet Behavior - //

        mBottomSheetBehaviour = BottomSheetBehavior.from(mDialerFragmentLayout); // Set the bottom sheet behaviour
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN); // Hide the bottom sheet
        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN || i == BottomSheetBehavior.STATE_COLLAPSED) {
                    showButtons(true);
                } else {
                    showButtons(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        //Initialize FABCoordinator
        mFABCoordinator = new FABCoordinator(mRightButton, mLeftButton);
        syncFABAndFragment();
    }

    // -- Overrides -- //

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        // Set this class as the listener for the fragments
        if (fragment instanceof DialFragment) {
            mDialFragment = (DialFragment) fragment;
        } else if (fragment instanceof SearchBarFragment) {
            mSearchBarFragment = (SearchBarFragment) fragment;
        }
    }

    /**
     * Triggered when the user gives some kind of a permission
     * (Usually through a permission dialog)
     *
     * @param requestCode
     * @param permissions  the permissions given
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isFirstInstance = PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key);

        // If this is the first time the user opens the app
        if (isFirstInstance) {
            PreferenceUtils.getInstance().putBoolean(R.string.pref_is_first_instance_key, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Clear focus on touch outside for all EditText inputs.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        syncFABAndFragment();
    }

    // -- OnClicks -- //

    @OnClick(R.id.right_button)
    public void fabRightClick() {
        mFABCoordinator.performRightClick();
    }

    @OnClick(R.id.left_button)
    public void fabLeftClick() {
        mFABCoordinator.performLeftClick();
    }

    // -- Fragments -- //

    /**
     * Returns the currently displayed fragment. Based on <a href="this">https://stackoverflow.com/a/18611036/5407365</a> answer
     * @return Fragment
     */
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + mViewPager.getCurrentItem());
    }

    public void syncFABAndFragment() {
        Fragment fragment = getCurrentFragment();
        mFABCoordinator.setListener(fragment);
        showButtons(true);
    }

    // -- UI -- //

    /**
     * Change the dialer status (collapse/expand)
     *
     * @param expand
     */
    public void expandDialer(boolean expand) {
        if (expand) {
            BottomSheetBehavior.from(mDialerFragmentLayout).setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            BottomSheetBehavior.from(mDialerFragmentLayout).setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    /**
     * Extend/Collapse the appbar_main according to given parameter
     *
     * @param expand
     */
    public void expandAppBar(boolean expand) {
        mAppBarLayout.setExpanded(expand);
    }

    /**
     * Animate action buttons
     *
     * @param isShow animate to visible/invisible
     */
    public void showButtons(boolean isShow) {
        if (isShow && mRightButton.isEnabled())
            mRightButton.animate().scaleX(1).scaleY(1).setDuration(100).start();
        else mRightButton.animate().scaleX(0).scaleY(0).setDuration(100).start();
        if (isShow && mLeftButton.isEnabled())
            mLeftButton.animate().scaleX(1).scaleY(1).setDuration(100).start();
        else mLeftButton.animate().scaleX(0).scaleY(0).setDuration(100).start();
    }
}
