package com.chooloo.www.callmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.dialog.ImportSpreadsheetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsListFragment extends Fragment {

    private ViewGroup mRootView;
    private ContactsListViewModel mViewModel;

    public static ContactsListFragment newInstance() {
        return new ContactsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_calling_lists, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ContactsListViewModel.class);
    }

    @OnClick(R.id.add_contacts)
    public void addContacts(View view) {
        ImportSpreadsheetDialog.showDialog(getContext());
    }
}
