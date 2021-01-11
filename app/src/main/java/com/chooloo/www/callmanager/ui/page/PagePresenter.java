package com.chooloo.www.callmanager.ui.page;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.BasePresenter;


public class PagePresenter<V extends PageMvpView> extends BasePresenter<V> implements PageMvpPresenter<V> {

    @Override
    public void onDialNumberChanged(String number) {
        mMvpView.loadNumber(number);
    }

    @Override
    public void onSearchTextChanged(String text) {
        mMvpView.loadSearchText(text);
    }

    @Override
    public void onScrollStateChanged(int newState) {
        boolean isScrolling = newState == (RecyclerView.SCROLL_STATE_DRAGGING | RecyclerView.SCROLL_STATE_SETTLING);
        if (isScrolling) {
            mMvpView.setDialpadFocused(true);
            mMvpView.setSearchBarFocused(false);
        } else {
            mMvpView.setDialpadFocused(false);
        }
    }
}