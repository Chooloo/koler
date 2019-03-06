package com.chooloo.www.callmanager.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.ContactsAdapter;
import com.chooloo.www.callmanager.util.Utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ContactsAdapter.OnChildClickListener {

    private static final int LOADER_ID = 1;
    private static final String ARG_PHONE_NUMBER = "phone_number";

    private static final String IGNORE_NUMBER_TOO_LONG_CLAUSE =
            "length(" + Phone.NUMBER + ") < 1000";


    private SharedDialViewModel mSharedDialViewModel;
    private View mRootView;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    ContactsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, mRootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactsAdapter(getContext(), null);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnChildClickListener(this);

        return mRootView;
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

        String phoneNumber = null;
        if (args != null && args.containsKey(ARG_PHONE_NUMBER)) {
            phoneNumber = args.getString(ARG_PHONE_NUMBER);
        }

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
        } else {
            builder = Phone.CONTENT_URI.buildUpon();
        }
        builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
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

    @Override
    public void onChildClick(String normPhoneNumber) {
        mSharedDialViewModel.setNumber(normPhoneNumber);
    }
}
