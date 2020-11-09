package com.chooloo.www.callmanager.ui.page;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface PageMvpPresenter<V extends PageMvpView> extends MvpPresenter<V> {
    void onDialNumberChanged(String number);

    void onSearchTextChanged(String text);

    void onScrollStateChanged(int state);
}
