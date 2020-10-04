/*
 * Copyright (C) 2014 skyfish.jy@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.ContactsContract;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.database.entity.Contact;

/**
 * Created by skyfishjy on 10/31/14.
 */

public abstract class AbsCursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    /**
     * Constructor
     *
     * @param context
     * @param cursor
     */
    public AbsCursorRecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;

        try {
            mRowIdColumn = mDataValid ? mCursor.getColumnIndex(ContactsContract.Contacts._ID) : -1;
        } catch (IllegalArgumentException e) {
            mRowIdColumn = mDataValid ? mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID) : -1;
        }

        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) mCursor.registerDataSetObserver(mDataSetObserver);
    }

    /**
     * Returns the cursor
     *
     * @return
     */
    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Returns the cursors counts
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataValid && mCursor != null ? mCursor.getCount() : 0;
    }

    /**
     * Returns an item id by position
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position))
            return mCursor.getLong(mRowIdColumn);
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!mDataValid)
            throw new IllegalStateException("this should only be called when the cursor is valid");
        if (!mCursor.moveToPosition(position))
            throw new IllegalStateException("couldn't move cursor to position " + position);
        onBindViewHolder(viewHolder, mCursor);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) old.close();
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    private Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) return null; // cursor hasn't changed

        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null)
            oldCursor.unregisterDataSetObserver(mDataSetObserver);

        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) mCursor.registerDataSetObserver(mDataSetObserver);
            try {
                mRowIdColumn = newCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            } catch (IllegalArgumentException e) {
                mRowIdColumn = newCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            }
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}