package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseFragment;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment<A extends CursorAdapter> extends BaseFragment implements CursorMvpView, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    protected A mAdapter;
    private CursorMvpPresenter<CursorMvpView> mPresenter;
    private OnLoadFinishedListener mOnLoadFinishedListener;

    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.list_item_header) protected TextView mAnchoredHeader;
    @BindView(R.id.empty_state) protected View mEmptyState;
    @BindView(R.id.empty_title) protected TextView mEmptyTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return mPresenter.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPresenter.onLoadFinished(loader, data);
        if (mOnLoadFinishedListener != null) {
            mOnLoadFinishedListener.onLoadFinished();
        }
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
        mAdapter = getAdapter();

        mPresenter = new CursorPresenter<>();
        mPresenter.onAttach(this);

        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());

        // TODO make this no permission handling better
        if (!hasPermissions()) {
            mPresenter.onNoPermissions();
            mEmptyTitle.setText(getString(R.string.empty_list_no_permissions));
        }
    }

    @Override
    public void setData(Cursor cursor) {
        mAdapter.setCursor(cursor);
    }

    @Override
    public int getSize() {
        return mAdapter.getItemCount();
    }

    @Override
    public void load() {
        if (hasPermissions()) {
            LoaderManager.getInstance(this).restartLoader(LOADER_ID, getArguments(), this);
        } else {
            mPresenter.onNoPermissions();
        }
    }

    @Override
    public void showEmptyPage(boolean isShow) {
        mEmptyState.setVisibility(isShow ? VISIBLE : GONE);
        mRecyclerView.setVisibility(isShow ? GONE : VISIBLE);
    }

    @Override
    public void setRefreshing(boolean isRefresh) {
        if (!isRefresh && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        } else if (isRefresh && !mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    public void setOnLoadFinishedListener(OnLoadFinishedListener onLoadFinishedListener) {
        mOnLoadFinishedListener = onLoadFinishedListener;
    }

    public interface OnLoadFinishedListener {
        void onLoadFinished();
    }

    public abstract A getAdapter();
}
