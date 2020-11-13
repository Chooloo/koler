package com.chooloo.www.callmanager.ui.recents;

import android.Manifest;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader;
import com.chooloo.www.callmanager.ui.base.CursorAdapter;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.PhoneNumberUtils;
import com.chooloo.www.callmanager.util.Utilities;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class RecentsFragment extends CursorFragment<RecentsAdapter> implements RecentsMvpView {

    private static final String[] REQUIRED_PERMISSIONS = {READ_CALL_LOG, WRITE_CALL_LOG};
    private static final String ARG_CONTACT_NAME = "contact_name";
    private static final String ARG_PHONE_NUMBER = "phone_number";

    private RecentsPresenter<RecentsMvpView> mPresenter;

    public static RecentsFragment newInstance() {
        RecentsFragment fragment = new RecentsFragment();
        fragment.setRequiredPermissions(REQUIRED_PERMISSIONS);
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public static RecentsFragment newInstance(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        RecentsFragment fragment = new RecentsFragment();
        fragment.setArguments(args);
        fragment.setRequiredPermissions(REQUIRED_PERMISSIONS);
        return fragment;
    }

    @Override
    public void setUp() {
        super.setUp();

        mPresenter = new RecentsPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        super.onCreateLoader(id, args);
        String contactName = args != null ? args.getString(ARG_CONTACT_NAME, null) : null;
        String phoneNumber = args != null ? args.getString(ARG_PHONE_NUMBER, null) : null;
        return new RecentsCursorLoader(mActivity, phoneNumber, contactName);
    }

    @Override
    public RecentsAdapter getAdapter() {
        RecentsAdapter recentsAdapter = new RecentsAdapter<>(mActivity);
        recentsAdapter.setOnRecentItemClickListener(new RecentsAdapter.OnRecentItemClickListener() {
            @Override
            public void onRecentItemClick(RecentCall recentCall) {
                mPresenter.onRecentItemClick(recentCall);
            }

            @Override
            public void onRecentItemLongClick(RecentCall recentCall) {
                mPresenter.onRecentItemLongClick(recentCall);
            }
        });
        return recentsAdapter;
    }

    @Override
    public void showRecentCallPopup(RecentCall recentCall) {
        Contact contact;
        if (recentCall.getCallerName() != null) {
            contact = ContactUtils.getContact(mActivity, recentCall.getCallerNumber(), null);
        } else {
            contact = new Contact(recentCall.getCallerName(), recentCall.getCallerNumber(), null);
        }

        // Initiate the dialog
        Dialog contactDialog = new Dialog(mActivity);
        contactDialog.setContentView(R.layout.contact_popup_view);

        // Views declarations
        ConstraintLayout popupLayout;
        TextView contactName, contactNumber, contactDate;
        ImageView contactPhoto, contactPhotoPlaceholder;
        ImageButton callButton, editButton, deleteButton, infoButton, addButton, smsButton, favButton;

        popupLayout = contactDialog.findViewById(R.id.contact_popup_layout);

        contactPhoto = contactDialog.findViewById(R.id.contact_popup_photo);
        contactPhotoPlaceholder = contactDialog.findViewById(R.id.contact_popup_photo_placeholder);

        contactName = contactDialog.findViewById(R.id.contact_popup_name);
        contactNumber = contactDialog.findViewById(R.id.contact_popup_number);
        contactDate = contactDialog.findViewById(R.id.contact_popup_date);

        callButton = contactDialog.findViewById(R.id.contact_popup_button_call);
        editButton = contactDialog.findViewById(R.id.contact_popup_button_edit);
        deleteButton = contactDialog.findViewById(R.id.contact_popup_button_delete);
        infoButton = contactDialog.findViewById(R.id.contact_popup_button_info);
        addButton = contactDialog.findViewById(R.id.contact_popup_button_add);
        smsButton = contactDialog.findViewById(R.id.contact_popup_button_sms);

        contactDialog.findViewById(R.id.contact_popup_button_fav).setVisibility(View.GONE);

        if (contact.getName() != null) {
            contactName.setText(contact.getName());
            contactNumber.setText(PhoneNumberUtils.formatPhoneNumber(mActivity, contact.getMainPhoneNumber()));
            infoButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            if (contact.getPhotoUri() == null || contact.getPhotoUri().isEmpty()) {
                contactPhoto.setVisibility(View.GONE);
                contactPhotoPlaceholder.setVisibility(View.VISIBLE);
            } else {
                contactPhoto.setVisibility(View.VISIBLE);
                contactPhotoPlaceholder.setVisibility(View.GONE);
                contactPhoto.setImageURI(Uri.parse(contact.getPhotoUri()));
            }
        } else {
            infoButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
            contactName.setText(PhoneNumberUtils.formatPhoneNumber(mActivity, contact.getMainPhoneNumber()));
            contactNumber.setVisibility(View.GONE);

            contactPhoto.setVisibility(View.GONE);
            contactPhotoPlaceholder.setVisibility(View.VISIBLE);
        }

        contactDate.setVisibility(View.VISIBLE);
        contactDate.setText(recentCall.getCallDateString());

        // -- Click Listeners -- //
        if (contact.getName() != null) {

            editButton.setOnClickListener(v -> {
                ContactUtils.openContactToEdit(getActivity(), contact.getContactId());
            });

            infoButton.setOnClickListener(v -> {
                ContactUtils.openContact(getActivity(), contact.getContactId());
            });

        }

        addButton.setOnClickListener(v -> {
            ContactUtils.openAddContact(getActivity(), recentCall.getCallerNumber());
        });

        smsButton.setOnClickListener(v -> {
            Utilities.openSmsWithNumber(getActivity(), recentCall.getCallerNumber());
        });

        callButton.setOnClickListener(v -> {
            CallManager.call(mActivity, contact.getMainPhoneNumber());
        });

        deleteButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_CALL_LOG) == PERMISSION_GRANTED) {
                String queryString = "_ID=" + recentCall.getCallId();
                mActivity.getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);
                load();
                Toast.makeText(mActivity, "Call log deleted", Toast.LENGTH_SHORT).show();
                contactDialog.dismiss();
            } else {
                Toast.makeText(mActivity, "I dont have the permission", Toast.LENGTH_LONG).show();
                PermissionUtils.askForPermissions(getActivity(), new String[]{WRITE_CALL_LOG});
                contactDialog.dismiss();
            }
        });

        popupLayout.setElevation(20);

        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contactDialog.show();
    }

    public void load(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        load(args);
    }
}
