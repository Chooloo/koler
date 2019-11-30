package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;

import androidx.recyclerview.widget.RecyclerView;

public abstract class AbsFastScrollerAdapter<VH extends RecyclerView.ViewHolder> extends AbsCursorRecyclerViewAdapter<VH> {

    public AbsFastScrollerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public abstract String getHeaderString(int position);

    public abstract void refreshHeaders();
}
