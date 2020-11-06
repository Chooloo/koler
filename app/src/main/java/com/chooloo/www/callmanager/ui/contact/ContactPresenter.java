package com.chooloo.www.callmanager.ui.contact;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class ContactPresenter<V extends ContactMvpView> extends BasePresenter<V> implements ContactMvpPresenter<V> {

    @Override
    public void onBackButtonPressed() {
        mMvpView.onBackPressed();
    }

    @Override
    public void onActionCall() {
        mMvpView.actionCall();
    }

    @Override
    public void onActionSms() {
        mMvpView.actionSms();
    }

    @Override
    public void onActionEdit() {
        mMvpView.actionDelete();
    }

    @Override
    public void onActionInfo() {
        mMvpView.actionInfo();
    }

    @Override
    public void onActionDelete() {
        mMvpView.actionDelete();
    }

    @Override
    public void onActionFav() {
        mMvpView.actionFav();
    }
}
