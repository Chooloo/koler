package com.chooloo.www.callmanager.ui.contact;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class ContactPresenter<V extends ContactMvpView> extends BasePresenter<V> implements ContactMvpPresenter<V> {
    @Override
    public void onActionCall() {
        mvpView.callContact();
    }

    @Override
    public void onActionSms() {
        mvpView.smsContact();
    }

    @Override
    public void onActionEdit() {
        mvpView.editContact();
    }

    @Override
    public void onActionInfo() {
        mvpView.openContact();
    }

    @Override
    public void onActionDelete() {
        mvpView.deleteContact();
    }
}
