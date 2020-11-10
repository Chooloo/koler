package com.chooloo.www.callmanager.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.CursorAdapter;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.ui.widgets.FastScroller;

import butterknife.BindView;

import static android.Manifest.permission.READ_CONTACTS;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

public class ContactsFragment extends CursorFragment implements ContactsMvpView {

    private static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};
    private final static String ARG_PHONE_NUMBER = "phoneNumber";
    private final static String ARG_CONTACT_NAME = "contactName";

    private ContactsAdapter<ListItemHolder> mAdapter;
    private ContactsPresenter<ContactsMvpView> mPresenter;

    @BindView(R.id.fast_scroller) protected FastScroller mFastScroller;

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        fragment.setRequiredPermissions(REQUIRED_PERMISSIONS);
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public static ContactsFragment newInstance(@Nullable String phoneNumber, @Nullable String contactNumber) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactNumber);
        fragment.setArguments(args);
        fragment.setRequiredPermissions(REQUIRED_PERMISSIONS);
        return fragment;
    }

    @Override
    public void setUp() {
        mPresenter = new ContactsPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        mAdapter = new ContactsAdapter<>(mActivity);
        mAdapter.setOnContactItemClick(new ContactsAdapter.OnContactItemClickListener() {
            @Override
            public void onContactItemClick(Contact contact) {
                mPresenter.onContactItemClick(contact);
            }

            @Override
            public void onContactItemLongClick(Contact contact) {
                mPresenter.onContactItemLongClick(contact);
            }
        });

        mLayoutManager = new LinearLayoutManager(mActivity) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                mFastScroller.setVisibility(mAdapter.getItemCount() > itemsShown ? VISIBLE : GONE);
            }
        };

        mFastScroller.setup(mAdapter, mLayoutManager);
        mFastScroller.setFastScrollerHeaderManager(new FastScroller.FastScrollerHeaderManager() {
            @Override
            public String getHeaderString(int position) {
                return mAdapter.getHeader(position);
            }

            @Override
            public void refreshHeaders() {
                mPresenter.onRefreshHeaders();
            }
        });

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mPresenter.onScrolled();
            }
        });

        load();
        super.setUp();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String phoneNumber = null;
        String contactName = null;
        if (args != null) {
            phoneNumber = args.getString(ARG_PHONE_NUMBER, null);
            contactName = args.getString(ARG_CONTACT_NAME, null);
        }
        boolean withFavs = (contactName == null || contactName.isEmpty()) && (phoneNumber == null || phoneNumber.isEmpty());
        return new FavoritesAndContactsLoader(mActivity, phoneNumber, contactName, withFavs);
    }

    @Override
    public void openContact(Contact contact) {

    }

    @Override
    public String getHeader(int position) {
        return mAdapter.getHeader(position);
    }

    @Override
    public void refreshHeaders() {
        mAdapter.refreshHeaders();
    }

    @Override
    public void updateScroll() {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int firstCompletelyVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition();

        // No items are visible, so there are no headers to update.
        if (firstCompletelyVisible == RecyclerView.NO_POSITION) {
            return;
        }

        String anchoredHeaderString = mAdapter.getHeader(firstCompletelyVisible);

        // If the user swipes to the top of the list very quickly, there is some strange behavior
        // between this method updating headers and adapter#onBindViewHolder updating headers.
        // To overcome this, we refresh the headers to ensure they are correct.
        if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
            mAdapter.refreshHeaders();
            mAnchoredHeader.setVisibility(INVISIBLE);
        } else {
            boolean headerIsAnchored = mAdapter.getHeader(firstVisibleItem).equals(anchoredHeaderString);
            mAnchoredHeader.setVisibility(headerIsAnchored ? VISIBLE : INVISIBLE);
            getContactHolder(firstVisibleItem).showHeader(!headerIsAnchored);
            getContactHolder(firstCompletelyVisible).showHeader(!headerIsAnchored);
            if (headerIsAnchored) {
                mAnchoredHeader.setText(anchoredHeaderString);
            }
        }
    }

    @Override
    public CursorAdapter getAdapter() {
        return mAdapter;
    }

    public void load(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        load(args);
    }

    private ListItemHolder getContactHolder(int position) {
        return ((ListItemHolder) mRecyclerView.findViewHolderForAdapterPosition(position));
    }
}
