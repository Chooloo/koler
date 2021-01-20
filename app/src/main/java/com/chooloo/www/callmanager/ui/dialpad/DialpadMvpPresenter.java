package com.chooloo.www.callmanager.ui.dialpad;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface DialpadMvpPresenter<V extends DialpadMvpView> extends MvpPresenter<V> {
    void onResume();

    void onPause();

    void onKeyClick(int keyCode);

    void onCallClick();

    void onDigitsClick();

    void onDeleteClick();

    void onAddContactClick();

    boolean onLongDeleteClick();

    boolean onLongOneClick();

    boolean onLongZeroClick();

    void onIntentDataChanged(String data);

    void onTextChanged(String text);
}
