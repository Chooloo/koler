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
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.base.CursorAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class CursorFragment extends BaseFragment implements CursorMvpView, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private CursorPresenter<CursorMvpView> mPresenter;
    private OnLoadFinishedListener mOnLoadFinishedListener = null;
    protected LinearLayoutManager mLayoutManager;

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
        mPresenter = new CursorPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrolled();
            }
        });

        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());

        togglePermissionButton();
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
        } else {
            askForPermissions();
        }
    }

    @Override
    public void load(@Nullable Bundle args) {
        setArguments(args);
        load();
    }

    @Override
    public void runLoader(Bundle args) {
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
    }

    @Override
    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    public void setOnLoadFinishListener(OnLoadFinishedListener onLoadFinishListener) {
        mOnLoadFinishedListener = onLoadFinishListener;
    }

    public interface OnLoadFinishedListener {
        void onLoadFinished();
    }

    public abstract CursorAdapter getAdapter();

}
