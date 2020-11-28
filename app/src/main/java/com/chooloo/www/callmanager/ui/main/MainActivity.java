package com.chooloo.www.callmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.about.AboutActivity;
import com.chooloo.www.callmanager.ui.dialpad.DialpadFragment;
import com.chooloo.www.callmanager.ui.search.SearchFragment;
import com.chooloo.www.callmanager.ui.settings.SettingsActivity;
import com.chooloo.www.callmanager.util.BiometricUtils;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// TODO implement FAB Coordination

public class MainActivity extends BaseActivity implements MainMvpView {

    private MainPresenter<MainMvpView> mPresenter;

    Menu mMenu;

    BottomSheetBehavior<View> mBottomSheetBehavior;

    SharedDialViewModel mSharedDialViewModel;

    DialpadFragment mDialpadFragment;
    SearchFragment mSearchBarFragment;

    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.search_bar_container) FrameLayout mSearchBarContainer;
    @BindView(R.id.dialpad_fragment) View mDialerView;
    @BindView(R.id.root_view) CoordinatorLayout mMainLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.view_pager_tab) SmartTabLayout mSmartTabLayout;
    @BindView(R.id.dialpad_fab_button) FloatingActionButton mDialpadFab;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
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
    public void onBackPressed() {
        if (!mPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
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

    @OnClick(R.id.dialpad_fab_button)
    public void onDialpadFabClick(View view) {
        mPresenter.onDialpadFabClick();
    }

    @Override
    public void setUp() {
        ButterKnife.bind(this);

        mPresenter = new MainPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        Utilities.showNewVersionDialog(this); // check new version
        PermissionUtils.checkDefaultDialer(this); // ask default dialer

        // view pager
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSmartTabLayout.setViewPager(mViewPager);

        // search bar fragment
        mSearchBarFragment = new SearchFragment();
        mSearchBarFragment.setOnFocusChangedListener(isFocused -> mPresenter.onSearchFocusChanged(isFocused));
        mSearchBarFragment.setOnTextChangedListener(text -> mPresenter.onSearchTextChanged(text));
        getSupportFragmentManager().beginTransaction().replace(R.id.search_bar_container, mSearchBarFragment).commit();

        // dialpad fragment
        mDialpadFragment = DialpadFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().add(R.id.dialpad_fragment, mDialpadFragment).commit();

        // bottom sheet
        mBottomSheetBehavior = BottomSheetBehavior.from(mDialerView);
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                mPresenter.onBottomSheetStateChanged(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);

        // dial view model
        mSharedDialViewModel = ViewModelProviders.of(this).get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsFocused().observe(this, focused -> mPresenter.onDialFocusChanged(focused));
        mSharedDialViewModel.getNumber().observe(this, number -> mPresenter.onDialNumberChanged(number));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        BiometricUtils.showBiometricPrompt(this);

        checkIncomingIntent();
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
    public void setDialNumber(String number) {
        mDialpadFragment.setNumber(number);
    }

    @Override
    public String getDialNumber() {
        return mSharedDialViewModel.getNumber().getValue();
    }

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

    @Override
    public void openAddContact() {
        ContactUtils.openAddContact(this, getDialNumber());
    }

    @Override
    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void goToAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void showDialpadFab(boolean isShow) {
        showView(mDialpadFab, isShow);
    }
}
