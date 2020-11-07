package com.chooloo.www.callmanager.ui.dialpad;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface DialpadMvpView extends MvpView {
    boolean isDialer();

    void setNumber(String number);

    void setViewModelNumber(String number);

    void call();

    void callVoicemail();

    void requestFocus();

    void toggleToneGenerator(boolean toggle);

    void stopTone();

    void playTone(int keyCode);

    void toggleCursor(boolean isShow);

    void registerKeyEvent(int keyCode);

    void vibrate();

    void setDigitsCanBeEdited(boolean isCanBeEdited);

    void showVoicemailButton(boolean isShow);
}
