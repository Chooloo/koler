package com.chooloo.www.callmanager.ui.fragment.cursor.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.adapter.ContactsAdapter1;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.ui.fragment.cursor.CursorFragment;

import static android.Manifest.permission.READ_CONTACTS;

public class ContactsFragment extends CursorFragment {

    public static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};

    private final static String ARG_PHONE_NUMBER = "phoneNumber";
    private final static String ARG_CONTACT_NAME = "contactName";

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
        mAdapter = new ContactsAdapter1(mContext, null);
        mRequiredPermissions = REQUIRED_PERMISSIONS;
        mRecyclerView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
            int firstCompletelyVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (firstCompletelyVisible == RecyclerView.NO_POSITION)
                return; // No items are visible, so there are no headers to update.
            String anchoredHeaderString = mAdapter.getHeaderString(firstCompletelyVisible);

            // If the user swipes to the top of the list very quickly, there is some strange behavior
            // between this method updating headers and adapter#onBindViewHolder updating headers.
            // To overcome this, we refresh the headers to ensure they are correct.
            if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
                mAdapter.refreshHeaders();
                mAnchoredHeader.setVisibility(View.INVISIBLE);
            } else {
                if (mAdapter.getHeaderString(firstVisibleItem).equals(anchoredHeaderString)) {
                    mAnchoredHeader.setText(anchoredHeaderString);
                    mAnchoredHeader.setVisibility(View.VISIBLE);
                    getContactHolder(firstVisibleItem).header.setVisibility(View.INVISIBLE);
                    getContactHolder(firstCompletelyVisible).header.setVisibility(View.INVISIBLE);
                } else {
                    mAnchoredHeader.setVisibility(View.INVISIBLE);
                    getContactHolder(firstVisibleItem).header.setVisibility(View.VISIBLE);
                    getContactHolder(firstCompletelyVisible).header.setVisibility(View.VISIBLE);
                }
            }
        });
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
}
