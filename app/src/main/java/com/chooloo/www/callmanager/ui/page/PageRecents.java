package com.chooloo.www.callmanager.ui.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void setUp() {
        super.setUp();

        mPresenter = new PagePresenter<>();
        mPresenter.onAttach(this);

        mRecentsFragment = RecentsFragment.newInstance();
        mRecentsFragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrollStateChanged(newState);
            }
        });

        putRecentsFragment();
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

    private void putRecentsFragment(){
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.fragmentPageLayout.getId(), mRecentsFragment).commit();
    }
}
