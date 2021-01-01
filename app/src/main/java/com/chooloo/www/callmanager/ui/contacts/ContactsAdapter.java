package com.chooloo.www.callmanager.ui.contacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.ui.listitem.ListItemHeader;
import com.chooloo.www.callmanager.ui.listitem.ListItemPerson;

import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.EXTRA_INDEX_COUNTS;

public class ContactsAdapter extends CursorAdapter<ListItemHolder> {

    private final static String FAVORITE_HEADER = "★";

    private final static int VIEW_TYPE_CONTACT = 0;
    private final static int VIEW_TYPE_HEADER = 1;

    private int mHeadersCount;

    private OnContactItemClickListener mOnContactItemClickListener;
    private OnContactItemLongClickListener mOnContactItemLongClickListener;

    public ContactsAdapter(Context context) {
        super(context);
        mHeadersCount = 0;
        mOnContactItemLongClickListener = contact -> true;
        mOnContactItemClickListener = contact -> {
        };
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTACT) {
            return new ListItemHolder<>(new ListItemPerson(mContext));
        } else {
            return new ListItemHolder<>(new ListItemHeader(mContext));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        char currentFirstLetter = Contact.fromCursor(mCursor).getName().charAt(0);
        mCursor.moveToPrevious();
        char previousFirstLetter = Contact.fromCursor(mCursor).getName().charAt(0);
        mCursor.moveToNext();
        return currentFirstLetter == previousFirstLetter ? VIEW_TYPE_CONTACT : VIEW_TYPE_HEADER;
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_CONTACT:
                Contact contact = Contact.fromCursor(mCursor);

                ListItemPerson listItemContact = (ListItemPerson) holder.getListItem();
                listItemContact.setBigText(contact.getName());
                listItemContact.setImageUri(contact.getPhotoUri() == null ? Uri.parse(contact.getPhotoUri()) : null);
                listItemContact.setOnClickListener(view -> mOnContactItemClickListener.onContactItemClick(contact));
                listItemContact.setOnLongClickListener(view -> mOnContactItemLongClickListener.onContactItemLongClick(contact));

                mCursor.moveToNext();
                break;
            case VIEW_TYPE_HEADER:
                ListItemHeader listItemHeader = (ListItemHeader) holder.getListItem();
                if (position == 0) {
                    listItemHeader.setHeader(FAVORITE_HEADER);
                } else {
                    mCursor.moveToNext();
                    char nextLetter = Contact.fromCursor(mCursor).getName().charAt(0);
                    mCursor.moveToPrevious();
                    listItemHeader.setHeader(String.valueOf(nextLetter));
                }
        }
    }

    @Override
    public void setCursor(Cursor newCursor) {
        super.setCursor(newCursor);
        mHeadersCount = newCursor.getExtras().getIntArray(EXTRA_INDEX_COUNTS).length;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mHeadersCount;
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
