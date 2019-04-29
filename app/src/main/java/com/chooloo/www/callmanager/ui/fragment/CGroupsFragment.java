package com.chooloo.www.callmanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CGroupsAdapter;
import com.chooloo.www.callmanager.adapter.listener.OnItemClickListener;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.database.entity.CGroupAndItsContacts;
import com.chooloo.www.callmanager.task.AsyncSpreadsheetImport;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.CGroupActivity;
import com.chooloo.www.callmanager.ui.dialog.ImportSpreadsheetDialog;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.viewmodels.CGroupsViewModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class CGroupsFragment extends AbsRecyclerViewFragment implements
        ImportSpreadsheetDialog.OnImportListener,
        FABCoordinator.OnFABClickListener,
        FABCoordinator.FABDrawableCoordination,
        OnItemClickListener {

    private CGroupsViewModel mViewModel;

    private LinearLayoutManager mLayoutManager;
    private CGroupsAdapter mAdapter;

    @BindView(R.id.empty_state) View mEmptyState;

    @Override
    protected void onCreateView() {
        mLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);

            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CGroupsAdapter(getContext(), null, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_cgroups;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CGroupsViewModel.class);
        mViewModel.getContactsLists().observe(this, this::setData);
    }

    // -- FABCoordinator -- //

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_add_black_24dp,
                -1 //This means no FAB at all
        };
    }

    @Override
    public void onRightClick() {
        new ImportSpreadsheetDialog.Builder(getChildFragmentManager())
                .onImportListener(this)
                .show(new ImportSpreadsheetDialog());
    }

    @Override
    public void onLeftClick() {
    }

    // - Adapter - //

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
        CGroup cGroup = (CGroup) data;
        Intent intent = new Intent(getContext(), CGroupActivity.class);
        intent.putExtra(CGroupActivity.EXTRA_LIST_ID, cGroup.getListId());
        startActivity(intent);
    }

    // - Dialog - //

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

    private void setData(List<CGroupAndItsContacts> cGroups) {
        mAdapter.setData(cGroups);
        if (cGroups.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }
}
