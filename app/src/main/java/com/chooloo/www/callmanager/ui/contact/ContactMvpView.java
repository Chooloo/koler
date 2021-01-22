package com.chooloo.www.callmanager.ui.contact;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface ContactMvpView extends MvpView {
    void onSetup();

    void callContact();

    void smsContact();

    void editContact();

    void openContact();

    void deleteContact();

    void animateLayout();
}
