package com.chooloo.www.callmanager.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.widgets.ListItem;

public class ListItemHolder extends RecyclerView.ViewHolder {

    private ListItem mListItem;

    /**
     * Constructor
     *
     * @param listItem the layout view
     */
    public ListItemHolder(@NonNull ListItem listItem) {
        super(listItem);
        this.mListItem = listItem;
    }

    public ListItem getListItem() {
        return mListItem;
    }
}
