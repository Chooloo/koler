package com.chooloo.www.callmanager.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    private int mCurrentPosition;
    @BottomSheetBehavior.State private int mBottomSheetState;

    public MainPresenter() {
        mCurrentPosition = 0;
        mBottomSheetState = STATE_COLLAPSED;
    }

    @Override
    public void onPageSelected() {
        mCurrentPosition = mMvpView.getCurrentPosition();
        mMvpView.toggleAddContactAction(mCurrentPosition == 1 && (mBottomSheetState == STATE_HIDDEN || mBottomSheetState == STATE_COLLAPSED));
        mMvpView.showSearchBar(false);
    }

    @Override
    public void onBottomSheetStateChanged(@BottomSheetBehavior.State int state) {
        mBottomSheetState = state;
    }

    @Override
    public void onDialFocusChanged(boolean isFocused) {
        mMvpView.setBottomSheetState(isFocused ? STATE_EXPANDED : STATE_COLLAPSED);
    }

    @Override
    public void onDialNumberChanged(String number) {
        mMvpView.toggleAddContactAction(number != null && number.length() > 0);
    }

    @Override
    public void onSearchTextChanged(String text) {
        // TODO
    }

    @Override
    public void onSearchFocusChanged(boolean isFocused) {
        mMvpView.showSearchBar(isFocused);
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact: {
                mMvpView.openAddContact();
                break;
            }
            case R.id.action_settings: {
                mMvpView.goToSettings();
                break;
            }
            case R.id.action_about: {
                mMvpView.goToAbout();
                break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mBottomSheetState != STATE_HIDDEN && mBottomSheetState != STATE_COLLAPSED) {
            mMvpView.setBottomSheetState(STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    @Override
    public void handleViewIntent(Intent intent) {
        String intentText = "";

        // try decoding incoming intent data
        try {
            intentText = URLDecoder.decode(intent.getDataString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            mMvpView.showError("An error occured when trying to get phone number :(");
            return;
        }

        // check for a number
        if (intentText.contains("tel:") && !intentText.equals("tel:")) {
            mMvpView.setBottomSheetState(STATE_EXPANDED);
            mMvpView.setDialNumber(intentText);
        } else {
            mMvpView.showError("No phone number detected");
        }
    }
}
