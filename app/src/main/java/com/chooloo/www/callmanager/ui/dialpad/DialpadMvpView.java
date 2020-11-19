package com.chooloo.www.callmanager.ui.dialpad;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface DialpadMvpView extends MvpView {
    boolean isDialer();

    void setNumber(String number);

    void setIsDialer(boolean isCanBeEdited);

    void updateViewModel(String number);

    void registerKeyEvent(int keyCode);

    void call();

    void callVoicemail();

    void toggleToneGenerator(boolean toggle);

    void stopTone();

    void playTone(int keyCode);

    void toggleCursor(boolean isShow);

    void vibrate();
}
