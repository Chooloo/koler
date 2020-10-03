package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.listener.OnItemClickListener;
import com.chooloo.www.callmanager.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.ui.ListItemHolder;
import com.chooloo.www.callmanager.util.PhoneNumberUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import timber.log.Timber;

public class ContactsAdapter extends AbsFastScrollerAdapter<ListItemHolder> {

    // Click listeners
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnContactSelectedListener mOnContactSelectedListener;

    private final ArrayMap<ListItemHolder, Integer> holderMap = new ArrayMap<>();

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
    public ContactsAdapter(Context context,
                           Cursor cursor,
                           OnItemClickListener onItemClickListener,
                           OnItemLongClickListener onItemLongClickListener) {
        super(context, cursor);
        mOnItemClickListener = onItemClickListener;
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ListItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemHolder viewHolder, Cursor cursor) {

        // get the contact from the cursor
        Contact contact = new Contact(cursor);

        // some settings
        int position = cursor.getPosition();
        String header = getHeaderString(position);
        holderMap.put(viewHolder, position);

        // set texts
        viewHolder.bigText.setText(contact.getName());
        viewHolder.smallText.setText(PhoneNumberUtils.formatPhoneNumber(mContext, contact.getMainPhoneNumber()));

        // set header
        boolean showHeader = position == 0 || !header.equals(getHeaderString(position - 1));
        viewHolder.header.setText(header);
        viewHolder.header.setVisibility(showHeader ? View.VISIBLE : View.INVISIBLE);

        // set photo
        if (contact.getPhotoUri() == null) {
            viewHolder.photo.setVisibility(View.GONE);
            viewHolder.photoPlaceholder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.photo.setVisibility(View.VISIBLE);
            viewHolder.photoPlaceholder.setVisibility(View.GONE);
            viewHolder.photo.setImageURI(Uri.parse(contact.getPhotoUri()));
        }

        // Set click listeners
        if (mOnContactSelectedListener != null)
            viewHolder.itemView.setOnClickListener(v -> mOnContactSelectedListener.onContactSelected(contact.getMainPhoneNumber()));

        if (mOnItemClickListener != null)
            viewHolder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(viewHolder, contact));

        if (mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(v -> {
                mOnItemLongClickListener.onItemLongClick(viewHolder, contact);
                return true;
            });
        }

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
                for (int count : mCounts) sum += count;
                if (sum != cursor.getCount())
                    Timber.e("Count sum (%d) != mCursor count (%d).", sum, cursor.getCount());
            }
        }
    }

    @Override
    public String getHeaderString(int position) {
        int index = -1;
        int sum = 0;
        while (sum <= position) {
            if (index + 1 >= mCounts.length) return "?"; // index is bigger than headers list size
            sum += mCounts[++index];
        }
        return mHeaders[index];
    }

    @Override
    public void refreshHeaders() {
        for (ListItemHolder holder : holderMap.keySet()) {
            int position = holderMap.get(holder);
            boolean showHeader =
                    position == 0 || !getHeaderString(position).equals(getHeaderString(position - 1));
            int visibility = showHeader ? View.VISIBLE : View.INVISIBLE;
            holder.header.setVisibility(visibility);
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
}
