package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.google.ContactsCursorLoader;
import com.chooloo.www.callmanager.ui.fragment.ContactsFragment;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContactsAdapter extends AbsFastScrollerAdapter<ContactsAdapter.ContactHolder> {

    private final static int VIEW_TYPE_ITEM = 0;
    private final static int VIEW_TYPE_SEPERATOR = 1;

    private OnContactSelectedListener mOnContactSelectedListener;

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

        // Check for separator
        String separator = checkSeparator(cursor);

        if (separator != null) {
            viewHolder.letterText.setText(separator.toUpperCase());
            viewHolder.letterText.setVisibility(View.VISIBLE);
        } else {
            viewHolder.letterText.setVisibility(View.INVISIBLE);
        }

        // Display the contact's information
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
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        mHeaders = cursor.getExtras().getStringArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES);
        mCounts = cursor.getExtras().getIntArray(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS);
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
        if (mHeader != ContactsFragment.Header.NONE) {
            count++;
        }
        return count;
    }

    @Override
    public String getHeaderString(int position) {
        if (mHeader != ContactsFragment.Header.NONE) {
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

    /**
     * Sets the onContactSelectedListener by a given one
     *
     * @param onContactSelectedListener
     */
    public void setOnContactSelectedListener(OnContactSelectedListener onContactSelectedListener) {
        mOnContactSelectedListener = onContactSelectedListener;
    }

    /**
     * If both previous and current contact in cursor start with the same letter
     * return null (No separator needed) else return the first letter of the current contact
     *
     * @param cursor
     * @return
     */
    public String checkSeparator(Cursor cursor) {

        // Variables
        char[] nameArray;
        char[] previousNameArray;
        String name;
        String previousName;
        int nameIndex = cursor.getColumnIndex(ContactsCursorLoader.CURSOR_NAME_COLUMN);

        // Get current data in cursor
        name = cursor.getString(nameIndex);
        nameArray = name.toCharArray();

        if (cursor.getPosition() == 0) return String.valueOf(nameArray[0]);

        // Get the previous data in cursor
        cursor.moveToPrevious();
        previousName = cursor.getString(nameIndex);
        cursor.moveToNext();
        previousNameArray = previousName.toCharArray();

        if (nameArray[0] == previousNameArray[0]) return null;
        else {
            return String.valueOf(nameArray[0]);
        }

    }

    /**
     * The interface for the onContactSelectedListener
     */
    public interface OnContactSelectedListener {
        void onContactSelected(String normPhoneNumber);
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
        @BindView(R.id.item_photo) ImageView photo;
        @BindView(R.id.item_big_text) TextView name;
        @BindView(R.id.item_small_text) TextView number;
        @BindView(R.id.letter_text) TextView letterText;

        /**
         * Constructor
         *
         * @param itemView the layout view
         */
        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SeparatorHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.separator_text) TextView text;

        public SeparatorHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
