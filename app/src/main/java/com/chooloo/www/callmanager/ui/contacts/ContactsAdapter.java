package com.chooloo.www.callmanager.ui.contacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.ui.widgets.ListItem;
import com.chooloo.www.callmanager.util.PhoneNumberUtils;

import java.util.Arrays;
import java.util.Timer;

import timber.log.Timber;

public class ContactsAdapter<VH extends ListItemHolder> extends CursorAdapter<VH> {

    private final static String FAVORITE_HEADER = "★";

    private String[] mHeaders = new String[0]; // List of contact sublist mHeaders
    private int[] mCounts = new int[0]; // Number of contacts that correspond to each mHeader in {@code mHeaders}.

    private OnContactItemClickListener mOnContactItemClickListener;
    private OnContactItemLongClickListener mOnContactItemLongClickListener;

    public ContactsAdapter(Context context) {
        super(context);
        mOnContactItemClickListener = contact -> {
        };
        mOnContactItemLongClickListener = contact -> {
        };
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (VH) new ListItemHolder(new ListItem(mContext));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, Cursor cursor) {
        ListItem listItem = holder.mListItem;
        Contact contact = new Contact(cursor);
        int position = cursor.getPosition();
        String name = contact.getName();
        String header = String.valueOf(name.charAt(0));

        listItem.setBigText(name);
        listItem.setHeaderText(header);
        listItem.showHeader(position == 0 || !(header.equals(getHeader(position - 1))));

        if (contact.getPhotoUri() != null) {
            listItem.setImageUri(Uri.parse(contact.getPhotoUri()));
        }

        listItem.setOnClickListener(view -> mOnContactItemClickListener.onContactItemClick(contact));
        listItem.setOnLongClickListener(view -> {
            mOnContactItemLongClickListener.onContactItemLongClick(contact);
            return true;
        });
    }

    @Override
    public void setCursor(Cursor newCursor) {
        super.setCursor(newCursor);

        String[] header = newCursor.getExtras().getStringArray(FavoritesAndContactsLoader.EXTRA_INDEX_TITLES);
        int[] counts = newCursor.getExtras().getIntArray(FavoritesAndContactsLoader.EXTRA_INDEX_COUNTS);
        int favoriteCount = newCursor.getExtras().getInt(FavoritesAndContactsLoader.EXTRA_FAVORITE_COUNT);

        updateHeaders(header == null ? new String[0] : header, counts == null ? new int[0] : counts, favoriteCount);
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
            holder.mListItem.showHeader(position == 0 || !getHeader(position).equals(getHeader(position - 1)));
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

    public void setOnContactItemLongClickListener(OnContactItemLongClickListener onContactItemLongClickListener) {
        mOnContactItemLongClickListener = onContactItemLongClickListener;
    }

    public interface OnContactItemClickListener {
        void onContactItemClick(Contact contact);
    }

    public interface OnContactItemLongClickListener {
        void onContactItemLongClick(Contact contact);
    }
}
