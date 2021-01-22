package com.chooloo.www.callmanager.ui.contact;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class ContactPresenter<V extends ContactMvpView> extends BasePresenter<V> implements ContactMvpPresenter<V> {
    @Override
    public void onActionCall() {
        mMvpView.callContact();
    }

    @Override
    public void onActionSms() {
        mMvpView.smsContact();
    }

    @Override
    public void onActionEdit() {
        mMvpView.editContact();
    }

    @Override
    public void onActionInfo() {
        mMvpView.openContact();
    }

    @Override
    public void onActionDelete() {
        mMvpView.deleteContact();
    }
}
