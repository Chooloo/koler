package com.chooloo.www.callmanager.ui.fragment.cursor;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.ContactsAdapter;
import com.chooloo.www.callmanager.adapter.base.CursorAdapter;
import com.chooloo.www.callmanager.ui.fragment.base.BaseFragment;
import com.chooloo.www.callmanager.ui2.FastScroller;
import com.chooloo.www.callmanager.ui2.ListItemHolder;
import com.chooloo.www.callmanager.util.PermissionUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment<A extends CursorAdapter<RecyclerView.ViewHolder>, P extends CursorPresenter<CursorContract.View>> extends BaseFragment implements CursorContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int LOADER_ID = 1;

    protected P mPresenter;
    protected A mAdapter;
    protected Context mContext;
    protected Activity mActivity;
    protected LinearLayoutManager mLayoutManager;
    protected String[] mRequiredPermissions;
    protected OnLoadFinishedListener mOnLoadFinishedListener = null;

    @BindView(R.id.recycler_view) public RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.enable_permission_btn) protected Button mEnablePermissionButton;
    @BindView(R.id.item_header) protected TextView mAnchoredHeader;
    @BindView(R.id.empty_state) protected View mEmptyState;
    @BindView(R.id.empty_title) protected TextView mEmptyTitle;
    @BindView(R.id.empty_desc) protected TextView mEmptyDesc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getContext();
        mActivity = getActivity();

        mPresenter = (P) new CursorPresenter<>();
        mPresenter.bind(this, getLifecycle());
        mPresenter.subscribe(mContext);

        setUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @OnClick(R.id.enable_permission_btn)
    public void enablePermissionClick() {
        mPresenter.onEnablePermissionClick();
    }

    @Override
    public void setUp() {
        mAdapter = (A) new CursorAdapter(mContext);

        mRecyclerView.setOnScrollChangeListener((view, i, i1, i2, i3) -> mPresenter.onScrollChange(view, i, i1, i2, i3));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());
    }

    @Override
    public String[] getRequiredPermissions() {
        return mRequiredPermissions;
    }

    @Override
    public boolean isPermissionGranted() {
        return PermissionUtils.checkPermissionsGranted(mContext, mRequiredPermissions, false);
    }

    @Override
    public void togglePermissionButton() {
        mEnablePermissionButton.setVisibility(isPermissionGranted() ? GONE : VISIBLE);
    }

    @Override
    public void setData(Cursor cursor) {
        mAdapter.setCursor(cursor);
        mRefreshLayout.setRefreshing(!mRefreshLayout.isRefreshing());

        boolean isCursorEmpty = cursor == null || cursor.getCount() == 0;
        mRecyclerView.setVisibility(isCursorEmpty ? GONE : VISIBLE);
        mEmptyState.setVisibility(isCursorEmpty ? VISIBLE : GONE);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        mAdapter.setCursor(cursor);
    }

    // Loader

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPresenter.onLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mPresenter.onLoaderReset(loader);
        if (mOnLoadFinishedListener != null) mOnLoadFinishedListener.onLoadFinished();
    }

    @Override
    public void load() {
        if (isPermissionGranted()) {
            runLoader(getArguments());
        }
    }

    @Override
    public void runLoader(Bundle args) {
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
    }

    // Listener

    public void setOnLoadFinishListener(OnLoadFinishedListener onLoadFinishListener) {
        mOnLoadFinishedListener = onLoadFinishListener;
    }

    public interface OnLoadFinishedListener {
        void onLoadFinished();
    }
}
