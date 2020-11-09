package com.chooloo.www.callmanager.ui.main;

import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface MainMvpView extends MvpView {
    void setUp();

    Fragment getCurrentFragment();

    int getCurrentPosition();

    void checkIncomingIntent();

    void setBottomSheetState(int state);

    int getBottomSheetState();

    void setDialNumber(String number);

    String getDialNumber();

    void toggleAddContactAction(boolean isShow);

    void showSearchBar(boolean isShow);

    void toggleSearchBar();

    void openAddContact();

    void goToSettings();

    void goToAbout();
}
