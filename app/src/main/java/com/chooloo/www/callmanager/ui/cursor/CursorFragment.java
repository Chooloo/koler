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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.FragmentItemsBinding;
import com.chooloo.www.callmanager.ui.base.BaseFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment<A extends CursorAdapter> extends BaseFragment implements CursorMvpView, LoaderManager.LoaderCallbacks<Cursor> {

    protected static int mLoaderId = 1;

    protected A mAdapter;
    private CursorMvpPresenter<CursorMvpView> mPresenter;
    protected FragmentItemsBinding binding;
    private RecyclerView.OnScrollListener mOnScrollListener;

    public CursorFragment() {
        mOnScrollListener = new RecyclerView.OnScrollListener() {
        };
    }

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
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
    }

    @Override
    public void onSetup() {
        mPresenter = new CursorPresenter<>();
        mPresenter.onAttach(this);

        mAdapter = onGetAdapter();

        binding.refreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());
        binding.itemsRecyclerView.setAdapter(mAdapter);
        binding.itemsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
        });

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
        } else if (mPresenter != null) {
            mPresenter.onNoPermissions();
        }
    }

    @Override
    public void runLoader() {
        if (LoaderManager.getInstance(this).getLoader(mLoaderId) == null) {
            LoaderManager.getInstance(this).initLoader(mLoaderId, getArgsSafely(), this);
        } else {
            LoaderManager.getInstance(this).restartLoader(mLoaderId, getArgsSafely(), this);
        }
    }

    @Override
    public void showEmptyPage(boolean isShow) {
        binding.emptyState.emptyState.setVisibility(isShow ? VISIBLE : GONE);
        binding.itemsRecyclerView.setVisibility(isShow ? GONE : VISIBLE);
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
    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public abstract A onGetAdapter();
}