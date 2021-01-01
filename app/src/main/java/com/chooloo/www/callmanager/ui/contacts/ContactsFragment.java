package com.chooloo.www.callmanager.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;

import static android.Manifest.permission.READ_CONTACTS;

public class ContactsFragment extends CursorFragment<ContactsAdapter> implements ContactsMvpView {

    private static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};
    private final static String ARG_PHONE_NUMBER = "phoneNumber";
    private final static String ARG_CONTACT_NAME = "contactName";

    private ContactsPresenter<ContactsMvpView> mPresenter;

    public static ContactsFragment newInstance() {
        return ContactsFragment.newInstance(null, null);
    }

    public static ContactsFragment newInstance(@Nullable String phoneNumber, @Nullable String contactNumber) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public ContactsAdapter getAdapter() {
        ContactsAdapter contactsAdapter = new ContactsAdapter(mActivity);
        contactsAdapter.setOnContactItemClick(contact -> mPresenter.onContactItemClick(contact));
        contactsAdapter.setOnContactItemLongClickListener(contact -> mPresenter.onContactItemLongClick(contact));
        return contactsAdapter;
    }

    @Override
    public String[] getPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    public Loader<Cursor> getLoader(Bundle args) {
        String contactName = args != null ? args.getString(ARG_CONTACT_NAME, null) : null;
        String phoneNumber = args != null ? args.getString(ARG_PHONE_NUMBER, null) : null;
        return new FavoritesAndContactsLoader(mActivity, phoneNumber, contactName);
    }

    @Override
    public void setUp() {
        super.setUp();

        mPresenter = new ContactsPresenter<>();
        mPresenter.onAttach(this);

        load();
    }

    @Override
    public void load(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        setArguments(args);
        load();
    }

    @Override
    public void openContact(Contact contact) {
        // TODO implement
    }
}
