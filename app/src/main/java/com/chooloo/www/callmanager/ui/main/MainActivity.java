package com.chooloo.www.callmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.ActivityMainBinding;
import com.chooloo.www.callmanager.ui.about.AboutActivity;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialpad.BottomDialpadFragment;
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment;
import com.chooloo.www.callmanager.ui.menu.MenuFragment;
import com.chooloo.www.callmanager.ui.search.SearchFragment;
import com.chooloo.www.callmanager.ui.settings.SettingsActivity;
import com.chooloo.www.callmanager.util.BiometricUtils;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;

import java.util.Objects;


// TODO implement FAB Coordination

public class MainActivity extends BaseActivity implements MainMvpView {

    private MainMvpPresenter<MainMvpView> mPresenter;

    private SharedDialViewModel mSharedDialViewModel;

    private DialpadBottomDialogFragment mDialpadFragment;
    private MenuFragment mMenuFragment;
    private SearchFragment mSearchFragment;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

    @Override
    public void onSetup() {
        mPresenter = new MainPresenter<>();
        mPresenter.onAttach(this);

        Utilities.showNewVersionDialog(this);
        PermissionUtils.checkDefaultDialer(this);
        BiometricUtils.showBiometricPrompt(this);

        binding.dialpadFabButton.setOnClickListener(view -> mPresenter.onDialpadFabClick());
        binding.appbarMain.mainMenuButton.setOnClickListener(view -> mPresenter.onMenuClick());
        binding.viewPager.setAdapter(new MainPagerAdapter(this));
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mPresenter.onPageSelected(position);
            }
        });
        binding.appbarMain.mainTabLayout.setViewPager(binding.viewPager);

        mSearchFragment = new SearchFragment();
        mSearchFragment.setOnFocusChangedListener(isFocused -> mPresenter.onSearchFocusChanged(isFocused));
        mSearchFragment.setOnTextChangedListener(text -> mPresenter.onSearchTextChanged(text));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_search_fragment, mSearchFragment).commit();

        mMenuFragment = MenuFragment.newInstance(R.menu.main_actions);
        mMenuFragment.setOnMenuItemClickListener(menuItem -> mPresenter.onOptionsItemSelected(menuItem));

        mDialpadFragment = DialpadBottomDialogFragment.newInstance(true);

        mSharedDialViewModel = mViewModelProvider.get(SharedDialViewModel.class);
        mSharedDialViewModel.getIsFocused().observe(this, focused -> mPresenter.onDialFocusChanged(focused));
        mSharedDialViewModel.getNumber().observe(this, number -> mPresenter.onDialNumberChanged(number));

        checkIncomingIntent();
    }

    @Override
    public int getCurrentPosition() {
        return binding.viewPager.getCurrentItem();
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
        } else if (mDialpadFragment.ismIsShown()) {
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
    public void showMenu(boolean isShow) {
        if (isShow && !mMenuFragment.ismIsShown()) {
            mMenuFragment.show(getSupportFragmentManager(), "main_menu_fragment");
        } else if (!isShow && mMenuFragment.ismIsShown()) {
            mMenuFragment.dismiss();
        }
    }

    @Override
    public void showDialpadFab(boolean isShow) {
        showView(binding.dialpadFabButton, isShow);
    }

    private static void showView(View v, boolean isShow) {
        isShow = isShow && v.isEnabled();
        v.animate().scaleX(isShow ? 1 : 0).scaleY(isShow ? 1 : 0).setDuration(100).start();
        v.setClickable(isShow);
        v.setFocusable(isShow);
    }

}
