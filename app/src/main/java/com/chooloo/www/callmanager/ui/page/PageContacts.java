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
import com.chooloo.www.callmanager.ui.contacts.ContactsFragment;

public class PageContacts extends PageFragment implements PageMvpView {

    private PagePresenter<PageMvpView> mPresenter;

    private ContactsFragment mContactsFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_page_layout, mContactsFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    protected void setUp() {
        super.setUp();

        mPresenter = new PagePresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        mContactsFragment = new ContactsFragment();
        mContactsFragment.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrollChanged(newState);
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
    public void loadNumber(String number) {
        mContactsFragment.load(number, number.equals("") ? null : number);
    }

    @Override
    public void loadSearchText(String text) {
        mContactsFragment.load(null, text.equals("") ? null : text);
    }
}
