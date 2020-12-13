package com.chooloo.www.callmanager.ui.page;

import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedSearchViewModel;

public abstract class PageFragment extends BaseFragment implements PageMvpView {

    private PageMvpPresenter<PageMvpView> mPresenter;

    protected SharedDialViewModel mDialViewModel;
    protected SharedSearchViewModel mSearchViewModel;

    @Override
    public void setUp() {
        mPresenter = new PagePresenter<>();
        mPresenter.onAttach(this);

        mDialViewModel = mViewModelProvider.get(SharedDialViewModel.class);
        mDialViewModel.getNumber().observe(this, this::onDialNumberChanged);

        mSearchViewModel = mViewModelProvider.get(SharedSearchViewModel.class);
        mSearchViewModel.getText().observe(this, this::onSearchTextChanged);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void setDialpadFocused(boolean isFocused) {
        mDialViewModel.setIsFocused(isFocused);
    }

    @Override
    public void setSearchBarFocused(boolean isFocused) {
        mSearchViewModel.setIsFocused(isFocused);
    }

    protected abstract void onSearchTextChanged(String text);

    protected abstract void onDialNumberChanged(String number);
}
