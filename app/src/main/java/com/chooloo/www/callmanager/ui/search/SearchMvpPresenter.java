package com.chooloo.www.callmanager.ui.search;

import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface SearchMvpPresenter<V extends SearchMvpView> extends MvpPresenter<V> {
    void onTextChanged(String text);

    void onFocusChanged(boolean hasFocus);
}
