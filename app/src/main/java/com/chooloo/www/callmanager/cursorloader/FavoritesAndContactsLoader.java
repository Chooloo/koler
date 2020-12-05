package com.chooloo.www.callmanager.cursorloader;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends the basic ContactsCursorLoader but also adds the favourite contacts to it
 */
public class FavoritesAndContactsLoader extends ContactsCursorLoader {

    private static final String FAVORITES_SELECTION = COLUMN_STARRED + " = 1";
    public static final String FAVORITES_COUNT = "favorites_count";

    private final String phoneNumber;
    private final String contactName;

    public FavoritesAndContactsLoader(Context context, @Nullable String phoneNumber, @Nullable String contactName) {
        super(context, phoneNumber, contactName);
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
    }

    @Override
    public Cursor loadInBackground() {
        // get only contacts
        final Cursor contactsCursor = super.loadInBackground();

        // Handle favourites too
        List<Cursor> cursors = new ArrayList<>();
        final Cursor favoritesCursor = loadFavorites(this.phoneNumber, this.contactName);
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

    private Cursor loadFavorites(@Nullable String phoneNumber, @Nullable String contactName) {
        return getContext().getContentResolver().query(
                buildUri(phoneNumber, contactName),
                CONTACTS_PROJECTION,
                FAVORITES_SELECTION,
                null,
                CONTACTS_ORDER);
    }
}