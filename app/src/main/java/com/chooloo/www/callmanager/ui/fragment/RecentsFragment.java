package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.RecentsAdapter;
import com.chooloo.www.callmanager.adapter.listener.OnItemClickListener;
import com.chooloo.www.callmanager.adapter.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.google.FastScroller;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;

public class RecentsFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FABCoordinator.FABDrawableCoordination,
        FABCoordinator.OnFABClickListener,
        View.OnScrollChangeListener, OnItemClickListener, OnItemLongClickListener {

    private static final int LOADER_ID = 1;

    LinearLayoutManager mLayoutManager;
    RecentsAdapter mRecentsAdapter;

    // ViewBinds
    @BindView(R.id.recents_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.fast_scroller) FastScroller mFastScroller;

    @BindView(R.id.empty_state) View mEmptyState;
    @BindView(R.id.empty_title) TextView mEmptyTitle;
    @BindView(R.id.empty_desc) TextView mEmptyDesc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    // -- Overrides -- //

    @Override
    protected void onFragmentReady() {
        mLayoutManager =
                new LinearLayoutManager(getContext()) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                        if (mRecentsAdapter.getItemCount() > itemsShown) {
                            mFastScroller.setVisibility(View.VISIBLE);
                            mRecyclerView.setOnScrollChangeListener(RecentsFragment.this);
                        } else {
                            mFastScroller.setVisibility(View.GONE);
                        }
                    }
                };
        mRecentsAdapter = new RecentsAdapter(getContext(), null, this, this);

        // mRecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecentsAdapter);

        // mRefreshLayout
        mRefreshLayout.setOnRefreshListener(() -> {
            LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, null, RecentsFragment.this);
            tryRunningLoader();
        });

        mEmptyTitle.setText(R.string.empty_recents_title);
        mEmptyDesc.setText(R.string.empty_recents_desc);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tryRunningLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRunningLoader();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
        RecentCall recentCall = (RecentCall) data;
        CallManager.call(this.getContext(), recentCall.getCallerNumber());
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, Object data) {
        RecentCall recentCall = (RecentCall) data;
        showContactPopup(ContactUtils.getContactByPhoneNumber(getContext(), recentCall.getCallerNumber()));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mRecentsAdapter.changeCursor(null);
    }

    // -- Loader -- //

    private void tryRunningLoader() {
        if (!isLoaderRunning() && Utilities.checkPermissionGranted(getContext(), Manifest.permission.READ_CALL_LOG)) {
            runLoader();
        }
    }

    private void runLoader() {
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    private boolean isLoaderRunning() {
        Loader loader = LoaderManager.getInstance(this).getLoader(LOADER_ID);
        return loader != null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        // Strings
        final String[] PROJECTION = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
        };
        String SELECTION = null;
        String ORDER = CallLog.Calls.DATE + " DESC";

        // Return cursor
        return new CursorLoader(
                getContext(),
                CallLog.Calls.CONTENT_URI,
                PROJECTION,
                SELECTION,
                null,
                ORDER);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        setData(data);
    }

    private void setData(Cursor data) {
        mRecentsAdapter.changeCursor(data);
        mFastScroller.setup(mRecentsAdapter, mLayoutManager);
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
        if (data != null && data.getCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    // -- FABCoordinator.OnFabClickListener -- //

    @Override
    public void onRightClick() {
        ((MainActivity) getActivity()).expandDialer(true);
    }

    @Override
    public void onLeftClick() {
        ((MainActivity) getActivity()).toggleSearchBar();
    }

    // -- FABCoordinator.FABDrawableCoordinator -- //

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_dialpad_black_24dp,
                R.drawable.ic_search_black_24dp
        };
    }

    // -- Other -- //

    /**
     * Shows a pop up window (dialog) with the contact's information
     *
     * @param contact
     */
    private void showContactPopup(Contact contact) {

        // Initiate the dialog
        Dialog contactDialog = new Dialog(getContext());
        contactDialog.setContentView(R.layout.contact_popup_view);

        // Views declarations
        TextView contactName;
        TextView contactNumber;
        ConstraintLayout popupLayout;

        contactName = (TextView) contactDialog.findViewById(R.id.contact_popup_name);
        contactNumber = (TextView) contactDialog.findViewById(R.id.contact_popup_number);
        popupLayout = (ConstraintLayout) contactDialog.findViewById(R.id.contact_popup_layout);

        if (contact.getName() != null)
            contactName.setText(contact.getName());
        else contactName.setText(contact.getMainPhoneNumber());

        contactNumber.setText(contact.getMainPhoneNumber());
        popupLayout.setElevation(20);

        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contactDialog.show();

    }
}

