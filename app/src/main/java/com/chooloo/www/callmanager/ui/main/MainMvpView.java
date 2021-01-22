package com.chooloo.www.callmanager.ui.main;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface MainMvpView extends MvpView {
    void onSetup();

    int getCurrentPosition();

    void checkIncomingIntent();

    void showDialpad(boolean isShow);

    void setDialNumber(String number);

    String getDialNumber();

    void goToSettings();

    void goToAbout();

    void showMenu(boolean isShow);

    void handleViewIntent(Intent intent);
}
