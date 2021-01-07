package com.chooloo.www.callmanager.ui.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.recents.RecentsFragment;

public class PageRecents extends PageFragment implements PageMvpView {

    private PageMvpPresenter<PageMvpView> mPresenter;

    private RecentsFragment mRecentsFragment;

    public static PageRecents newInstance() {
        Bundle args = new Bundle();
        PageRecents fragment = new PageRecents();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetup() {
        super.onSetup();

        mPresenter = new PagePresenter<>();
        mPresenter.onAttach(this);

        mRecentsFragment = RecentsFragment.newInstance();
        mRecentsFragment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrollStateChanged(newState);
            }
        });

        mActivity.getSupportFragmentManager().beginTransaction().replace(binding.fragmentPageLayout.getId(), mRecentsFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
    }

    @Override
    protected void onSearchTextChanged(String text) {
        mPresenter.onSearchTextChanged(text);
    }

    @Override
    protected void onDialNumberChanged(String number) {
        mPresenter.onDialNumberChanged(number);
    }

    @Override
    public void loadNumber(@Nullable String number) {
        mRecentsFragment.load(number, number == "" ? null : number);
    }

    @Override
    public void loadSearchText(@Nullable String text) {
        mRecentsFragment.load(null, text == "" ? null : text);
    }
}
