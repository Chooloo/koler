package com.chooloo.www.callmanager.ui.main;

import android.content.Intent;
import android.view.MenuItem;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    void onDialpadFabClick();

    void onPageSelected(int position);

    void onBottomSheetStateChanged(int state);

    void onSearchTextChanged(String text);

    void onSearchFocusChanged(boolean isFocused);

    void onDialNumberChanged(String number);

    void onDialFocusChanged(boolean isFocused);

    void onOptionsItemSelected(MenuItem item);

    boolean onBackPressed();

    void onMenuClick();

    void handleViewIntent(Intent intent);
}
