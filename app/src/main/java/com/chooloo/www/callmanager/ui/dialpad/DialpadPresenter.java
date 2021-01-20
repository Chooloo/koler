package com.chooloo.www.callmanager.ui.dialpad;

import android.view.KeyEvent;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class DialpadPresenter<V extends DialpadMvpView> extends BasePresenter<V> implements DialpadMvpPresenter<V> {

    @Override
    public void onResume() {
        mMvpView.toggleToneGenerator(true);
    }

    @Override
    public void onPause() {
        mMvpView.stopTone();
        mMvpView.toggleToneGenerator(false);
    }

    @Override
    public void onKeyClick(int keyCode) {
        mMvpView.vibrate();
        mMvpView.playTone(keyCode);
        mMvpView.toggleCursor(false);
        mMvpView.registerKeyEvent(keyCode);
    }

    @Override
    public void onCallClick() {
        mMvpView.call();
    }

    @Override
    public void onDigitsClick() {
        mMvpView.toggleCursor(true);
    }

    @Override
    public void onDeleteClick() {
        mMvpView.backspace();
    }

    @Override
    public void onAddContactClick() {
        mMvpView.addContact();
    }

    @Override
    public boolean onLongDeleteClick() {
        mMvpView.setNumber("");
        return true;
    }

    @Override
    public boolean onLongOneClick() {
        if (mMvpView.isDialer()) {
            mMvpView.callVoicemail();
        }
        return mMvpView.isDialer();
    }

    @Override
    public boolean onLongZeroClick() {
        onKeyClick(KeyEvent.KEYCODE_PLUS);
        return true;
    }

    @Override
    public void onIntentDataChanged(String data) {
        mMvpView.setNumber(data);
        mMvpView.updateViewModel(data);
    }

    @Override
    public void onTextChanged(String text) {
        boolean isShowOptions = text != null && text.length() > 0;
        mMvpView.showDeleteButton(isShowOptions);
        mMvpView.showAddContactButton(isShowOptions);
        mMvpView.updateViewModel(text);
    }
}
