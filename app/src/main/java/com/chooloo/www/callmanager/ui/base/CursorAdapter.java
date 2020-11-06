package com.chooloo.www.callmanager.ui.base;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.ArrayMap;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Cursor mCursor;
    protected Context mContext;
    protected DataSetObserver mDataSetObserver;
    protected final ArrayMap<VH, Integer> mViewHoldersMap;

    public CursorAdapter(Context context) {
        mContext = context;
        mViewHoldersMap = new ArrayMap<>();
        setUp();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (mCursor == null) {
            throw new IllegalStateException("This should only be called after the cursor is valid");
        } else if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Couldn't move cursor to position" + position);
        }
        mViewHoldersMap.put(holder, position);
        onBindViewHolder(holder, mCursor);
    }

    @Override
    public long getItemId(int position) {
        return mCursor != null && mCursor.moveToPosition(position) ? mCursor.getLong(getIdColumn()) : 0;
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    protected void setUp() {
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                notifyDataSetChanged();
            }
        };
        registerDataSetObserver();
    }

    public void setCursor(Cursor newCursor) {
        if (newCursor == mCursor) return;
        unregisterDataSetObserver();
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        setUp();
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void setDataSetObserver(DataSetObserver dataSetObserver) {
        mDataSetObserver = dataSetObserver;
    }

    public void registerDataSetObserver() {
        if (mCursor != null) mCursor.registerDataSetObserver(mDataSetObserver);
    }

    public void unregisterDataSetObserver() {
        if (mCursor != null && mDataSetObserver != null) {
            mCursor.unregisterDataSetObserver(mDataSetObserver);
        }
    }

    public int getIdColumn() {
        return -1;
    }

    public void onBindViewHolder(@NonNull VH viewHolder, Cursor cursor) {
    }
}
