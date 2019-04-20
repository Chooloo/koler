package com.chooloo.www.callmanager.adapter.helper;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {

    /**
     * Called when a view is selected.
     *
     * @param holder The holder of the view to drag.
     */
    void onItemSelected(RecyclerView.ViewHolder holder);

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param holder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder holder);

    /**
     * Called when a view is requesting a start of a swipe.
     *
     * @param holder The holder of the view to drag.
     */
    void onStartSwipe(RecyclerView.ViewHolder holder);
}
