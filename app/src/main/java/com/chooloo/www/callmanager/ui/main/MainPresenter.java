package com.chooloo.www.callmanager.ui.main;

import android.content.Intent;
import android.view.MenuItem;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    @BottomSheetBehavior.State private int mBottomSheetState;

    public MainPresenter() {
        mBottomSheetState = STATE_COLLAPSED;
    }

    @Override
    public void onDialpadFabClick() {
        mvpView.showDialpad(true);
    }

    @Override
    public void onDialNumberChanged(String number) {
    }

    @Override
    public void onSearchTextChanged(String text) {
        // TODO
    }

    @Override
    public void onSearchFocusChanged(boolean isFocused) {
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                mvpView.goToSettings();
                break;
            }
            case R.id.action_about: {
                mvpView.goToAbout();
                break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mBottomSheetState != STATE_HIDDEN && mBottomSheetState != STATE_COLLAPSED) {
            mvpView.showDialpad(false);
            return true;
        }
        return false;
    }

    @Override
    public void onMenuClick() {
        mvpView.showMenu(true);
    }

    @Override
    public void onViewIntent(Intent intent) {
        String intentText = "";

        try {
            intentText = URLDecoder.decode(intent.getDataString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            mvpView.showError("An error occured when trying to get phone number :(");
            return;
        }

        if (intentText.contains("tel:")) {
            mvpView.showDialpad(true);
            mvpView.setDialNumber(intentText);
        } else {
            mvpView.showError("No phone number detected");
        }
    }
}
