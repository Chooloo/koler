package com.chooloo.www.callmanager.ui.call;

import com.chooloo.www.callmanager.ui.base.MvpView;

interface CallMvpView extends MvpView {
    void answer();

    void reject();

    void toggleHold(boolean isHold);

    void toggleMute(boolean isMute);

    void toggleSpeaker(boolean isSpeaker);

    void showDialpad(boolean isShow);

    void pressDialpadKey(char keyChar);

    void addCall();

    void updateCallerInfo();

    void updateState(int state);

    void moveHangupButtonToMiddle();

    void switchToActiveCallUI();

    void acquireWakeLock();

    void releaseWakeLock();

    void createNotification();

    void createNotificationChannel();

    void cancelNotification();
}
