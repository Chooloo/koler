package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.base.CursorAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment<A extends CursorAdapter> extends BaseFragment implements CursorMvpView, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    protected A mAdapter;
    private CursorPresenter<CursorMvpView> mPresenter;
    private OnLoadFinishedListener mOnLoadFinishedListener;

    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.enable_permission_btn) protected Button mEnablePermissionButton;
    @BindView(R.id.item_header) protected TextView mAnchoredHeader;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @OnClick(R.id.enable_permission_btn)
    public void enablePermissionClick() {
        mPresenter.onEnablePermissionClick();
    }

    @Override
    public void setUp() {
        mAdapter = getAdapter();

        mPresenter = new CursorPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());

        if (!hasPermissions()) {
            this.mEmptyTitle.setText(getString(R.string.empty_list_no_permissions));
            showEmptyPage(true);
        }
    }

    @Override
    public void togglePermissionButton() {
        mEnablePermissionButton.setVisibility(hasPermissions() ? GONE : VISIBLE);
    }

    @Override
    public void setData(Cursor cursor) {
        getAdapter().setCursor(cursor);
        showEmptyPage(cursor == null || cursor.getCount() == 0);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        setRefreshing(true);
        return null;
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
    public void load() {
        if (hasPermissions()) {
            runLoader();
        } else {
            showEmptyPage(true);
            togglePermissionButton();
        }
    }

    @Override
    public void load(@Nullable Bundle args) {
        setArguments(args);
        load();
    }

    @Override
    public void runLoader() {
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, getArguments(), this);
    }

    @Override
    public int getSize() {
        return mAdapter.getItemCount();
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

    public interface OnLoadFinishedListener {
        void onLoadFinished();
    }

    public void setOnLoadFinishedListener(OnLoadFinishedListener onLoadFinishedListener) {
        mOnLoadFinishedListener = onLoadFinishedListener;
    }

    public abstract A getAdapter();
}
