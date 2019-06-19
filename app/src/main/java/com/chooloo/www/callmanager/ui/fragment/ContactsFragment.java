package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.chooloo.www.callmanager.google.FastScroller;
import com.chooloo.www.callmanager.google.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodels.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodels.SharedSearchViewModel;

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
        ContactsAdapter.OnContactSelectedListener {

    private static final int LOADER_ID = 1;
    private static final String ARG_SEARCH_PHONE_NUMBER = "phone_number";
    private static final String ARG_SEARCH_CONTACT_NAME = "contact_name";

    /**
     * An enum for the different types of headers that be inserted at position 0 in the list.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Header.NONE, Header.STAR})
    public @interface Header {
        int NONE = 0;
        int STAR = 1;
    }

    private SharedDialViewModel mSharedDialViewModel;
    private SharedSearchViewModel mSharedSearchViewModel;

    // View Binds
    @BindView(R.id.fast_scroller) FastScroller mFastScroller;
    @BindView(R.id.contacts_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.item_header) TextView mAnchoredHeader;

    LinearLayoutManager mLayoutManager;
    ContactsAdapter mContactsAdapter;

    @BindView(R.id.empty_state) View mEmptyState;
    @BindView(R.id.empty_title) TextView mEmptyTitle;
    @BindView(R.id.empty_desc) TextView mEmptyDesc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    protected void onFragmentReady() {
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

        // The list adapter
        mContactsAdapter = new ContactsAdapter(getContext(), null);
        mContactsAdapter.setOnContactSelectedListener(this);

        // Recycle View
        mRecyclerView.setAdapter(mContactsAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
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

        // Refresh Layout
        mRefreshLayout.setOnRefreshListener(() -> {
            LoaderManager.getInstance(ContactsFragment.this).restartLoader(LOADER_ID, null, ContactsFragment.this);
            tryRunningLoader();
        });

        mEmptyTitle.setText(R.string.empty_contact_title);
        mEmptyDesc.setText(R.string.empty_contact_desc);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Dialer View Model
        mSharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, s -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_SEARCH_PHONE_NUMBER, s);
                LoaderManager.getInstance(ContactsFragment.this).restartLoader(LOADER_ID, args, ContactsFragment.this);
            }
        });

        // Search Bar View Model
        mSharedSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getText().observe(this, t -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_SEARCH_CONTACT_NAME, t);
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

    /*
     * When our recycler view updates, we need to ensure that our row headers and anchored header
     * are in the correct state.
     *
     * The general rule is, when the row headers are shown, our anchored header is hidden. When the
     * recycler view is scrolling through a sublist that has more than one element, we want to show
     * out anchored header, to create the illusion that our row header has been anchored. In all
     * other situations, we want to hide the anchor because that means we are transitioning between
     * two sublists.
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int firstCompletelyVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisible == RecyclerView.NO_POSITION) {
            // No items are visible, so there are no headers to update.
            return;
        }
        String anchoredHeaderString = mContactsAdapter.getHeaderString(firstCompletelyVisible);

        // If the user swipes to the top of the list very quickly, there is some strange behavior
        // between this method updating headers and adapter#onBindViewHolder updating headers.
        // To overcome this, we refresh the headers to ensure they are correct.
        if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
            mContactsAdapter.refreshHeaders();
            mAnchoredHeader.setVisibility(View.INVISIBLE);
        } else {
            if (mContactsAdapter.getHeaderString(firstVisibleItem).equals(anchoredHeaderString)) {
                mAnchoredHeader.setText(anchoredHeaderString);
                mAnchoredHeader.setVisibility(View.VISIBLE);
                getContactHolder(firstVisibleItem).getHeaderView().setVisibility(View.INVISIBLE);
                getContactHolder(firstCompletelyVisible).getHeaderView().setVisibility(View.INVISIBLE);
            } else {
                mAnchoredHeader.setVisibility(View.INVISIBLE);
                getContactHolder(firstVisibleItem).getHeaderView().setVisibility(View.VISIBLE);
                getContactHolder(firstCompletelyVisible).getHeaderView().setVisibility(View.VISIBLE);
            }
        }
    }

    private ContactsAdapter.ContactHolder getContactHolder(int position) {
        return ((ContactsAdapter.ContactHolder) mRecyclerView.findViewHolderForAdapterPosition(position));
    }

    @Override
    public void onContactSelected(String normPhoneNumber) {
        if (normPhoneNumber == null) return;
        CallManager.call(this.getContext(), normPhoneNumber);
    }

    // -- Loader -- //

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String searchContactName = null;
        String searchPhoneNumber = null;
        if (args != null && args.containsKey(ARG_SEARCH_PHONE_NUMBER)) {
            searchPhoneNumber = args.getString(ARG_SEARCH_PHONE_NUMBER);
        }
        if (args != null && args.containsKey(ARG_SEARCH_CONTACT_NAME)) {
            searchContactName = args.getString(ARG_SEARCH_CONTACT_NAME);
        }

        boolean isSearchContactNameEmpty = searchContactName == null || searchContactName.isEmpty();
        boolean isSearchPhoneNumberEmpty = searchPhoneNumber == null || searchPhoneNumber.isEmpty();
        FavoritesAndContactsLoader cursorLoader = new FavoritesAndContactsLoader(getContext(), searchPhoneNumber, searchContactName);
        if (!isSearchContactNameEmpty || !isSearchPhoneNumberEmpty) { //Don't show favorites if the user is searching for a contact
            cursorLoader.setLoadFavorites(false);
        } else {
            cursorLoader.setLoadFavorites(true);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mContactsAdapter.changeCursor(null);
    }

    private void setData(Cursor data) {
        mContactsAdapter.changeCursor(data);
        mFastScroller.setup(mContactsAdapter, mLayoutManager);
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
     * Checks for the required permission in order to run the loader
     */
    private void tryRunningLoader() {
        if (!isLoaderRunning() && Utilities.checkPermissionsGranted(getContext(), Manifest.permission.READ_CONTACTS)) {
            runLoader();
        }
    }

    /**
     * Runs the loader
     */
    private void runLoader() {
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    /**
     * Checks whither the loader is currently running
     * (meaning the page is refreshing)
     *
     * @return boolean
     */
    private boolean isLoaderRunning() {
        Loader loader = LoaderManager.getInstance(this).getLoader(LOADER_ID);
        return loader != null;
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
}
