package com.chooloo.www.callmanager.ui.contacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.CursorAdapter;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui2.ListItemHolder;
import com.chooloo.www.callmanager.util.PhoneNumberUtils;

public class ContactsAdapter<VH extends ListItemHolder> extends CursorAdapter<VH> {

    private final static String FAVORITE_HEADER = "★";

    private String[] mHeaders = new String[0]; // List of contact sublist mHeaders
    private int[] mCounts = new int[0]; // Number of contacts that correspond to each mHeader in {@code mHeaders}.

    private OnContactItemClickListener mOnContactItemClickListener;

    public ContactsAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (VH) new ListItemHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, Cursor cursor) {
        Contact contact = new Contact(cursor);
        int position = cursor.getPosition();

        String name = contact.getName();
        String number = PhoneNumberUtils.formatPhoneNumber(mContext, contact.getMainPhoneNumber());
        String header = String.valueOf(name.charAt(0));
        boolean isShowHeader = position == 0 || !(header.equals(getHeader(position - 1)));
        boolean isPhotoExist = contact.getPhotoUri() != null;

        viewHolder.setBigText(name);
        viewHolder.setSmallText(number);
        viewHolder.setHeader(header);
        viewHolder.showHeader(isShowHeader);
        viewHolder.showPhoto(isPhotoExist, isPhotoExist ? Uri.parse(contact.getPhotoUri()) : null);
        viewHolder.setOnItemClickListener(new ListItemHolder.OnItemClickListener() {
            @Override
            public void onItemClickListener() {
                if (mOnContactItemClickListener != null) {
                    mOnContactItemClickListener.onContactItemClick(contact);
                }
            }

            @Override
            public void onItemLongClickListener() {
                if (mOnContactItemClickListener != null) {
                    mOnContactItemClickListener.onContactItemLongClick(contact);
                }
            }
        });
    }

    @Override
    public void setCursor(Cursor newCursor) {
        super.setCursor(newCursor);

        String[] header = newCursor.getExtras().getStringArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES);
        int[] counts = newCursor.getExtras().getIntArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS);
        int favoriteCount = newCursor.getExtras().getInt(FavoritesAndContactsLoader.FAVORITES_COUNT);

        header = header == null ? new String[0] : header;
        counts = counts == null ? new int[0] : counts;

        updateHeaders(header, counts, favoriteCount);
    }

    @Override
    public int getIdColumn() {
        return mCursor != null ? mCursor.getColumnIndex(ContactsContract.Contacts._ID) : 1;
    }

    private void updateHeaders(String[] headers, int[] counts, int favoriteCount) {
        if (favoriteCount == 0) {
            mHeaders = headers;
            mCounts = counts;
        } else {
            mHeaders = new String[(headers.length) + 1];
            mHeaders[0] = FAVORITE_HEADER;
            System.arraycopy(headers, 0, mHeaders, 1, mHeaders.length - 1);

            mCounts = new int[counts.length + 1];
            mCounts[0] = favoriteCount;
            System.arraycopy(counts, 0, mCounts, 1, mCounts.length - 1);
        }
    }

    public void refreshHeaders() {
        for (ListItemHolder holder : mViewHoldersMap.keySet()) {
            int position = mViewHoldersMap.get(holder);
            holder.showHeader(position == 0 || !getHeader(position).equals(getHeader(position - 1)));
        }
    }

    public String getHeader(int position) {
        int index = -1;
        int sum = 0;
        while (sum <= position) {
            if (index + 1 >= mCounts.length) return "?"; // index is bigger than headers list size
            sum += mCounts[++index];
        }
        return mHeaders[index];
    }

    public void setOnContactItemClick(OnContactItemClickListener onContactItemClick) {
        mOnContactItemClickListener = onContactItemClick;
    }

    public interface OnContactItemClickListener {
        void onContactItemClick(Contact contact);

        void onContactItemLongClick(Contact contact);
    }
}
