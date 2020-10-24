package com.chooloo.www.callmanager.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.ui2.activity.SettingsActivity;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {

    private int mCurrentPosition;
    @BottomSheetBehavior.State private int mBottomSheetState;

    public MainPresenter() {
        mCurrentPosition = 0;
        mBottomSheetState = STATE_COLLAPSED;
    }

    @Override
    public void onPageSelected() {
        mCurrentPosition = mView.getCurrentPosition();
        mView.toggleAddContactAction(mCurrentPosition == 1 && (mBottomSheetState == STATE_HIDDEN || mBottomSheetState == STATE_COLLAPSED));
        mView.showSearchBar(false);
    }

    @Override
    public void onBottomSheetStateChanged(@BottomSheetBehavior.State int state) {
        mBottomSheetState = state;
    }

    @Override
    public void onDialFocusChanged(boolean isFocused) {
        mView.setBottomSheetState(isFocused ? STATE_EXPANDED : STATE_COLLAPSED);
    }

    @Override
    public void onDialNumberChanged(String number) {
        mView.toggleAddContactAction(number != null && number.length() > 0);
    }

    @Override
    public void onSearchTextChanged(String text) {
        // TODO
    }

    @Override
    public void onSearchFocusChanged(boolean isFocused) {
        mView.showSearchBar(isFocused);
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact: {
                ContactUtils.openAddContact((Activity) mContext, mView.getDialNumber());
                break;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                mContext.startActivity(intent);
                break;
            }
            case R.id.action_about: {
                Intent intent = new Intent(mContext, AboutActivity.class);
                mContext.startActivity(intent);
                break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mBottomSheetState != STATE_HIDDEN && mBottomSheetState != STATE_COLLAPSED) {
            mView.setBottomSheetState(STATE_COLLAPSED);
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
            Toast.makeText(mContext, "An error occured when trying to get phone number :(", Toast.LENGTH_LONG).show();
            return;
        }

        // check for a number
        if (intentText.contains("tel:") && !intentText.equals("tel:")) {
            mView.setBottomSheetState(STATE_EXPANDED);
            mView.setDialNumber(intentText);
        } else {
            Toast.makeText(mContext, "No phone number detected", Toast.LENGTH_SHORT).show();
        }
    }
}
