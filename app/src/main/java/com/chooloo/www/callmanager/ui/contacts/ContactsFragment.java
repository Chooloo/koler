package com.chooloo.www.callmanager.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;
import com.chooloo.www.callmanager.ui2.FastScroller;

import butterknife.BindView;

import static android.Manifest.permission.READ_CONTACTS;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ContactsFragment extends CursorFragment implements ContactsMvpView {

    public static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};

    private final static String ARG_PHONE_NUMBER = "phoneNumber";
    private final static String ARG_CONTACT_NAME = "contactName";

    @BindView(R.id.fast_scroller) protected FastScroller mFastScroller;

    public static ContactsFragment newInstance(@Nullable String phoneNumber, @Nullable String contactNumber) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUp() {
        mAdapter = new ContactsAdapter();
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

        mFastScroller.setup(mAdapter, mLayoutManager);
        mFastScroller.setFastScrollerHeaderManager(new FastScroller.FastScrollerHeaderManager() {
            @Override
            public String getHeaderString(int position) {
                return mAdapter.getHeader(position);
            }

            @Override
            public void refreshHeaders() {
                this.refreshHeaders();
            }
        });

        mLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                mFastScroller.setScrollBarSize(itemsShown);
            }
        };

        mRecyclerView.setOnScrollChangeListener((view, i, i1, i2, i3) -> mPresenter.onScrollChange(view, i, i1, i2, i3));

        mRequiredPermissions = REQUIRED_PERMISSIONS;

        super.setUp();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String phoneNumber = args.getString(ARG_PHONE_NUMBER);
        String contactName = args.getString(ARG_CONTACT_NAME);
        boolean withFavs = (contactName == null || contactName.isEmpty()) && (phoneNumber == null || phoneNumber.isEmpty());
        return new FavoritesAndContactsLoader(mContext, phoneNumber, contactName, withFavs);
    }

    @Override
    public void openContact(Contact contact) {

    }

    @Override
    public String getHeader(int position) {
        return mAdapter.getHeader(position);
    }

    @Override
    public void showAnchoredHeader(boolean isShow) {
        mAnchoredHeader.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void setAnchoredHeader(String header) {
        mAnchoredHeader.setText(header);
    }

    @Override
    public void refreshHeaders() {
        mAdapter.refreshHeaders();
    }

    @Override
    public void updateFastScrollerPosition() {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
    }

    @Override
    public int getFirstVisibleItem() {
        return mLayoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public int getFirstCompletelyVisibleItem() {
        return mLayoutManager.findFirstCompletelyVisibleItemPosition();
    }
}
