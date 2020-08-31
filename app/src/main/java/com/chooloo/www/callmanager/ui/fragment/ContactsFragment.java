package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.ContactsAdapter;
import com.chooloo.www.callmanager.listener.OnItemClickListener;
import com.chooloo.www.callmanager.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.ListItemHolder;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsCursorFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;

import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ContactsFragment extends AbsCursorFragment implements
        FABCoordinator.FABDrawableCoordination,
        FABCoordinator.OnFABClickListener,
        OnItemClickListener,
        OnItemLongClickListener {

    public static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};

    public ContactsFragment(Context context) {
        super(context);
        mAdapter = new ContactsAdapter(mContext, null, this, this);
        mRequiredPermissions = REQUIRED_PERMISSIONS;
    }

    @Override
    protected void onFragmentReady() {
        if (!PermissionUtils.checkPermissionsGranted(mContext, mRequiredPermissions, false)) {
            this.mEmptyTitle.setText(R.string.empty_contact_persmission_title);
            this.mEmptyDesc.setText(R.string.empty_contact_persmission_desc);
        }
        super.onFragmentReady();
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
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String contactName = args != null && args.containsKey(ARG_CONTACT_NAME) ? args.getString(ARG_CONTACT_NAME) : null;
        String phoneNumber = args != null && args.containsKey(ARG_PHONE_NUMBER) ? args.getString(ARG_PHONE_NUMBER) : null;
        return new FavoritesAndContactsLoader(mContext, phoneNumber, contactName);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
        Contact contact = (Contact) data;
        showContactPopup(contact);
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, Object data) {
    }

    @Override
    public void onRightClick() {
        ((MainActivity) getActivity()).expandDialer(true);
    }

    @Override
    public void onLeftClick() {
        ((MainActivity) getActivity()).toggleSearchBar();
    }

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_dialpad_black_24dp,
                R.drawable.ic_search_black_24dp
        };
    }

    private ListItemHolder getContactHolder(int position) {
        return ((ListItemHolder) mRecyclerView.findViewHolderForAdapterPosition(position));
    }

    /**
     * Shows a pop up window (dialog) with the contact's information
     *
     * @param contact
     */
    private void showContactPopup(Contact contact) {

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
        favButton = contactDialog.findViewById(R.id.contact_popup_button_fav);

        if (contact.getName() != null) {
            contactName.setText(contact.getName());
            contactNumber.setText(Utilities.formatPhoneNumber(contact.getMainPhoneNumber()));
            infoButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            if (contact.getIsFavorite())
                favButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
        } else {
            infoButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
            contactName.setText(Utilities.formatPhoneNumber(contact.getMainPhoneNumber()));
            contactNumber.setVisibility(View.GONE);
        }

        if (contact.getPhotoUri() == null || contact.getPhotoUri().isEmpty()) {
            contactPhoto.setVisibility(View.GONE);
            contactPhotoPlaceholder.setVisibility(View.VISIBLE);
        } else {
            contactPhoto.setVisibility(View.VISIBLE);
            contactPhotoPlaceholder.setVisibility(View.GONE);
            contactPhoto.setImageURI(Uri.parse(contact.getPhotoUri()));
        }

        callButton.setOnClickListener(v -> {
            Timber.i("MAIN PHONE NUMBER: " + contact.getMainPhoneNumber());
            CallManager.call(getActivity(), contact.getMainPhoneNumber());
        });

        editButton.setOnClickListener(v -> {
            ContactUtils.openContactToEdit(getActivity(), contact.getContactId());
        });

        infoButton.setOnClickListener(v -> {
            ContactUtils.openContact(getActivity(), contact.getContactId());
        });

        smsButton.setOnClickListener(v -> {
            Utilities.openSmsWithNumber(getActivity(), contact.getMainPhoneNumber());
        });

        favButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                if (contact.getIsFavorite()) {
                    contact.setIsFavorite(false);
                    favButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_outline_black_24dp));
                    ContactUtils.setContactIsFavorite(getActivity(), Long.toString(contact.getContactId()), false);
                } else {
                    contact.setIsFavorite(true);
                    favButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
                    ContactUtils.setContactIsFavorite(getActivity(), Long.toString(contact.getContactId()), true);
                }
            } else {
                PermissionUtils.askForPermission(getActivity(), WRITE_CONTACTS);
                Toast.makeText(mContext, "I dont have the permission to do that :(", Toast.LENGTH_LONG).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS) == PERMISSION_GRANTED) {
                ContactUtils.deleteContact(getActivity(), contact.getContactId());
                contactDialog.dismiss();
            } else {
                Toast.makeText(mContext, "I dont have the permission", Toast.LENGTH_LONG).show();
                contactDialog.dismiss();
            }
        });

        popupLayout.setElevation(20);

        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contactDialog.show();

    }
}
