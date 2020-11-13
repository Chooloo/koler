package com.chooloo.www.callmanager.ui.page;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

import static com.chooloo.www.callmanager.ui.page.PageFragment.PAGE_STATE_ACTIVE;
import static com.chooloo.www.callmanager.ui.page.PageFragment.PAGE_STATE_IDLE;

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
        if (newState == (RecyclerView.SCROLL_STATE_DRAGGING | RecyclerView.SCROLL_STATE_SETTLING)) {
            mMvpView.setPageState(PAGE_STATE_ACTIVE);
        } else {
            mMvpView.setPageState(PAGE_STATE_IDLE);
        }
    }
}
