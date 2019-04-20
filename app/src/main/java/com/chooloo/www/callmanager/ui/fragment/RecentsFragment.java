package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.RecentsAdapter;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.google.FastScroller;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;

public class RecentsFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnScrollChangeListener,
        RecentsAdapter.OnChildClickListener{

    private static final int LOADER_ID = 1;
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_CONTACT_NAME = "contact_name";

    private SharedDialViewModel mSharedDialViewModel;
    private SharedSearchViewModel mSharedSearchViewModel;

    LinearLayoutManager mLayoutManager;
    RecentsAdapter mRecentsAdapter;

    @BindView(R.id.recents_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.fast_scroller) FastScroller mFastScroller;

    @Override
    protected void onCreateView() {
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
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecentsAdapter = new RecentsAdapter(getContext(), null);
        mRecyclerView.setAdapter(mRecentsAdapter);

        mRecentsAdapter.setOnChildClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mSharedDialViewModel.setIsOutOfFocus(false);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mSharedDialViewModel.setIsOutOfFocus(true);
                        mSharedSearchViewModel.setIsFocused(false);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        mSharedDialViewModel.setIsOutOfFocus(true);
                        mSharedSearchViewModel.setIsFocused(false);
                        break;
                    default:
                        mSharedDialViewModel.setIsOutOfFocus(false);
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(() -> {
            LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, null, RecentsFragment.this);
            tryRunningLoader();
        });
        mRefreshLayout.setColorSchemeColors(getContext().getColor(R.color.red_phone));
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_recents;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, s -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_PHONE_NUMBER, s);
                LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, args, RecentsFragment.this);
            }
        });
        mSharedSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getText().observe(this, t -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_CONTACT_NAME, t);
                LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, args, RecentsFragment.this);
            }
        });
        tryRunningLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRunningLoader();
    }

    private void tryRunningLoader() {
        if (!isLoaderRunning() && Utilities.checkPermissionGranted(getContext(), Manifest.permission.READ_CONTACTS)) {
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


        // Check if a specific name/number is required
        String contactName = null;
        String phoneNumber = null;

        if (args != null && args.containsKey(ARG_PHONE_NUMBER)) {
            phoneNumber = args.getString(ARG_PHONE_NUMBER);
        } else if (args != null && args.containsKey(ARG_CONTACT_NAME)) {
            contactName = args.getString(ARG_CONTACT_NAME);
        }

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            SELECTION = CallLog.Calls.NUMBER + " = " + phoneNumber;
        } else if (contactName != null && !contactName.isEmpty()) {
            SELECTION = CallLog.Calls.CACHED_NAME + " = " + contactName;
        }

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
        mRecentsAdapter.changeCursor(data);
        mFastScroller.setup(mRecentsAdapter, mLayoutManager);
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mRecentsAdapter.changeCursor(null);
    }

    /**
     * Call the contact on click
     *
     * @param normPhoneNumber
     */
    @Override
    public void onChildClick(String normPhoneNumber) {
//        mSharedDialViewModel.setNumber(normPhoneNumber);
        CallManager.call(this.getContext(), normPhoneNumber);
    }

    @Override
    public boolean onChildLongClick(Contact contact) {
        // TODO make a pop window with the contact's details
        return true;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
    }
}
