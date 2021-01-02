package com.chooloo.www.callmanager.ui.listitem;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemHolder extends RecyclerView.ViewHolder {

    private final ListItem mListItem;

    public ListItemHolder(Context context) {
        this(new ListItem(context));
    }

    public ListItemHolder(@NonNull ListItem itemView) {
        super(itemView);
        mListItem = itemView;
    }

    public ListItem getListItem() {
        return mListItem;
    }
}
