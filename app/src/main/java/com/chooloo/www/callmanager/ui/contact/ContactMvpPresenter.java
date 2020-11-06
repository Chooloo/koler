package com.chooloo.www.callmanager.ui.contact;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface ContactMvpPresenter<V extends ContactMvpView> extends MvpPresenter<V> {
    void onBackButtonPressed();

    void onActionCall();

    void onActionSms();

    void onActionEdit();

    void onActionInfo();

    void onActionDelete();

    void onActionFav();

    void onRecentsLoadFinished();
}
