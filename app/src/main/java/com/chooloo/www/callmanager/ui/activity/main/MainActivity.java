package com.chooloo.www.callmanager.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CustomPagerAdapter;
import com.chooloo.www.callmanager.ui.activity.base.BaseThemeActivity;
import com.chooloo.www.callmanager.ui2.fragment.SearchBarFragment;
import com.chooloo.www.callmanager.util.BiometricUtils;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedIntentViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


// TODO implement FAB Coordination

public class MainActivity extends BaseThemeActivity implements MainContract.View {

    private MainPresenter<MainContract.View> mPresenter;

    FABManager mFABManager;

    Menu mMenu;

    BottomSheetBehavior<View> mBottomSheetBehavior;

    // View Models
    SharedDialViewModel mSharedDialViewModel;
    SharedIntentViewModel mSharedIntentViewModel;

    // Fragments
    FragmentPagerAdapter mAdapterViewPager;
    DialpadFragment mDialpadFragment;
    SearchBarFragment mSearchBarFragment;

    // App Bar
    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.appbar) View mAppBar;

    // Views
    @BindView(R.id.search_bar_container) FrameLayout mSearchBarContainer;
    @BindView(R.id.dialer_fragment) View mDialerView;
    @BindView(R.id.root_view) CoordinatorLayout mMainLayout;

    // Buttons
    @BindView(R.id.right_button) FloatingActionButton mRightButton;
    @BindView(R.id.left_button) FloatingActionButton mLeftButton;
    @BindView(R.id.add_contact_fab_button) FloatingActionButton mAddContactFAB;

    // Other
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.view_pager_tab) SmartTabLayout mSmartTabLayout;

    // Built-In Overrides

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setThemeType(ThemeUtils.TYPE_NO_ACTION_BAR); // set theme
        setContentView(R.layout.activity_main); // set the layout

        ButterKnife.bind(this);

        // set presenter
        mPresenter = new MainPresenter<>();
        mPresenter.bind(this, getLifecycle());
        mPresenter.subscribe(this);

        setUp();

        BiometricUtils.showBiometricPrompt(this);

        checkIncomingIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.bind(this, getLifecycle());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unbind();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof SearchBarFragment) {
            mSearchBarFragment = (SearchBarFragment) fragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onOptionsItemSelected(item);
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
        if (mPresenter.onBackPressed()) return;
        super.onBackPressed();
    }

    // OnClickes

    @OnClick(R.id.right_button)
    public void fabRightClick() {
        mFABManager.onRightClick();
    }

    @OnClick(R.id.left_button)
    public void fabLeftClick() {
        mFABManager.onLeftClick();
    }

    @OnClick(R.id.add_contact_fab_button)
    public void addContact() {
        String number = mSharedDialViewModel.getNumber().getValue();
        ContactUtils.openAddContact(this, number);
    }

    // Interface Overrides

    @Override
    public void setUp() {
        PreferenceUtils.getInstance(this);
        Utilities.setUpLocale(this);
        Utilities.showNewVersionDialog(this); // check new version
        PermissionUtils.checkDefaultDialer(this); // ask default dialer

        // view pager
        mAdapterViewPager = new CustomPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO add on page selected to presenter
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(PreferenceUtils.getInstance().getInt(R.string.pref_default_page_key)); // set page the default
        mSmartTabLayout.setViewPager(mViewPager);

        // search bar fragment
        mSearchBarFragment = new SearchBarFragment();
        mSearchBarFragment.setOnFocusChangedListener(isFocused -> mPresenter.onSearchFocusChanged(isFocused));
        mSearchBarFragment.setOnTextChangedListener(text -> mPresenter.onSearchTextChanged(text));
        getSupportFragmentManager().beginTransaction().replace(R.id.search_bar_container, mSearchBarFragment).commit();

        // dialpad fragment
        mDialpadFragment = DialpadFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().add(R.id.dialer_fragment, mDialpadFragment).commit();

        // bottom sheet
        mBottomSheetBehavior = BottomSheetBehavior.from(mDialerView);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // TODO add on bottom sheet state changed
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // dial view model
        mSharedDialViewModel = ViewModelProviders.of(this).get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsFocused().observe(this, focused -> mPresenter.onDialFocusChanged(focused));
        mSharedDialViewModel.getNumber().observe(this, number -> mPresenter.onDialNumberChanged(number));

        // set add contact fab icon
        mAddContactFAB.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_black_24dp));
    }

    @Override
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + mViewPager.getCurrentItem());
    }

    @Override
    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void checkIncomingIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Objects.equals(action, Intent.ACTION_DIAL) || Objects.equals(action, Intent.ACTION_VIEW)) {
            mPresenter.handleViewIntent(intent);
        }
    }

    @Override
    public void setBottomSheetState(@BottomSheetBehavior.State int state) {
        mBottomSheetBehavior.setState(state);
    }

    @Override
    public int getBottomSheetState() {
        return mBottomSheetBehavior.getState();
    }

    @Override
    public void setDialNumber(String number) {
        mDialpadFragment.setNumber(number);
    }

    @Override
    public String getDialNumber() {
        return mSharedDialViewModel.getNumber().getValue();
    }

    @Override
    public void toggleAddContactAction(boolean isShow) {
        if (mMenu != null) {
            mMenu.findItem(R.id.action_add_contact).setVisible(isShow);
        }
    }

    @Override
    public void toggleAddContactFAB(boolean isShow) {
        showView(mAddContactFAB, isShow);
    }

    @Override
    public void showSearchBar(boolean isShow) {
        mSearchBarFragment.toggleSearchBar(isShow);
        mSearchBarContainer.setVisibility(isShow ? VISIBLE : GONE);
        mAppBarLayout.setExpanded(isShow);
    }

    public void toggleSearchBar() {
        showSearchBar(mSearchBarContainer.getVisibility() == VISIBLE ? false : true);
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

    public interface FABManager {
        void onRightClick();

        void onLeftClick();

        int[] getIconsResources();
    }
}
