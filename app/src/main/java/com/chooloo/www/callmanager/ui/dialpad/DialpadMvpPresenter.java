package com.chooloo.www.callmanager.ui.dialpad;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface DialpadMvpPresenter<V extends DialpadMvpView> extends MvpPresenter<V> {
    void onResume();

    void onPause();

    void onKeyClick(int keyCode);

    void onCallClick();

    void onDigitsClick();

    void onLongDeleteClick();

    void onLongOneClick();

    void onLongZeroClick();

    void onHiddenChanged(boolean hidden);

    void onIntentDataChanged(String data);

    void onTextChanged(String text);
}
