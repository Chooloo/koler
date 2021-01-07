package com.chooloo.www.callmanager.ui.about;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface AboutMvpView extends MvpView {
    void onSetup();

    void openChangelog();

    void openSource();

    void sendEmail();

    void reportBug();

    void rateApp();

    void donate();
}
