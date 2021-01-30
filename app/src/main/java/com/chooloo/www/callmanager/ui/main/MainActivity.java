package com.chooloo.www.callmanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.MainPagerAdapter;
import com.chooloo.www.callmanager.databinding.ActivityMainBinding;
import com.chooloo.www.callmanager.ui.about.AboutActivity;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment;
import com.chooloo.www.callmanager.ui.dialpad.DialpadFragment;
import com.chooloo.www.callmanager.ui.menu.MenuFragment;
import com.chooloo.www.callmanager.ui.search.SearchFragment;
import com.chooloo.www.callmanager.ui.settings.SettingsActivity;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;

import static android.content.Intent.ACTION_DIAL;
import static android.content.Intent.ACTION_VIEW;


// TODO implement FAB Coordination

public class MainActivity extends BaseActivity implements MainMvpView {

    private MainMvpPresenter<MainMvpView> mPresenter;

    private SharedDialViewModel mSharedDialViewModel;

    private DialpadBottomDialogFragment mBottomDialpadFragment;
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
        mPresenter.detach();
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
        mPresenter.attach(this);

        Utilities.showNewVersionDialog(this);
        PermissionUtils.ensureDefaultDialer(this);

        binding.dialpadFabButton.setOnClickListener(view -> mPresenter.onDialpadFabClick());
        binding.appbarMain.mainMenuButton.setOnClickListener(view -> mPresenter.onMenuClick());
        binding.viewPager.setAdapter(new MainPagerAdapter(this));
        binding.appbarMain.mainTabLayout.setViewPager(binding.viewPager);

        mBottomDialpadFragment = DialpadBottomDialogFragment.newInstance(true);

        mMenuFragment = MenuFragment.newInstance(R.menu.main_actions);
        mMenuFragment.setOnMenuItemClickListener(menuItem -> mPresenter.onOptionsItemSelected(menuItem));

        mSearchFragment = new SearchFragment();
        mSearchFragment.setOnFocusChangedListener(isFocused -> mPresenter.onSearchFocusChanged(isFocused));
        mSearchFragment.setOnTextChangedListener(text -> mPresenter.onSearchTextChanged(text));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_search_fragment, mSearchFragment).commit();

        mSharedDialViewModel = viewModelProvider.get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, number -> mPresenter.onDialNumberChanged(number));

        Intent intent = getIntent();
        if (intent.getAction().equals(ACTION_DIAL) || intent.getAction().equals(ACTION_VIEW)) {
            mPresenter.onViewIntent(intent);
        }
    }

    @Override
    public void showDialpad(boolean isShow) {
        if (isShow) {
            mBottomDialpadFragment.dismiss();
            mBottomDialpadFragment.show(getSupportFragmentManager(), DialpadFragment.TAG);
        } else if (mBottomDialpadFragment.isShown()) {
            mBottomDialpadFragment.dismiss();
        }
    }

    @Override
    public void setDialNumber(String number) {
        mBottomDialpadFragment.setNumber(number);
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
        if (isShow && !mMenuFragment.isShown()) {
            mMenuFragment.show(getSupportFragmentManager(), "main_menu_fragment");
        } else if (!isShow && mMenuFragment.isShown()) {
            mMenuFragment.dismiss();
        }
    }
}
