package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.FragmentItemsBinding;
import com.chooloo.www.callmanager.ui.base.BaseFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment<A extends CursorAdapter> extends BaseFragment implements CursorMvpView, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    protected A mAdapter;
    private CursorMvpPresenter<CursorMvpView> mPresenter;
    protected FragmentItemsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentItemsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(permissions);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return mPresenter.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPresenter.onLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mPresenter.onLoaderReset(loader);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void setUp() {

        mPresenter = new CursorPresenter<>();
        mPresenter.onAttach(this);

        mAdapter = getAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        
        binding.refreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());

        // TODO make this no permission handling better
        if (!hasPermissions()) {
            mPresenter.onNoPermissions();
            binding.emptyState.emptyTitle.setText(getString(R.string.empty_list_no_permissions));
        }
    }

    @Override
    public void updateData(Cursor cursor) {
        mAdapter.setCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public void load() {
        if (hasPermissions()) {
            runLoader();
        } else {
            mPresenter.onNoPermissions();
        }
    }

    @Override
    public void runLoader() {
        if (LoaderManager.getInstance(this).getLoader(LOADER_ID) == null) {
            LoaderManager.getInstance(this).initLoader(LOADER_ID, getArgsSafely(), this);
        } else {
            LoaderManager.getInstance(this).restartLoader(LOADER_ID, getArgsSafely(), this);
        }
    }

    @Override
    public void showEmptyPage(boolean isShow) {
        binding.emptyState.emptyState.setVisibility(isShow ? VISIBLE : GONE);
        binding.recyclerView.setVisibility(isShow ? GONE : VISIBLE);
    }

    @Override
    public void setRefreshing(boolean isRefresh) {
        if (!isRefresh && binding.refreshLayout.isRefreshing()) {
            binding.refreshLayout.setRefreshing(false);
        } else if (isRefresh && !binding.refreshLayout.isRefreshing()) {
            binding.refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        binding.recyclerView.addOnScrollListener(onScrollListener);
    }

    public abstract A getAdapter();
}
