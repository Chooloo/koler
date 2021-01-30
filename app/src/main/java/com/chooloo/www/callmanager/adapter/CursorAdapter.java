package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;

import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Cursor mCursor;
    protected Context mContext;
    protected DataSetObserver mDataSetObserver;

    public CursorAdapter(Context context) {
        mContext = context;
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
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public void setCursor(Cursor newCursor) {
        if (newCursor != mCursor) {
            if (mCursor != null) {
                unregisterDataSetObserver(mDataSetObserver);
                mCursor.close();
            }
            mCursor = newCursor;
            registerDataSetObserver(mDataSetObserver);
        }
    }

    private void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (mCursor != null) {
            mCursor.registerDataSetObserver(dataSetObserver);
        }
    }

    private void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        if (mCursor != null) {
            try {
                mCursor.unregisterDataSetObserver(dataSetObserver);
            } catch (IllegalStateException e) {
                Timber.e("CursorAdapter was not registered when trying to unregister");
            }
        }
    }
}
