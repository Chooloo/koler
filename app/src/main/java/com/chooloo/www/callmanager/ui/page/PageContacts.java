package com.chooloo.www.callmanager.ui.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.contacts.ContactsFragment;

public class PageContacts extends PageFragment implements PageMvpView {

    private PageMvpPresenter<PageMvpView> mPresenter;

    private ContactsFragment mContactsFragment;

    public static PageContacts newInstance() {
        Bundle args = new Bundle();
        PageContacts fragment = new PageContacts();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetup() {
        super.onSetup();

        mPresenter = new PagePresenter<>();
        mPresenter.attach(this);

        mContactsFragment = ContactsFragment.newInstance();
        mContactsFragment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrollStateChanged(newState);
            }
        });

        activity.getSupportFragmentManager().beginTransaction().add(binding.fragmentPageLayout.getId(), mContactsFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detach();
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
        mContactsFragment.load(number, number == "" ? null : number);
    }

    @Override
    public void loadSearchText(@Nullable String text) {
        mContactsFragment.load(null, text == "" ? null : text);
    }
}
