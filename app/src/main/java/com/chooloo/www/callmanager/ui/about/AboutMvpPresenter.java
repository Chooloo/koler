package com.chooloo.www.callmanager.ui.about;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface AboutMvpPresenter<V extends AboutMvpView> extends MvpPresenter<V> {
    void onChangelogClick();

    void onOpenSourceClick();

    void onSendEmailClick();

    void onReportBugClick();

    void onRateAppClick();

    void onDonateClick();
}
