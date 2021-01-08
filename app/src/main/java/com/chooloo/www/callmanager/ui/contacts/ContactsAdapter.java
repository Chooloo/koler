package com.chooloo.www.callmanager.ui.contacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.ui.listitem.ListItem;
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.EXTRA_INDEX_COUNTS;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.EXTRA_INDEX_TITLES;
import static com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader.EXTRA_FAVORITE_COUNT;

public class ContactsAdapter extends CursorAdapter<ListItemHolder> {

    private List<Integer> mHeadersCounts;
    private List<String> mHeaders;

    private OnContactItemClickListener mOnContactItemClickListener;
    private OnContactItemLongClickListener mOnContactItemLongClickListener;

    public ContactsAdapter(Context context) {
        super(context);
        mOnContactItemLongClickListener = contact -> true;
        mOnContactItemClickListener = contact -> {
        };
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder(new ListItem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        mCursor.moveToPosition(position);
        Contact contact = Contact.fromCursor(mCursor);

        ListItem listItemContact = holder.getListItem();
        listItemContact.setBigText(contact.getName());
        listItemContact.setImageUri(contact.getPhotoUri() == null ? null : Uri.parse(contact.getPhotoUri()));
        listItemContact.setHeaderText(getHeader(position));
        listItemContact.showHeader(isFirstInHeader(position));
        listItemContact.setOnClickListener(view -> mOnContactItemClickListener.onContactItemClick(contact));
        listItemContact.setOnLongClickListener(view -> mOnContactItemLongClickListener.onContactItemLongClick(contact));
    }

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        int[] headersCounts = cursor != null ? cursor.getExtras().getIntArray(EXTRA_INDEX_COUNTS) : new int[]{};
        mHeadersCounts = Arrays.stream(headersCounts).boxed().collect(Collectors.toList());

        String[] headers = cursor != null ? cursor.getExtras().getStringArray(EXTRA_INDEX_TITLES) : new String[]{};
        mHeaders = Stream.of(headers).collect(Collectors.toCollection(ArrayList::new));

        // add favorites section
        int favoritesCount = cursor != null ? cursor.getExtras().getInt(EXTRA_FAVORITE_COUNT) : 0;
        if (favoritesCount > 0) {
            mHeadersCounts.add(0, favoritesCount);
            mHeaders.add(0, "☆");
        }
    }

    private boolean isFirstInHeader(int position) {
        int total = 0;
        for (int count : mHeadersCounts) {
            if (position == total) {
                return true;
            }
            total += count;
        }
        return false;
    }

    private String getHeader(int position) {
        int total = 0;
        for (int i = 0; i < mHeadersCounts.size(); i++) {
            if (position <= total) {
                return mHeaders.get(i);
            }
            total += mHeadersCounts.get(i);
        }
        return "";
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
        boolean onContactItemLongClick(Contact contact);
    }
}
