package com.chooloo.www.callmanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CGroupsAdapter;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.task.AsyncSpreadsheetImport;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.CGroupActivity;
import com.chooloo.www.callmanager.ui.dialog.ImportSpreadsheetDialog;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class CGroupsFragment extends AbsRecyclerViewFragment implements
        ImportSpreadsheetDialog.OnImportListener,
        FABCoordinator.OnFabClickListener {

    private CGroupsViewModel mViewModel;

    private CGroupsAdapter mAdapter;

    public static CGroupsFragment newInstance(int page, String title) {
        CGroupsFragment cGroupsFragment = new CGroupsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        cGroupsFragment.setArguments(args);
        return cGroupsFragment;
    }

    @Override
    protected void onCreateView() {
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new CGroupsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener((v, cgroup) -> {
            Intent intent = new Intent(getContext(), CGroupActivity.class);
            intent.putExtra(CGroupActivity.EXTRA_LIST_ID, cgroup.getListId());
            startActivity(intent);
        });
    }

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

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_add_black_24dp,
                -1 //This means no FAB at all
        };
    }

    // -- OnClicks -- //

    @Override
    public void onRightClick() {
        new ImportSpreadsheetDialog.Builder(getFragmentManager())
                .onImportListener(this)
                .show(new ImportSpreadsheetDialog());
    }

    @Override
    public void onLeftClick() {

    }
}
