package com.chooloo.www.callmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.ContactsList;
import com.chooloo.www.callmanager.dialog.ImportSpreadsheetDialog;
import com.chooloo.www.callmanager.util.AsyncSpreadsheetImport;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomContactsFragment extends Fragment implements ImportSpreadsheetDialog.OnImportListener {
    private ViewGroup mRootView;
    private CustomContactsViewModel mViewModel;

    public static CustomContactsFragment newInstance() {
        return new CustomContactsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_custom_contacts, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CustomContactsViewModel.class);
    }

    @OnClick(R.id.add_contacts)
    public void addContacts(View view) {
        new ImportSpreadsheetDialog.Builder(getFragmentManager())
                .onImportListener(this)
                .show(new ImportSpreadsheetDialog());
    }

    @Override
    public void onImport(ContactsList list, File excelFile, int nameColIndex, int numberColIndex) {
        MaterialDialog progressDialog = new MaterialDialog.Builder(getContext())
                .progress(false, 0, true)
                .progressNumberFormat("%1d/%2d")
                .show();

        AsyncSpreadsheetImport task = new AsyncSpreadsheetImport(getContext(),
                excelFile,
                nameColIndex,
                numberColIndex,
                list);

        AsyncSpreadsheetImport.OnProgressListener onProgressListener = (rowsRead, rowsCount) -> {
            progressDialog.setProgress(rowsRead);
            progressDialog.setMaxProgress(rowsCount);
        };

        AsyncSpreadsheetImport.OnFinishListener onFinishListener = progressDialog::dismiss;

        task.setOnProgressListener(onProgressListener);
        task.setOnFinishListener(onFinishListener);
        task.execute();
    }
}
