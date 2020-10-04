package com.chooloo.www.callmanager.cursorloader;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.chooloo.www.callmanager.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Extends the basic ContactsCursorLoader but also adds the favourite contacts to it
 */
public class FavoritesAndContactsLoader extends ContactsCursorLoader {

    public static final String FAVORITES_COUNT = "favorites_count";

    private boolean mWithFavs = true;

    /**
     * Constructor
     *
     * @param context     calling context
     * @param phoneNumber String
     * @param contactName String
     */
    public FavoritesAndContactsLoader(Context context, String phoneNumber, String contactName, boolean withFavs) {
        super(context, phoneNumber, contactName);
        Timber.i("Creating contacts loader with " + phoneNumber + " : " + contactName);
        this.mWithFavs = withFavs;
    }

    @Override
    public Cursor loadInBackground() {
        // get only contacts
        final Cursor contactsCursor = loadContacts();

        if (!mWithFavs) return contactsCursor;

        // Handle favourites too
        List<Cursor> cursors = new ArrayList<>();
        final Cursor favoritesCursor = loadFavorites();
        final int favoritesCount = favoritesCursor == null ? 0 : favoritesCursor.getCount();

        // add the cursors
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

    /**
     * Try to load the contacts, handle the exceptions
     *
     * @return The contacts cursor
     */
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

    /**
     * Load the favorite contacts
     *
     * @return The cursor containing the favorites
     */
    private Cursor loadFavorites() {
        PermissionUtils.checkPermissionsGranted(getContext(), new String[]{READ_CONTACTS}, true);
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