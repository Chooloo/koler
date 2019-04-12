package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.ContactsAdapter;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.Utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import timber.log.Timber;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class ContactsFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ContactsAdapter.OnChildClickListener {

    private static final int LOADER_ID = 1;
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_CONTACT_NAME = "contact_name";

    private static final String IGNORE_NUMBER_TOO_LONG_CLAUSE =
            "length(" + Phone.NUMBER + ") < 1000";


    private SharedDialViewModel mSharedDialViewModel;
    private SharedSearchViewModel mSharedSearchViewModel;
    private View mRootView;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    ContactsAdapter mAdapter;

    @Override
    protected void onCreateView() {
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactsAdapter(getContext(), null);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnChildClickListener(this);
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

        Uri.Builder builder;
        final String[] PROJECTION = new String[]{
                Phone._ID,
                Phone.DISPLAY_NAME_PRIMARY,
                Phone.PHOTO_THUMBNAIL_URI,
                Phone.NUMBER
        };
        String selection = Data.MIMETYPE + " = '" + Phone.CONTENT_ITEM_TYPE + "' AND " +
                RawContacts.ACCOUNT_TYPE + " NOT LIKE '%whatsapp%' AND " +
                Contacts.IN_VISIBLE_GROUP + "=1 AND " +
                Contacts.HAS_PHONE_NUMBER + "=1 AND " +
                IGNORE_NUMBER_TOO_LONG_CLAUSE;
        String sortOrder = Phone.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC";

        String contactName = null;
        String phoneNumber = null;
        if (args != null && args.containsKey(ARG_PHONE_NUMBER)) {
            phoneNumber = args.getString(ARG_PHONE_NUMBER);
        } else if (args != null && args.containsKey(ARG_CONTACT_NAME)) {
            contactName = args.getString(ARG_CONTACT_NAME);
        }

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
            builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        } else if (contactName != null && !contactName.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon();
            builder.appendQueryParameter(ContactsContract.PRIMARY_ACCOUNT_NAME, "true");
        } else {
            builder = Phone.CONTENT_URI.buildUpon();
        }

        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");

        return new CursorLoader(
                getContext(),
                builder.build(),
                PROJECTION,
                selection,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        Timber.d(DatabaseUtils.dumpCursorToString(data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    /**
     * Call the contact on click
     *
     * @param normPhoneNumber
     */
    @Override
    public void onChildClick(String normPhoneNumber) {
//        mSharedDialViewModel.setNumber(normPhoneNumber);
        Utilities.call(this.getContext(), normPhoneNumber);
    }
}
