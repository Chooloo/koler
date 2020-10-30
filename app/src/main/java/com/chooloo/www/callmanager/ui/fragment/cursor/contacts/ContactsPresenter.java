package com.chooloo.www.callmanager.ui.fragment.cursor.contacts;

import android.database.Cursor;
import android.view.View;

import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.activity.contact.ContactContract;
import com.chooloo.www.callmanager.ui.fragment.cursor.CursorContract;
import com.chooloo.www.callmanager.ui.fragment.cursor.CursorPresenter;

public class ContactsPresenter<V extends ContactsContract.View> extends CursorPresenter<V> implements CursorContract.Presenter<V> {
    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int firstCompletelyVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisible == RecyclerView.NO_POSITION)
            return; // No items are visible, so there are no headers to update.
        String anchoredHeaderString = mAdapter.getHeaderString(firstCompletelyVisible);

        // If the user swipes to the top of the list very quickly, there is some strange behavior
        // between this method updating headers and adapter#onBindViewHolder updating headers.
        // To overcome this, we refresh the headers to ensure they are correct.
        if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
            mAdapter.refreshHeaders();
            mAnchoredHeader.setVisibility(View.INVISIBLE);
        } else {
            if (mAdapter.getHeaderString(firstVisibleItem).equals(anchoredHeaderString)) {
                mAnchoredHeader.setText(anchoredHeaderString);
                mAnchoredHeader.setVisibility(View.VISIBLE);
                getContactHolder(firstVisibleItem).header.setVisibility(View.INVISIBLE);
                getContactHolder(firstCompletelyVisible).header.setVisibility(View.INVISIBLE);
            } else {
                mAnchoredHeader.setVisibility(View.INVISIBLE);
                getContactHolder(firstVisibleItem).header.setVisibility(View.VISIBLE);
                getContactHolder(firstCompletelyVisible).header.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(View view) {
        super.onItemClick(view);
        mView.openContact();
    }

    @Override
    public boolean onItemLongClick(View view) {
        return super.onItemLongClick(view);
    }
}
