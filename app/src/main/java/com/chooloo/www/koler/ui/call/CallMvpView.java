package com.chooloo.www.koler.ui.call;

import com.chooloo.www.koler.ui.base.MvpView;

interface CallMvpView extends MvpView {
    void answer();

    void reject();

    void toggleHold(boolean isHold);

    void toggleMute(boolean isMute);

    void toggleSpeaker(boolean isSpeaker);

    void toggleDialpad(boolean isShow);

    void pressDialpadKey(char keyChar);

    void addCall();

    void updateCall();

    void updateState();

    void switchToActiveCallUI();

    void moveHangupButtonToMiddle();

    void createNotification();

    void createNotificationChannel();

    void cancelNotification();

    boolean isDialpadOpened();
}
