package com.chooloo.www.callmanager.ui.dialpad;

import android.view.KeyEvent;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class DialpadPresenter<V extends DialpadContract.View> extends BasePresenter<V> implements DialpadContract.Presenter<V> {

    @Override
    public void onResume() {
        mView.toggleToneGenerator(true);
    }

    @Override
    public void onPause() {
        mView.stopTone();
        mView.toggleToneGenerator(false);
    }

    @Override
    public void onKeyClick(int keyCode) {
        mView.vibrate();
        mView.playTone(keyCode);
        mView.toggleCursor(false);
        mView.registerKeyEvent(keyCode);
    }

    @Override
    public void onCallClick() {
        mView.call();
    }

    @Override
    public void onDigitsClick() {
        mView.toggleCursor(true);
    }

    @Override
    public void onLongDeleteClick() {
        mView.setNumber("");
    }

    @Override
    public void onLongOneClick() {
        if (mView.isDialer()) {
            mView.callVoicemail();
        }
    }

    @Override
    public void onLongZeroClick() {
        onKeyClick(KeyEvent.KEYCODE_PLUS);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) mView.requestFocus();
    }

    @Override
    public void onIntentDataChanged(String data) {
        mView.setNumber(data);
        mView.setViewModelNumber(data);
    }

    @Override
    public void onTextChanged(String text) {
        mView.setViewModelNumber(text);
    }
}
