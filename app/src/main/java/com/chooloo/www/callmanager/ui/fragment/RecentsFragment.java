package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
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
import com.chooloo.www.callmanager.adapter.RecentsAdapter;
import com.chooloo.www.callmanager.listener.OnItemClickListener;
import com.chooloo.www.callmanager.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsCursorFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.PhoneNumberUtils;
import com.chooloo.www.callmanager.util.Utilities;

import timber.log.Timber;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class RecentsFragment extends AbsCursorFragment implements
        OnItemClickListener,
        OnItemLongClickListener {

    public static final String[] REQUIRED_PERMISSIONS = {READ_CALL_LOG, WRITE_CALL_LOG};

    private String mPhoneNumber = null;
    private String mContactName = null;

    public RecentsFragment(Context context) {
        super(context);
        mAdapter = new RecentsAdapter(mContext, null, this, this);
        mRequiredPermissions = REQUIRED_PERMISSIONS;
    }

    public RecentsFragment(Context context, String phoneNumber, String contactName) {
        super(context, phoneNumber, contactName);
        mAdapter = new RecentsAdapter(mContext, null, this, this);
        mRequiredPermissions = REQUIRED_PERMISSIONS;
    }

    @Override
    protected void onFragmentReady() {
        if (!PermissionUtils.checkPermissionsGranted(mContext, mRequiredPermissions, false)) {
            this.mEmptyTitle.setText(getString(R.string.empty_recents_persmission_title));
            this.mEmptyDesc.setText(getString(R.string.empty_recents_persmission_desc));
        }
        super.onFragmentReady();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String contactName = args != null && args.containsKey(ARG_CONTACT_NAME) ? args.getString(ARG_CONTACT_NAME) : null;
        String phoneNumber = args != null && args.containsKey(ARG_PHONE_NUMBER) ? args.getString(ARG_PHONE_NUMBER) : null;

        return new RecentsCursorLoader(getContext(), phoneNumber, contactName);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
        RecentCall recentCall = (RecentCall) data;
        showRecentPopup(recentCall);
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, Object data) {
    }

    /**
     * Shows a pop up window (dialog) with the contact's information
     *
     * @param recentCall
     */
    private void showRecentPopup(RecentCall recentCall) {

        Contact contact;
        if (recentCall.getCallerName() != null)
            contact = ContactUtils.lookupContact(mContext, recentCall.getCallerNumber());
        else contact = new Contact(recentCall.getCallerName(), recentCall.getCallerNumber(), null);

        // Initiate the dialog
        Dialog contactDialog = new Dialog(mContext);
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
            contactNumber.setText(PhoneNumberUtils.formatPhoneNumber(mContext, contact.getMainPhoneNumber()));
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
            contactName.setText(PhoneNumberUtils.formatPhoneNumber(mContext, contact.getMainPhoneNumber()));
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
            CallManager.call(getActivity(), contact.getMainPhoneNumber());
        });

        deleteButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALL_LOG) == PERMISSION_GRANTED) {
                String queryString = "_ID=" + recentCall.getCallId();
                getActivity().getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);
                load(null, null);
                Toast.makeText(mContext, "Call log deleted", Toast.LENGTH_SHORT).show();
                contactDialog.dismiss();
            } else {
                Toast.makeText(mContext, "I dont have the permission", Toast.LENGTH_LONG).show();
                PermissionUtils.askForPermissions(getActivity(), new String[]{WRITE_CALL_LOG});
                contactDialog.dismiss();
            }
        });

        popupLayout.setElevation(20);

        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contactDialog.show();

    }
}
