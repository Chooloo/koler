package com.chooloo.www.callmanager.ui.page;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface PageMvpView extends MvpView {

    void setPageState(int pageState);

    void loadNumber(String number);

    void loadSearchText(String text);
}
