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

    public static final String EXTRA_FAVORITE_COUNT = "favorites_count";

    private static final String FAVORITES_SELECTION = COLUMN_STARRED + " = 1";

    private final String phoneNumber;
    private final String contactName;

    public FavoritesAndContactsLoader(Context context, @Nullable String phoneNumber, @Nullable String contactName) {
        super(context, phoneNumber, contactName);
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
    }

    @Override
    public Cursor loadInBackground() {
        List<Cursor> cursors = new ArrayList<>();
        final Cursor contactsCursor = super.loadInBackground();
        final Cursor favoritesCursor = loadFavorites(this.phoneNumber, this.contactName);

        cursors.add(favoritesCursor);
        cursors.add(contactsCursor);

        return new MergeCursor(cursors.toArray(new Cursor[0])) {
            @Override
            public Bundle getExtras() {
                Bundle extras = contactsCursor == null ? new Bundle() : contactsCursor.getExtras();
                extras.putInt(EXTRA_FAVORITE_COUNT, favoritesCursor.getCount());
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