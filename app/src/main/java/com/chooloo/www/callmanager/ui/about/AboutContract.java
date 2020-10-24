package com.chooloo.www.callmanager.ui.about;

import com.chooloo.www.callmanager.ui.base.BaseContract;


public class AboutContract implements BaseContract {

    interface View extends BaseContract.View {

        void setUp();

        void openChangelog();

        void openSource();

        void sendEmail();

        void reportBug();

        void rateApp();

        void donate();
    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {

        void onChangelogClick();

        void onOpenSourceClick();

        void onSendEmailClick();

        void onReportBugClick();

        void onRateAppClick();

        void onDonateClick();
    }
}
