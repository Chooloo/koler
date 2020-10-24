package com.chooloo.www.callmanager.ui.activity.contact;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class ContactPresenter<V extends ContactContract.View> extends BasePresenter<V> implements ContactContract.Presenter<V> {

    @Override
    public void onBackButtonPressed() {
        mView.onBackPressed();
    }

    @Override
    public void onActionCall() {
        mView.actionCall();
    }

    @Override
    public void onActionSms() {
        mView.actionSms();
    }

    @Override
    public void onActionEdit() {
        mView.actionDelete();
    }

    @Override
    public void onActionInfo() {
        mView.actionInfo();
    }

    @Override
    public void onActionDelete() {
        mView.actionDelete();
    }

    @Override
    public void onActionFav() {
        mView.actionFav();
    }
}
