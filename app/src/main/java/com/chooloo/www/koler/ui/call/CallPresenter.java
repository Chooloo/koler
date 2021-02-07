package com.chooloo.www.koler.ui.call;

import android.view.KeyEvent;

import com.chooloo.www.koler.ui.base.BasePresenter;

public class CallPresenter<V extends CallMvpView> extends BasePresenter<V> implements CallMvpPresenter<V> {
    @Override
    public boolean onBackPressed() {
        if (mvpView.isDialpadOpened()) {
            mvpView.toggleDialpad(false);
            return true;
        }
        return false;
    }

    @Override
    public void onAnswerClick() {
        mvpView.switchToActiveCallUI();
        mvpView.answer();
    }

    @Override
    public void onRejectClick() {
        mvpView.reject();
    }

    @Override
    public void onAddCallClick() {
        // TODO this should get a call as a parameter or something idk do it
        mvpView.addCall();
    }

    @Override
    public void onKeypadClick() {
        mvpView.toggleDialpad(!mvpView.isDialpadOpened());
    }

    @Override
    public void onMuteClick(boolean isActivated) {
        mvpView.toggleHold(isActivated);
    }

    @Override
    public void onSpeakerClick(boolean isActivated) {
        mvpView.toggleSpeaker(isActivated);
    }

    @Override
    public void onHoldClick(boolean isActivated) {
        mvpView.toggleHold(isActivated);
    }

    @Override
    public void onDialpadKeyClick(int keyCode, KeyEvent event) {
        mvpView.pressDialpadKey((char) event.getUnicodeChar());
    }

    @Override
    public void onDetailsChanged() {
        mvpView.updateCall();
    }

    @Override
    public void onStateChanged() {
        mvpView.updateState();
    }
}