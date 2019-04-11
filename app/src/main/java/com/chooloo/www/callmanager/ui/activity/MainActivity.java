package com.chooloo.www.callmanager.ui.activity;

import android.annotation.SuppressLint;
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

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.DialFragment;
import com.chooloo.www.callmanager.ui.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.ui.fragment.SharedDialViewModel;
import com.chooloo.www.callmanager.ui.fragment.SharedSearchViewModel;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static com.chooloo.www.callmanager.util.Utilities.askForPermissions;
import static com.chooloo.www.callmanager.util.Utilities.checkPermissionGranted;

public class MainActivity extends AppBarActivity {

    // View Models
    SharedDialViewModel mSharedDialViewModel;
    SharedSearchViewModel mSharedSearchViewModel;

    // Layouts and Fragments
    @BindView(R.id.appbar) View mAppBar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.activity_main) CoordinatorLayout mMainLayout;
    @BindView(R.id.top_dialer) RelativeLayout mTopDialer;
    @BindView(R.id.main_dialer_fragment) View mDialerFragmentLayout;

    // Buttons
    @BindView(R.id.dialer_button) FloatingActionButton mDialerButton;
    @BindView(R.id.search_button) FloatingActionButton mSearchButton;

    // Fragments
    DialFragment mDialFragment;
    SearchBarFragment mSearchBarFragment;

    BottomSheetBehavior mBottomSheetBehaviour;

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
        if (!checkPermissionGranted(this, CALL_PHONE) || !checkPermissionGranted(this, SEND_SMS)) {
            askForPermissions(this, new String[]{CALL_PHONE, READ_CONTACTS, SEND_SMS});
        }

        //Select the second item in the tab layout
        mTabLayout.setScrollPosition(1, 0f, true);
        //Move to the desired tab on tap
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //TODO add recents fragment
                    case 1:
                        moveToFragment(R.id.contactsFragment);
                        break;
                    case 2:
                        moveToFragment(R.id.cgroupsFragment);
                        break;
                    default:
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // - View Models - //

        // Search Bar View Model
        mSharedSearchViewModel = ViewModelProviders.of(this).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getIsFocused().observe(this, f -> {
            if (f) {
                collapseAppBar(true);
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
                    animateButtons(true);
                } else {
                    animateButtons(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
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
            case R.id.settings:
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
        NavController controller = Navigation.findNavController(this, R.id.main_fragment);
        int currentFragment = controller.getCurrentDestination().getId();
        if (currentFragment == R.id.cgroupsFragment) {
            animateButtons(true);
        }
        super.onBackPressed();
    }

    // -- OnClicks -- //

    @OnClick(R.id.dialer_button)
    public void toggleDialer(View view) {
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.search_button)
    public void toggleSearch(View view) {
        boolean isOpened = toggleSearchBar();
        if (isOpened) mSearchButton.setBackgroundColor(getResources().getColor(R.color.red_phone));
        else mSearchButton.setBackgroundColor(getResources().getColor(R.color.white));
    }


    // -- Other -- //

    /**
     * Change the dialer status (collapse/expand)
     *
     * @param isExpanded
     */
    public void setDialerExpanded(boolean isExpanded) {
        if (isExpanded) {
            BottomSheetBehavior.from(mDialerFragmentLayout).setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            BottomSheetBehavior.from(mDialerFragmentLayout).setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void moveToFragment(@IdRes int fragmentId) {
        NavController controller = Navigation.findNavController(this, R.id.main_fragment);
        int currentFragmentId = controller.getCurrentDestination().getId();
        switch(currentFragmentId) {
            case R.id.contactsFragment: {
                animateButtons(false);
                if (fragmentId == R.id.cgroupsFragment) {
                    controller.navigate(R.id.action_contactsFragment_to_cGroupsFragment);
                }
                break;
            }
            case R.id.cgroupsFragment: {
                if (fragmentId == R.id.contactsFragment) {
                    controller.navigate(R.id.action_cGroupsFragment_to_contactsFragment);
                }
                break;
            }
        }
    }

    /**
     * Extend/Collapse the appbar according to given parameter
     *
     * @param collapse
     */
    public void collapseAppBar(boolean collapse) {
        mAppBarLayout.setExpanded(!collapse);
    }

    /**
     * Animate action buttons (toggle on/off)
     *
     * @param isShow animate to visible/invisible
     */
    @SuppressLint("RestrictedApi")
    public void animateButtons(boolean isShow) {
        if (isShow) {
            // Move them back and show them
            CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) mSearchButton.getLayoutParams();
            p1.setAnchorId(R.id.main_dialer_fragment);
            mSearchButton.setLayoutParams(p1);
            mSearchButton.setVisibility(View.VISIBLE);
            CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) mDialerButton.getLayoutParams();
            p2.setAnchorId(R.id.main_dialer_fragment);
            mDialerButton.setLayoutParams(p2);
            mDialerButton.setVisibility(View.VISIBLE);
            // Animate them
            mSearchButton.animate().scaleX(1).scaleY(1).setDuration(100).start();
            mDialerButton.animate().scaleX(1).scaleY(1).setDuration(100).start();
            // Enable them
            mSearchButton.setEnabled(true);
            mDialerButton.setEnabled(true);

        } else {
            // Animate them
            mSearchButton.animate().scaleX(0).scaleY(0).setDuration(100).start();
            mDialerButton.animate().scaleX(0).scaleY(0).setDuration(100).start();
            // Disable them
            mSearchButton.setEnabled(false);
            mDialerButton.setEnabled(false);
            // Move them and hide them
            CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) mSearchButton.getLayoutParams();
            p1.setAnchorId(View.NO_ID); //should let you set visibility
            mSearchButton.setLayoutParams(p1);
            mSearchButton.setVisibility(View.GONE);
            CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) mDialerButton.getLayoutParams();
            p2.setAnchorId(View.NO_ID); //should let you set visibility
            mDialerButton.setLayoutParams(p2);
            mDialerButton.setVisibility(View.GONE);
        }
    }
}
