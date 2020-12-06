package com.chooloo.www.callmanager.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.recents.RecentsFragment;

import butterknife.ButterKnife;

public class PageRecents extends PageFragment implements PageMvpView {

    private PageMvpPresenter<PageMvpView> mPresenter;

    private RecentsFragment mRecentsFragment;

    public static PageRecents newInstance() {
        Bundle args = new Bundle();
        PageRecents fragment = new PageRecents();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecentsFragment = RecentsFragment.newInstance();
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_page_layout, mRecentsFragment).commit();
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
        mPresenter.onAttach(this, getLifecycle());

        mRecentsFragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrollStateChanged(newState);
            }
        });
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
