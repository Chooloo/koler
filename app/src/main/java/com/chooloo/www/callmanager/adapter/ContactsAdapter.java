package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.fragment.ContactsFragment.Header;
import com.chooloo.www.callmanager.util.Utilities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContactsAdapter extends CursorRecyclerViewAdapter<ContactsAdapter.ContactHolder> {

    private OnContactSelectedListener mOnContactSelectedListener;

    private @Header int mHeader = Header.NONE;
    // List of contact sublist mHeaders
    private String[] mHeaders = new String[0];
    // Number of contacts that correspond to each mHeader in {@code mHeaders}.
    private int[] mCounts = new int[0];
    // Cursor with list of contacts
    private Cursor mCursor;

    public ContactsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mCursor = cursor;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        mCursor = cursor;
        mHeaders = cursor.getExtras().getStringArray(Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES);
        mCounts = cursor.getExtras().getIntArray(Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS);
        if (mCounts != null) {
            int sum = 0;
            for (int count : mCounts) {
                sum += count;
            }
            if (sum != cursor.getCount()) {
                Timber.e("Count sum (%d) != mCursor count (%d).", sum, cursor.getCount());
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        // Manually insert the header if one exists.
        if (mHeader != Header.NONE) {
            count++;
        }
        return count;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactHolder viewHolder, Cursor cursor) {
        Contact contact = new Contact(cursor);
        viewHolder.name.setText(contact.getName());
        String phoneNumber = contact.getMainPhoneNumber();
        viewHolder.number.setText(Utilities.formatPhoneNumber(phoneNumber));

        if (contact.getPhotoUri() == null) {
            viewHolder.photo.setVisibility(View.GONE);
            viewHolder.photoPlaceholder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.photo.setImageURI(Uri.parse(contact.getPhotoUri()));
            viewHolder.photo.setVisibility(View.VISIBLE);
            viewHolder.photoPlaceholder.setVisibility(View.GONE);
        }
        if (mOnContactSelectedListener != null) {
            viewHolder.itemView.setOnClickListener(v -> mOnContactSelectedListener.onContactSelected(phoneNumber));
        }
    }

    public String getHeaderString(int position) {
        if (mHeader != Header.NONE) {
            if (position == 0) {
                return "+";
            }
            position--;
        }
        int index = -1;
        int sum = 0;
        while (sum <= position) {
            sum += mCounts[++index];
        }
        return mHeaders[index];
    }

    public void setOnContactSelectedListener(OnContactSelectedListener onContactSelectedListener) {
        mOnContactSelectedListener = onContactSelectedListener;
    }

    public interface OnContactSelectedListener {
        void onContactSelected(String normPhoneNumber);
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
        @BindView(R.id.item_photo) ImageView photo;
        @BindView(R.id.item_name_text) TextView name;
        @BindView(R.id.item_number_text) TextView number;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
