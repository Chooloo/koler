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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
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
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedIntentViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedSearchViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
    SharedIntentViewModel mSharedIntentViewModel;

    //Coordinator
    FABCoordinator mFABCoordinator;

    // Fragments
    DialpadFragment mDialpadFragment;
    SearchBarFragment mSearchBarFragment;
    FragmentPagerAdapter mAdapterViewPager;

    // Other
    BottomSheetBehavior mBottomSheetBehavior;
    BiometricPrompt mBiometricPrompt;
    Menu mMenu;

    // - View Binds - //

    // Views
    @BindView(R.id.appbar)
    View mAppBar;
    @BindView(R.id.dialer_fragment)
    View mDialerView;

    // Layouts
    @BindView(R.id.root_view)
    CoordinatorLayout mMainLayout;

    // Buttons
    @BindView(R.id.right_button)
    FloatingActionButton mRightButton;
    @BindView(R.id.left_button)
    FloatingActionButton mLeftButton;
    @BindView(R.id.add_contact_fab_button)
    FloatingActionButton mAddContactButton;

    // Other
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.view_pager_tab)
    SmartTabLayout mSmartTabLayout;

    // -- Overrides -- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set theme and view
        setThemeType(ThemeUtils.TYPE_NO_ACTION_BAR); // set theme
        setContentView(R.layout.activity_main); // set the layout

        // code settings
        PreferenceUtils.getInstance(this);
        Utilities.setUpLocale(this);
        ButterKnife.bind(this);

        // checks
        PermissionUtils.checkDefaultDialer(this);
        showNewVersionDialog();

        // View Pager
        mAdapterViewPager = new CustomPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (isSearchBarVisible()) toggleSearchBar();
                syncFABAndFragment();
                if (position == 1) showView(mAddContactButton, !isBottomSheetOpen(mBottomSheetBehavior.getState()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSmartTabLayout.setViewPager(mViewPager);

        // View Models

        // Intent View Model
        mSharedIntentViewModel = ViewModelProviders.of(this).get(SharedIntentViewModel.class);

        // Search Bar View Model
        mSharedSearchViewModel = ViewModelProviders.of(this).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getIsFocused().observe(this, f -> {
            if (f) expandAppBar(true);
        });

        // Dial View Model
        mSharedDialViewModel = ViewModelProviders.of(this).get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsOutOfFocus().observe(this, b -> {
            if (b) mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        mSharedDialViewModel.getNumber().observe(this, n -> {
            if (n == null || n.length() == 0) toggleAddContactAction(false);
            else toggleAddContactAction(true);
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

        mAddContactButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_black_24dp));

        // Check for intents from others apps
        checkIncomingIntent();

        showBiometricPrompt(this);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof SearchBarFragment)
            mSearchBarFragment = (SearchBarFragment) fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        syncFABAndFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact: {
                String number = mSharedDialViewModel.getNumber().getValue();
                ContactUtils.openAddContact(this, number);
                return true;
            }
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
        if (isBottomSheetOpen(mBottomSheetBehavior.getState())) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }

        super.onBackPressed();
        syncFABAndFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @OnClick(R.id.add_contact_fab_button)
    public void addContact() {
        String number = mSharedDialViewModel.getNumber().getValue();
        ContactUtils.openAddContact(this, number);
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
     * @param isExpand should expend dialer or not
     */
    public void expandDialer(boolean isExpand) {
        if (isExpand) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    /**
     * Extend/Collapse the appbar_main according to given parameter
     *
     * @param isExpand should expand the app bar or not
     */
    public void expandAppBar(boolean isExpand) {
        mAppBarLayout.setExpanded(isExpand);
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

    // -- Utilities -- //

    /**
     * If user using a new version of the app, show the new version dialog
     */
    private void showNewVersionDialog() {
        if (PreferenceUtils.getInstance().getInt(R.string.pref_last_version_key) < BuildConfig.VERSION_CODE) {
            PreferenceUtils.getInstance().putInt(R.string.pref_last_version_key, BuildConfig.VERSION_CODE);
            new ChangelogDialog().show(getSupportFragmentManager(), TAG_CHANGELOG_DIALOG);
        }
    }

    private boolean isBottomSheetOpen(int bottomSheetState) {
        return bottomSheetState != BottomSheetBehavior.STATE_HIDDEN && bottomSheetState != BottomSheetBehavior.STATE_COLLAPSED;
    }

    // -- Other -- //

    /**
     * Checking for incoming intents from other applications
     */
    private void checkIncomingIntent() {
        mIntent = getIntent();
        mIntentAction = mIntent.getAction();
        mIntentType = mIntent.getType();
        if (mIntentAction == Intent.ACTION_DIAL || mIntentAction == Intent.ACTION_VIEW)
            handleViewIntent(mIntent);
    }

    /**
     * Handle a VIEW intent (For example when you click a number in whatsapp)
     *
     * @param intent intent to handle
     */
    private void handleViewIntent(Intent intent) {
        String sharedText = "";

        // Try decoding incoming intent data
        try {
            sharedText = URLDecoder.decode(intent.getDataString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "An error occured when trying to get phone number :(", Toast.LENGTH_LONG).show();
            return;
        }

        if (sharedText.contains("tel:") && !sharedText.equals("tel:")) {
            mSharedIntentViewModel.setData(sharedText.replace("tel:", ""));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            Toast.makeText(this, "No phone number detected", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Toggle "add contact" button at the top of the screen
     *
     * @param isShow show or not to show the add contact button
     */
    private void toggleAddContactAction(boolean isShow) {
        if (mMenu != null) {
            MenuItem addContact = mMenu.findItem(R.id.action_add_contact);
            addContact.setVisible(isShow);
        }
    }

}
