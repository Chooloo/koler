package com.chooloo.www.callmanager.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.contact.ContactBottomDialogFragment;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;

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
    public ContactsAdapter onGetAdapter() {
        ContactsAdapter contactsAdapter = new ContactsAdapter(activity);
        contactsAdapter.setOnContactItemClick(contact -> mPresenter.onContactItemClick(contact));
        contactsAdapter.setOnContactItemLongClickListener(contact -> mPresenter.onContactItemLongClick(contact));
        return contactsAdapter;
    }

    @Override
    public String[] onGetPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    public Loader<Cursor> onGetLoader(Bundle args) {
        String contactName = args != null ? args.getString(ARG_CONTACT_NAME, null) : null;
        String phoneNumber = args != null ? args.getString(ARG_PHONE_NUMBER, null) : null;
        return new FavoritesAndContactsLoader(activity, phoneNumber, contactName);
    }

    @Override
    public void onSetup() {
        super.onSetup();

        mLoaderId = 0;

        mPresenter = new ContactsPresenter<>();
        mPresenter.attach(this);

        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detach();
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
        ContactBottomDialogFragment.newInstance(contact).show(activity.getSupportFragmentManager(), ContactBottomDialogFragment.TAG);
    }
}
