package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.google.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.ui.fragment.ContactsFragment;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContactsAdapter extends AbsFastScrollerAdapter<ContactsAdapter.ContactHolder> {

    private OnContactSelectedListener mOnContactSelectedListener;

    private final ArrayMap<ContactHolder, Integer> holderMap = new ArrayMap<>();

    private @ContactsFragment.Header int mHeader = ContactsFragment.Header.NONE;
    // List of contact sublist mHeaders
    private String[] mHeaders = new String[0];
    // Number of contacts that correspond to each mHeader in {@code mHeaders}.
    private int[] mCounts = new int[0];

    /**
     * Constructor
     *
     * @param context
     * @param cursor
     */
    public ContactsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactHolder viewHolder, Cursor cursor) {

        int position = cursor.getPosition();
        holderMap.put(viewHolder, position);

        // Display the contact's information
        String header = getHeaderString(position);
        Contact contact = new Contact(cursor);
        String contactName = contact.getName();
        String contactNumber = contact.getMainPhoneNumber();
        String formattedNumber = Utilities.formatPhoneNumber(contactNumber);

        viewHolder.name.setText(contactName);
        viewHolder.number.setText(formattedNumber);

        if (contact.getPhotoUri() == null) {
            viewHolder.photo.setVisibility(View.GONE);
            viewHolder.photoPlaceholder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.photo.setVisibility(View.VISIBLE);
            viewHolder.photoPlaceholder.setVisibility(View.GONE);
            viewHolder.photo.setImageURI(Uri.parse(contact.getPhotoUri()));
        }

        if (mOnContactSelectedListener != null) {
            viewHolder.itemView.setOnClickListener(v -> mOnContactSelectedListener.onContactSelected(contactNumber));
        }

        boolean showHeader = position == 0 || !header.equals(getHeaderString(position - 1));
        viewHolder.header.setText(header);
        viewHolder.header.setVisibility(showHeader ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);

        String[] tempHeaders = cursor.getExtras().getStringArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES);
        int[] tempCounts = cursor.getExtras().getIntArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS);

        int favoritesCount = cursor.getExtras().getInt(FavoritesAndContactsLoader.FAVORITES_COUNT);

        if (favoritesCount == 0) {
            mHeaders = tempHeaders;
            mCounts = tempCounts;
        } else {
            mHeaders = new String[(tempHeaders != null ? tempHeaders.length : 0) + 1];
            mHeaders[0] = "★";
            System.arraycopy(tempHeaders, 0, mHeaders, 1, mHeaders.length - 1);

            mCounts = new int[tempCounts.length + 1];
            mCounts[0] = favoritesCount;
            System.arraycopy(tempCounts, 0, mCounts, 1, mCounts.length - 1);

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
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        // Manually insert the header if one exists.
        if (mHeader != ContactsFragment.Header.NONE) {
            count++;
        }
        return count;
    }

    @Override
    public String getHeaderString(int position) {
        int index = -1;
        int sum = 0;
        while (sum <= position) {
            if (index + 1 >= mCounts.length) {//Index is bigger than headers list size
                return "?";
            }
            sum += mCounts[++index];
        }
        return mHeaders[index];
    }

    @Override
    public void refreshHeaders() {
        for (ContactHolder holder : holderMap.keySet()) {
            int position = holderMap.get(holder);
            boolean showHeader =
                    position == 0 || !getHeaderString(position).equals(getHeaderString(position - 1));
            int visibility = showHeader ? View.VISIBLE : View.INVISIBLE;
            holder.getHeaderView().setVisibility(visibility);
        }
    }

    /**
     * Sets the onContactSelectedListener by a given one
     *
     * @param onContactSelectedListener
     */
    public void setOnContactSelectedListener(OnContactSelectedListener onContactSelectedListener) {
        mOnContactSelectedListener = onContactSelectedListener;
    }

    /**
     * The interface for the onContactSelectedListener
     */
    public interface OnContactSelectedListener {
        void onContactSelected(String normPhoneNumber);
    }

    public class ContactHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
        @BindView(R.id.item_photo) ImageView photo;
        @BindView(R.id.item_big_text) TextView name;
        @BindView(R.id.item_small_text) TextView number;
        @BindView(R.id.item_header) TextView header;

        /**
         * Constructor
         *
         * @param itemView the layout view
         */
        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public TextView getHeaderView() {
            return header;
        }
    }
}
