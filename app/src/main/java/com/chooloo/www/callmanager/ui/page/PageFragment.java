package com.chooloo.www.callmanager.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.chooloo.www.callmanager.databinding.FragmentPageBinding;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedSearchViewModel;

public abstract class PageFragment extends BaseFragment implements PageMvpView {

    private PageMvpPresenter<PageMvpView> mPresenter;

    protected FragmentPageBinding binding;

    protected SharedDialViewModel mDialViewModel;
    protected SharedSearchViewModel mSearchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onSetup() {
        mPresenter = new PagePresenter<>();
        mPresenter.onAttach(this);

        mDialViewModel = mViewModelProvider.get(SharedDialViewModel.class);
        mSearchViewModel = mViewModelProvider.get(SharedSearchViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDialViewModel.getNumber().observe(getViewLifecycleOwner(), this::onDialNumberChanged);
        mSearchViewModel.getText().observe(getViewLifecycleOwner(), this::onSearchTextChanged);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
