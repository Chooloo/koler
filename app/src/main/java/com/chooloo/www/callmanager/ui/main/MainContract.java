package com.chooloo.www.callmanager.ui.main;

import android.content.Intent;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public class MainContract implements BaseContract {
    interface View extends BaseContract.View {
        void setUp();

        Fragment getCurrentFragment();

        int getCurrentPosition();

        void checkIncomingIntent();

        void setBottomSheetState(int state);

        int getBottomSheetState();

        void setDialNumber(String number);

        String getDialNumber();

        void toggleAddContactAction(boolean isShow);

        void toggleAddContactFAB(boolean isShow);

        void showSearchBar(boolean isShow);
    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
        void onPageSelected();

        void onBottomSheetStateChanged(int state);

        void onSearchTextChanged(String text);

        void onSearchFocusChanged(boolean isFocused);

        void onDialNumberChanged(String number);

        void onDialFocusChanged(boolean isFocused);

        void onOptionsItemSelected(MenuItem item);

        boolean onBackPressed();

        void handleViewIntent(Intent intent);
    }
}
