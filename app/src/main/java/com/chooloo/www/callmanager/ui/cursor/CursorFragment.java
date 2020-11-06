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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.CursorAdapter;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui2.ListItemHolder;
import com.chooloo.www.callmanager.util.PermissionUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment extends BaseFragment implements CursorMvpView, LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int LOADER_ID = 1;

    protected CursorPresenter<CursorMvpView> mPresenter;
    protected LinearLayoutManager mLayoutManager;
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

        mPresenter = new CursorPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        setUp();
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

    // OnClicks

    @OnClick(R.id.enable_permission_btn)
    public void enablePermissionClick() {
        mPresenter.onEnablePermissionClick();
    }

    // Mvp Overrides

    @Override
    public void setUp() {
        mRecyclerView.setOnScrollChangeListener((view, i, i1, i2, i3) -> mPresenter.onScrollChange(view, i, i1, i2, i3));
        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());

        togglePermissionButton();
        load();
    }

    @Override
    public String[] getRequiredPermissions() {
        return mRequiredPermissions;
    }

    @Override
    public void togglePermissionButton() {
        mEnablePermissionButton.setVisibility(hasPermissions() ? GONE : VISIBLE);
    }

    @Override
    public void setData(Cursor cursor) {
        getAdapter().setCursor(cursor);
        mRefreshLayout.setRefreshing(!mRefreshLayout.isRefreshing());

        boolean isCursorEmpty = cursor == null || cursor.getCount() == 0;
        mRecyclerView.setVisibility(isCursorEmpty ? GONE : VISIBLE);
        mEmptyState.setVisibility(isCursorEmpty ? VISIBLE : GONE);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        getAdapter().setCursor(cursor);
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
        if (hasPermissions()) {
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

    // Abstract

    public abstract CursorAdapter<> getAdapter();
}
