package com.chooloo.www.callmanager.ui.search;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface SearchMvpView extends MvpView {

    void setFocus();

    String getText();

    void toggleSearchBar(boolean isShow);
}
