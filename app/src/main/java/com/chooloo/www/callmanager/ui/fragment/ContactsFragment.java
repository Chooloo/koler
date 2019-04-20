package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.ContactsAdapter;
import com.chooloo.www.callmanager.google.ContactsCursorLoader;
import com.chooloo.www.callmanager.google.FastScroller;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.bla.CallManager;
import com.chooloo.www.callmanager.util.bla.Utilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;

/**
 * A {@link androidx.fragment.app.Fragment} that is heavily influenced by
 * Google's default dial app - <a href="ContactsFragment">https://android.googlesource.com/platform/packages/apps/Dialer/+/refs/heads/master/java/com/android/dialer/contactsfragment/ContactsFragment.java</a>
 */
public class ContactsFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FABCoordinator.FABDrawableCoordination,
        FABCoordinator.OnFABClickListener,
        View.OnScrollChangeListener,
        ContactsAdapter.OnContactSelectedListener{

    private static final int LOADER_ID = 1;
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_CONTACT_NAME = "contact_name";

    /**
     * An enum for the different types of headers that be inserted at position 0 in the list.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Header.NONE, Header.ADD_CONTACT})
    public @interface Header {
        int NONE = 0;
        /**
         * Header that allows the user to add a new contact.
         */
        int ADD_CONTACT = 1;
    }

    private SharedDialViewModel mSharedDialViewModel;
    private SharedSearchViewModel mSharedSearchViewModel;

    @BindView(R.id.fast_scroller) FastScroller mFastScroller;
    @BindView(R.id.contacts_refresh_layout) SwipeRefreshLayout mRefreshLayout;

    LinearLayoutManager mLayoutManager;
    ContactsAdapter mContactsAdapter;

    @Override
    protected void onCreateView() {
        mLayoutManager =
                new LinearLayoutManager(getContext()) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                        if (mContactsAdapter.getItemCount() > itemsShown) {
                            mFastScroller.setVisibility(View.VISIBLE);
                            mRecyclerView.setOnScrollChangeListener(ContactsFragment.this);
                        } else {
                            mFastScroller.setVisibility(View.GONE);
                        }
                    }
                };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mContactsAdapter = new ContactsAdapter(getContext(), null);
        mRecyclerView.setAdapter(mContactsAdapter);

        mContactsAdapter.setOnContactSelectedListener(this);
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
                    default:
                        mSharedDialViewModel.setIsOutOfFocus(false);
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(() -> {
            LoaderManager.getInstance(ContactsFragment.this).restartLoader(LOADER_ID, null, ContactsFragment.this);
            tryRunningLoader();
        });
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red_phone));
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, s -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_PHONE_NUMBER, s);
                LoaderManager.getInstance(ContactsFragment.this).restartLoader(LOADER_ID, args, ContactsFragment.this);
            }
        });
        mSharedSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getText().observe(this, t -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_CONTACT_NAME, t);
                LoaderManager.getInstance(ContactsFragment.this).restartLoader(LOADER_ID, args, ContactsFragment.this);
            }
        });
        tryRunningLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRunningLoader();
    }

    @Override
    public void onRightClick() {
        ((MainActivity) getActivity()).expandAppBar(true);
    }

    @Override
    public void onLeftClick() {
        MainActivity mainActivity = (MainActivity) getActivity();
        boolean isOpened = mainActivity.isSearchBarVisible();
        mainActivity.toggleSearchBar(!isOpened);
    }

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_dialpad_black_24dp,
                R.drawable.ic_search_black_24dp
        };
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

        String contactName = null;
        String phoneNumber = null;
        if (args != null && args.containsKey(ARG_PHONE_NUMBER)) {
            phoneNumber = args.getString(ARG_PHONE_NUMBER);
        } else if (args != null && args.containsKey(ARG_CONTACT_NAME)) {
            contactName = args.getString(ARG_CONTACT_NAME);
        }

        ContactsCursorLoader cursorLoader = new ContactsCursorLoader(getContext(), phoneNumber, contactName);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mContactsAdapter.changeCursor(data);
        mFastScroller.setup(mContactsAdapter, mLayoutManager);
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mContactsAdapter.changeCursor(null);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
    }

    /**
     * Call the contact on click
     *
     * @param normPhoneNumber
     */
    @Override
    public void onContactSelected(String normPhoneNumber) {
        if (normPhoneNumber == null) return;
//        mSharedDialViewModel.setNumber(normPhoneNumber);
        CallManager.call(this.getContext(), normPhoneNumber);
    }
}
