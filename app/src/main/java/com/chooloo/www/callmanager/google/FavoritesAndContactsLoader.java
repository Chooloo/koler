package com.chooloo.www.callmanager.google;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.internal.Utils;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A loader for use in the default contact list, which will also query for favorite contacts
 * if configured to do so.
 */
public class FavoritesAndContactsLoader extends ContactsCursorLoader {

    public static final String FAVORITES_COUNT = "favorites_count";

    /**
     * Constructor
     *
     * @param context
     * @param phoneNumber String
     * @param contactName String
     */
    public FavoritesAndContactsLoader(Context context, String phoneNumber, String contactName) {
        super(context, phoneNumber, contactName);
    }

    @Override
    public Cursor loadInBackground() {
        List<Cursor> cursors = new ArrayList<>();
        // get cursors
        final Cursor favoritesCursor = loadFavoritesContacts();
        final Cursor contactsCursor = loadContacts();
        // set favorites count
        final int favoritesCount = favoritesCursor == null ? 0 : favoritesCursor.getCount();
        // add cursors to array
        cursors.add(favoritesCursor);
        cursors.add(contactsCursor);
        // merge cursors
        return new MergeCursor(cursors.toArray(new Cursor[0])) {
            @Override
            public Bundle getExtras() {
                // Need to get the extras from the contacts cursor.
                Bundle extras = contactsCursor == null ? new Bundle() : contactsCursor.getExtras();
                extras.putInt(FAVORITES_COUNT, favoritesCount);
                return extras;
            }
        };
    }

    private Cursor loadContacts() {
        // ContactsCursor.loadInBackground() can return null; MergeCursor
        // correctly handles null cursors.
        try {
            return super.loadInBackground();
        } catch (NullPointerException | SQLiteException | SecurityException e) {
            // Ignore NPEs, SQLiteExceptions and SecurityExceptions thrown by providers
            return null;
        }
    }

    private Cursor loadFavoritesContacts() {
        Utilities.checkPermissionGranted(getContext(), READ_CONTACTS, true);
        String selection = ContactsCursorLoader.COLUMN_STARRED + " = 1";
        return getContext().getContentResolver().query(
                buildFavoritesUri(),
                getProjection(),
                selection,
                null,
                getSortOrder());
    }

    /**
     * Builds contact uri by given name and phone number
     *
     * @return Builder.build()
     */
    private static Uri buildFavoritesUri() {
        Uri.Builder builder = Phone.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");
        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        return builder.build();
    }
}