package com.chooloo.www.callmanager.ui.cursor;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Cursor mCursor;
    protected Context mContext;
    protected DataSetObserver mDataSetObserver;
    protected ArrayMap<VH, Integer> mViewHoldersMap;

    public CursorAdapter(Context context) {
        mContext = context;
        setUp();
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
        mViewHoldersMap = new ArrayMap<>();

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
        registerDataSetObserver(mDataSetObserver);
    }

    public void setCursor(Cursor newCursor) {
        if (newCursor != mCursor) {
            unregisterDataSetObserver();
            if (mCursor != null) mCursor.close();
            mCursor = newCursor;
            setUp();
        }
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (mCursor != null) {
            mCursor.registerDataSetObserver(dataSetObserver);
        }
    }

    public void unregisterDataSetObserver() {
        if (mCursor != null && mDataSetObserver != null) {
            try {
                mCursor.unregisterDataSetObserver(mDataSetObserver);
            } catch (IllegalStateException e) {
                Timber.e("CursorAdapter was not registered when trying to unregister");
            }
        }
    }
}
