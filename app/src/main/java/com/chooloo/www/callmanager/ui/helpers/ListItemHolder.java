package com.chooloo.www.callmanager.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.listitem.ListItem;
import com.chooloo.www.callmanager.ui.listitem.ListItemPerson;

public class ListItemHolder<LI extends ListItem> extends RecyclerView.ViewHolder {

    private final LI mListItem;

    /**
     * Constructor
     *
     * @param listItem the layout view
     */
    public ListItemHolder(@NonNull LI listItem) {
        super(listItem);
        this.mListItem = listItem;
    }

    public LI getListItem() {
        return mListItem;
    }
}
