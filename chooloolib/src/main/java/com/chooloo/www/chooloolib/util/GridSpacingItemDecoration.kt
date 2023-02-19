package com.chooloo.www.chooloolib.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view); // item position
        val column = position % spanCount; // item column

        outRect.left =
            if (includeEdge) spacing - column * spacing / spanCount else column * spacing / spanCount
        outRect.right =
            if (includeEdge) (column + 1) * spacing / spanCount else spacing - (column + 1) * spacing / spanCount

        if (includeEdge) {
            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}