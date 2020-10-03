package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.chooloo.www.callmanager.ui.activity.ContactActivity;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsCursorFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;

import java.io.Serializable;

import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ContactsFragment extends AbsCursorFragment implements
        OnItemClickListener,
        OnItemLongClickListener {

    public static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};

    public ContactsFragment(Context context) {
        super(context);
        mAdapter = new ContactsAdapter(mContext, null, this, this);
        mRequiredPermissions = REQUIRED_PERMISSIONS;
    }

    public ContactsFragment(Context context, String phoneNumber, String contactName) {
        super(context, phoneNumber, contactName);
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
        load(null, null);
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

        boolean withFavs = (contactName == null || contactName.isEmpty()) && (phoneNumber == null || phoneNumber.isEmpty());
        return new FavoritesAndContactsLoader(mContext, phoneNumber, contactName, withFavs);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
        openContactActivity((Contact) data);
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, Object data) {
    }

    private ListItemHolder getContactHolder(int position) {
        return ((ListItemHolder) mRecyclerView.findViewHolderForAdapterPosition(position));
    }

    /**
     * Open a contact activity with a given contact
     *
     * @param contact contact to pass into the contact activity
     */
    private void openContactActivity(Contact contact) {
        Intent contactLayoutIntent = new Intent(mContext, ContactActivity.class);
        contactLayoutIntent.putExtra(ContactActivity.CONTACT_INTENT_ID, contact);
        startActivity(contactLayoutIntent);
    }
}
