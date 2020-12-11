package com.chooloo.www.callmanager.ui.page;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface PageMvpView extends MvpView {

    void loadNumber(String number);

    void loadSearchText(String text);

    void setDialpadFocused(boolean isFocused);

    void setSearchBarFocused(boolean isFocused);
}
