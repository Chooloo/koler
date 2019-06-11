package com.chooloo.www.callmanager.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.chooloo.www.callmanager.BuildConfig;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CustomPagerAdapter;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.dialog.ChangelogDialog;
import com.chooloo.www.callmanager.ui.fragment.DialpadFragment;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodels.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodels.SharedSearchViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chooloo.www.callmanager.util.BiometricUtils.showBiometricPrompt;

public class MainActivity extends AbsSearchBarActivity {

    private static final String TAG_CHANGELOG_DIALOG = "changelog";
    boolean mIsBiometric;

    // Intent
    Intent mIntent;
    String mIntentAction;
    String mIntentType;

    // View Models
    SharedDialViewModel mSharedDialViewModel;
    SharedSearchViewModel mSharedSearchViewModel;

    //Coordinator
    FABCoordinator mFABCoordinator;

    // Fragments
    DialpadFragment mDialpadFragment;
    SearchBarFragment mSearchBarFragment;

    BottomSheetBehavior mBottomSheetBehavior;

    FragmentPagerAdapter mAdapterViewPager;
    BiometricPrompt mBiometricPrompt;

    // - View Binds - //

    // Views
    @BindView(R.id.appbar) View mAppBar;
    @BindView(R.id.dialer_fragment) View mDialerView;
    // Layouts
    @BindView(R.id.root_view) CoordinatorLayout mMainLayout;
    // Buttons
    @BindView(R.id.right_button) FloatingActionButton mRightButton;
    @BindView(R.id.left_button) FloatingActionButton mLeftButton;
    // Other
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.view_pager_tab) SmartTabLayout mSmartTabLayout;

    // -- Overrides -- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NO_ACTION_BAR);
        setContentView(R.layout.activity_main);
        PreferenceUtils.getInstance(this); // Get the preferences
        Utilities.setUpLocale(this);

        // Check if first instance
        boolean isFirstInstance = PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key);
        if (!isFirstInstance) checkVersion();

        // Bind variables
        ButterKnife.bind(this);

        // Check wither this app was set as the default dialer
        boolean isDefaultDialer = Utilities.checkDefaultDialer(this);
        if (isDefaultDialer) checkPermissions(null);

        // View Pager
        mAdapterViewPager = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
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
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        mSharedDialViewModel.getNumber().observe(this, n -> {
            if (n == null || n.length() == 0) {
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // Bottom Sheet Behavior
        mBottomSheetBehavior = BottomSheetBehavior.from(mDialerView); // Set the bottom sheet behaviour
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); // Hide the bottom sheet
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

        // Set default page
        int pagePreference = Integer.parseInt(PreferenceUtils.getInstance().getString(R.string.pref_default_page_key));
        mViewPager.setCurrentItem(pagePreference);

        // Add the dialer fragment
        mDialpadFragment = DialpadFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.dialer_fragment, mDialpadFragment)
                .commit();

        // Check for intents from others apps
        checkIncomingIntent();

        showBiometricPrompt(this);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof SearchBarFragment) {
            mSearchBarFragment = (SearchBarFragment) fragment;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        syncFABAndFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_about: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Check for first instance
        boolean isFirstInstance = PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key);

        if (isFirstInstance) {
            PreferenceUtils.getInstance().putBoolean(R.string.pref_is_first_instance_key, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utilities.DEFAULT_DIALER_RC) {
            checkPermissions(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissions(grantResults);
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
     *
     * @return Fragment
     */
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + mViewPager.getCurrentItem());
    }

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
     * @param expand
     */
    public void expandDialer(boolean expand) {
        if (expand) {
            BottomSheetBehavior.from(mDialerView).setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            BottomSheetBehavior.from(mDialerView).setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    public void updateButtons() {
        updateButtons(mBottomSheetBehavior.getState());
    }

    public void updateButtons(int bottomSheetState) {
        if (bottomSheetState == BottomSheetBehavior.STATE_HIDDEN || bottomSheetState == BottomSheetBehavior.STATE_COLLAPSED) {
            showButtons(true);
        } else {
            showButtons(false);
        }
    }

    /**
     * Animate action buttons
     *
     * @param isShow animate to visible/invisible
     */
    public void showButtons(boolean isShow) {
        View[] buttons = {mRightButton, mLeftButton};
        for (View v : buttons) {
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

    // -- Utilities -- //

    private void checkPermissions(@Nullable int[] grantResults) {
        if (
                (grantResults != null && Utilities.checkPermissionsGranted(grantResults)) ||
                        Utilities.checkPermissionsGranted(this, Utilities.MUST_HAVE_PERMISSIONS)) { //If granted
            checkVersion();
        } else {
            Utilities.askForPermissions(this, Utilities.MUST_HAVE_PERMISSIONS);
        }
    }

    /**
     * Check for the app version
     */
    private void checkVersion() {
        int lastVersionCode = PreferenceUtils.getInstance().getInt(R.string.pref_last_version_key);
        if (lastVersionCode < BuildConfig.VERSION_CODE) {
            PreferenceUtils.getInstance().putInt(R.string.pref_last_version_key, BuildConfig.VERSION_CODE);
            new ChangelogDialog().show(getSupportFragmentManager(), TAG_CHANGELOG_DIALOG);
        }
    }

    // -- Other -- //

    /**
     * Checking for incoming intents from other applications
     */
    private void checkIncomingIntent() {
        mIntent = getIntent();
        mIntentAction = mIntent.getAction();
        mIntentType = mIntent.getType();

        if (Intent.ACTION_VIEW.equals(mIntentAction)) {
            handleViewIntent(mIntent);
        }
    }

    /**
     * Handle a VIEW intent (For example when you click a number in whatsapp)
     *
     * @param intent
     */
    private void handleViewIntent(Intent intent) {
        String sharedText = intent.getData().toString();
        String number = "";
        if (sharedText.contains("tel:")) {
            number = sharedText.substring(4, sharedText.length() - 1);

            if (number != null) {
                mSharedDialViewModel.setNumber(number);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }
}
