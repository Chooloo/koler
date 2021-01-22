package com.chooloo.www.callmanager.ui.page;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface PageMvpView extends MvpView {

    void loadNumber(String number);

    void loadSearchText(String text);

    void setSearchBarFocused(boolean isFocused);
}
