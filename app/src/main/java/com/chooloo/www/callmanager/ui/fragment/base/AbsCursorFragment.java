package com.chooloo.www.callmanager.ui.fragment.base;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.AbsFastScrollerAdapter;
import com.chooloo.www.callmanager.ui.FastScroller;
import com.chooloo.www.callmanager.util.PermissionUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Made by Chooloo
 * A basic cursor fragment
 * Optimized and adapted for Koler's use, not recommended for every cursor fragment
 * Adapted for item name search and for number search (Can be used not only with phone and contact context)
 * Has a predefined layout manager and recycler view and a refresh layout
 * <p>
 * The parameters thats needs to be initialized in child classes are context, adapter and cursor loader
 * To implement a cursor loader, override the function onCreateLoader and return your own cursoe loader instance
 * In Koler's case, the number and contact arguments are being used by both cursor loaders in use
 * But the arguments can be removed to make the class more modular and adaptive
 * </p>
 * There's a pre defined permission button, to make it relevant, initialize the required permissions list
 * If permission aren't given, the list won't load and an "Enable Permission" button will show
 * Also when list is empty due are no data is showen, an empty state screen will show
 * You can implement in onFragmentReady and check if the list isnt loading due to permissions,
 * you can set your own text in the empty state elements
 */
public class AbsCursorFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnScrollChangeListener {

    // Constants
    protected static final int LOADER_ID = 1;
    protected static final String ARG_PHONE_NUMBER = "phone_number";
    protected static final String ARG_CONTACT_NAME = "contact_name";

    // Bind Views
    @BindView(R.id.fast_scroller) protected FastScroller mFastScroller;
    @BindView(R.id.refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.enable_permission_btn) protected Button mEnablePermissionButton;
    @BindView(R.id.item_header) protected TextView mAnchoredHeader;
    @BindView(R.id.empty_state) protected View mEmptyState;
    @BindView(R.id.empty_title) protected TextView mEmptyTitle;
    @BindView(R.id.empty_desc) protected TextView mEmptyDesc;

    // Variables
    protected Context mContext;
    protected AbsFastScrollerAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected String[] mRequiredPermissions;
    protected String mPhoneNumber = null;
    protected String mContactName = null;
    protected OnLoadFinishedListener mOnLoadFinishedListener = null;

    protected AbsCursorFragment(Context context) {
        this.mContext = context;
    }

    protected AbsCursorFragment(Context context, String phoneNumber, String contactName) {
        this.mContext = context;
        this.mPhoneNumber = phoneNumber;
        this.mContactName = contactName;
    }

    @Override
    protected void onFragmentReady() {

        // layout manger
        mLayoutManager =
                new LinearLayoutManager(mContext) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                        if (mAdapter.getItemCount() > itemsShown) {
                            mFastScroller.setVisibility(View.VISIBLE);
                            mRecyclerView.setOnScrollChangeListener(AbsCursorFragment.this);
                        } else {
                            mFastScroller.setVisibility(View.GONE);
                        }
                    }
                };

        // recycle view
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // refresh layout
        mRefreshLayout.setOnRefreshListener(this::load);

        togglePermissionButton();
        load(mPhoneNumber, mContactName);
        Timber.i("Loading recents fragment with phone number: " + mPhoneNumber + " | contact number: " + mContactName);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        setData(data);
        if (mOnLoadFinishedListener != null) mOnLoadFinishedListener.onLoadFinished();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        togglePermissionButton();
        load();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    // -- On Click -- //

    @OnClick(R.id.enable_permission_btn)
    public void enablePermissionClick() {
        PermissionUtils.askForPermissions(this, mRequiredPermissions);
    }

    // -- Loader -- //

    /**
     * Run loader but first check if possible
     *
     * @param phoneNumber number filter
     * @param contactName name filter
     */
    public void load(@Nullable String phoneNumber, @Nullable String contactName) {
        if (PermissionUtils.checkPermissionsGranted(mContext, mRequiredPermissions, false))
            runLoader(phoneNumber, contactName);
    }

    public void load() {
        load(mPhoneNumber, mContactName);
    }

    /**
     * Run loader without considering anything
     *
     * @param phoneNumber number filter
     * @param contactName name filter
     */
    private void runLoader(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
    }

    /**
     * Check is the loader currently running
     * Basically is the list updating
     *
     * @return boolean is loader running or not
     */
    protected boolean isLoaderRunning() {
        return LoaderManager.getInstance(this).getLoader(LOADER_ID) != null;
    }

    /**
     * Set new data from a given cursor
     * Basically update the list
     *
     * @param data the cursor holding the new data
     */
    private void setData(Cursor data) {
        mAdapter.changeCursor(data);
        mFastScroller.setup(mAdapter, mLayoutManager);
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
        if (data != null && data.getCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checking whither to show the "enable contacts" button
     */
    public void togglePermissionButton() {
        boolean isPermissionGranted = PermissionUtils.checkPermissionsGranted(mContext, mRequiredPermissions, false);
        mEnablePermissionButton.setVisibility(isPermissionGranted ? View.GONE : View.VISIBLE);
    }

    public int size() {
        return mAdapter.getItemCount();
    }

    public void setOnLoadFinishListener(OnLoadFinishedListener onLoadFinishListener) {
        mOnLoadFinishedListener = onLoadFinishListener;
    }

    public interface OnLoadFinishedListener {
        public void onLoadFinished();
    }
}
