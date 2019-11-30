package com.chooloo.www.callmanager.adapter.helper;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.util.Utilities;

/**
 * A helper influenced by <a href="Medium's Article">https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf</a>
 * and also <a href="SimpleElevationItemTouchHelperCallback">https://github.com/brianwernick/RecyclerExt/blob/master/library/src/main/java/com/devbrackets/android/recyclerext/adapter/helper/SimpleElevationItemTouchHelperCallback.java</a>
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {


    private final ItemTouchHelperAdapter mAdapter;

    protected boolean isElevated = false;
    protected float originalElevation = 0;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; //For the time being
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder =
                    (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }

        updateElevation(recyclerView, viewHolder, false);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState,
                            boolean isCurrentlyActive) {

        //To avoid elevation conflicts with the Lollipop+ implementation, we will always inform the super that we aren't active
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);
        if (isCurrentlyActive && !isElevated) {
            updateElevation(recyclerView, viewHolder, true);
        }
    }

    /**
     * Updates the elevation for the specified <code>holder</code> by either increasing
     * or decreasing by the specified amount
     *
     * @param recyclerView The recyclerView to use when calculating the new elevation
     * @param holder       The ViewHolder to increase or decrease the elevation for
     * @param elevate      True if the <code>holder</code> should have it's elevation increased
     */
    protected void updateElevation(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder holder, boolean elevate) {
        if (elevate) {
            originalElevation = ViewCompat.getElevation(holder.itemView);
            float newElevation = Utilities.convertDpToPixel(recyclerView.getContext(), 4f);
            ViewCompat.setElevation(holder.itemView, newElevation);
            isElevated = true;
        } else {
            ViewCompat.setElevation(holder.itemView, originalElevation);
            originalElevation = 0;
            isElevated = false;
        }
    }

    public interface ItemTouchHelperAdapter {

        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }
}