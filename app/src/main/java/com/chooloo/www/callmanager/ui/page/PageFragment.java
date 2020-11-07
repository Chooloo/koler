package com.chooloo.www.callmanager.ui.page;

import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedSearchViewModel;

public abstract class PageFragment extends BaseFragment implements PageMvpView {

    protected static final int PAGE_STATE_IDLE = 0;
    protected static final int PAGE_STATE_ACTIVE = 1;

    protected SharedDialViewModel mDialViewModel;
    protected SharedSearchViewModel mSearchViewModel;

    @Override
    protected void setUp() {
        mDialViewModel = ViewModelProviders.of(mActivity).get(SharedDialViewModel.class);
        mDialViewModel.getNumber().observe(this, this::onDialNumberChanged);

        mSearchViewModel = ViewModelProviders.of(mActivity).get(SharedSearchViewModel.class);
        mSearchViewModel.getText().observe(this, this::onSearchTextChanged);
    }

    @Override
    public void setPageState(int pageState) {
        switch (pageState) {
            case PAGE_STATE_IDLE:
                mDialViewModel.setIsFocused(false);
                break;
            case PAGE_STATE_ACTIVE:
                mDialViewModel.setIsFocused(true);
                mSearchViewModel.setIsFocused(false);
                break;
            default:
                mDialViewModel.setIsFocused(false);
        }
    }

    protected abstract void onSearchTextChanged(String text);

    protected abstract void onDialNumberChanged(String number);
}
