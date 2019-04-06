package com.chooloo.www.callmanager.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CGroupAdapter;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.dialog.ImportSpreadsheetDialog;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.task.AsyncSpreadsheetImport;

import java.io.File;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class CGroupsFragment extends AbsRecyclerViewFragment implements ImportSpreadsheetDialog.OnImportListener {

    private CGroupsViewModel mViewModel;

    private CGroupAdapter mAdapter;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreateView() {
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new CGroupAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener((v, cgroup) -> {
            Bundle args = new Bundle();
            args.putLong(getString(R.string.arg_list_id), cgroup.getListId());
            Navigation.findNavController(v).navigate(R.id.action_cgroupsFragment_to_cGroupDetailsFragment, args);
        });
    }

    // -- Overrides -- //

    @Override
    protected int layoutId() {
        return R.layout.fragment_cgroups;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CGroupsViewModel.class);
        mViewModel.getContactsLists().observe(this, cgroups -> mAdapter.setData(cgroups));
    }

    @Override
    public void onImport(CGroup list, File excelFile, int nameColIndex, int numberColIndex) {
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

        AsyncSpreadsheetImport.OnFinishListener onFinishListener = callback -> progressDialog.dismiss();

        task.setOnProgressListener(onProgressListener);
        task.setOnFinishListener(onFinishListener);
        task.execute();
    }

    // -- OnClicks -- //

    @OnClick(R.id.add_contacts)
    public void addContacts(View view) {
        new ImportSpreadsheetDialog.Builder(getFragmentManager())
                .onImportListener(this)
                .show(new ImportSpreadsheetDialog());
    }

    @OnClick(R.id.back_button)
    public void switchBack(View view) {
        getActivity().onBackPressed();
    }
}
