package com.chooloo.www.callmanager.ui.contact;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface ContactMvpView extends MvpView {
    void onSetup();

    void actionCall();

    void actionSms();

    void actionEdit();

    void actionInfo();

    void actionDelete();

    void actionFav();

    void toggleFavIcon(boolean toggle);

    void setContactFromIntent();

    void handleNoRecents();

    void onBackPressed();
}
