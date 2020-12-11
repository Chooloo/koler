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

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.about.AboutActivity;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment;
import com.chooloo.www.callmanager.ui.page.PageAdapterMain;
import com.chooloo.www.callmanager.ui.search.SearchFragment;
import com.chooloo.www.callmanager.ui.settings.SettingsActivity;
import com.chooloo.www.callmanager.ui.widgets.tablayout.TabLayout;
import com.chooloo.www.callmanager.util.BiometricUtils;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// TODO implement FAB Coordination

public class MainActivity extends BaseActivity implements MainMvpView {

    private MainMvpPresenter<MainMvpView> mPresenter;

    Menu mMenu;

    SharedDialViewModel mSharedDialViewModel;

    DialpadBottomDialogFragment mDialpadFragment;
    SearchFragment mSearchBarFragment;

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.view_pager_tab) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager2 mViewPager;
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
        mPresenter.onAttach(this);

        Utilities.showNewVersionDialog(this); // check new version
        PermissionUtils.checkDefaultDialer(this); // ask default dialer

        // view pager
        mViewPager.setAdapter(new PageAdapterMain(this));
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mPresenter.onPageSelected(position);
            }
        });
        mTabLayout.setViewPager(mViewPager);

        // search bar fragment
        mSearchBarFragment = new SearchFragment();
        mSearchBarFragment.setOnFocusChangedListener(isFocused -> mPresenter.onSearchFocusChanged(isFocused));
        mSearchBarFragment.setOnTextChangedListener(text -> mPresenter.onSearchTextChanged(text));
        getSupportFragmentManager().beginTransaction().replace(R.id.search_bar_container, mSearchBarFragment).commit();

        // dialpad fragment
        mDialpadFragment = DialpadBottomDialogFragment.newInstance(true);

        // dial view model
        mSharedDialViewModel = mViewModelProvider.get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsFocused().observe(this, focused -> mPresenter.onDialFocusChanged(focused));
        mSharedDialViewModel.getNumber().observe(this, number -> mPresenter.onDialNumberChanged(number));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
    public void showDialpad(boolean isShow) {
        if (isShow) {
            mDialpadFragment.show(getSupportFragmentManager(), DialpadBottomDialogFragment.TAG);
        } else if (mDialpadFragment.isShown()) {
            mDialpadFragment.dismiss();
        }
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
        isShow = isShow && v.isEnabled();
        v.animate().scaleX(isShow ? 1 : 0).scaleY(isShow ? 1 : 0).setDuration(100).start();
        v.setClickable(isShow);
        v.setFocusable(isShow);
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
