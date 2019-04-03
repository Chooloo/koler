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
import android.view.WindowManager;
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

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
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


        // Watch the search bar state
        mSharedSearchViewModel = ViewModelProviders.of(this).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getIsFocused().observe(this, f -> {
            if (f) {
                collapseAppBar(true);
            } else {
                if (mSearchBarFragment != null)
                    toggleSearchBar();
            }
        });

        // Watch the dial state
        mSharedDialViewModel = ViewModelProviders.of(this).get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsOutOfFocus().observe(this, b -> {
            if (b) {
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // Set the bottom sheet behaviour
        mBottomSheetBehaviour = BottomSheetBehavior.from(mDialerFragmentLayout);
        // First hide the bottom sheet completely
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);


        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    mSearchButton.animate().scaleX(1).scaleY(1).setDuration(100).start();
                    mDialerButton.animate().scaleX(1).scaleY(1).setDuration(100).start();
                } else {
                    mSearchButton.animate().scaleX(0).scaleY(0).setDuration(100).start();
                    mDialerButton.animate().scaleX(0).scaleY(0).setDuration(100).start();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

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
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.excel:
                switchFragments();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -- OnClicks -- //

    @OnClick(R.id.dialer_button)
    public void toggleDialer(View view) {
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.search_button)
    public void toggleSearch(View view) {
        toggleSearchBar();
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

    /**
     * Switch between contacts and excel fragments
     */
    public void switchFragments() {
        NavController controller = Navigation.findNavController(this, R.id.main_fragment);
        int id = controller.getCurrentDestination().getId();
        if (id == R.id.contactsFragment) {
            controller.navigate(R.id.action_contactsFragment_to_cGroupsFragment);
        } else {
            controller.navigate(R.id.action_cGroupsFragment_to_contactsFragment);
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
}
