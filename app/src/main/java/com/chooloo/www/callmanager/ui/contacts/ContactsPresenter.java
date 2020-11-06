package com.chooloo.www.callmanager.ui.contacts;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter;

public class ContactsPresenter<V extends ContactsContract.View> extends CursorPresenter<V> implements ContactsContract.Presenter<V> {
    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        mView.updateFastScrollerPosition();
        int firstVisibleItem = mView.getFirstVisibleItem();
        int firstCompletelyVisible = mView.getFirstCompletelyVisibleItem();
        if (firstCompletelyVisible == RecyclerView.NO_POSITION)
            return; // No items are visible, so there are no headers to update.
        String anchoredHeaderString = mView.getHeader(firstCompletelyVisible);

        // If the user swipes to the top of the list very quickly, there is some strange behavior
        // between this method updating headers and adapter#onBindViewHolder updating headers.
        // To overcome this, we refresh the headers to ensure they are correct.
        if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
            mView.refreshHeaders();
            mView.showAnchoredHeader(false);
        } else {
            if (mView.getHeader(firstVisibleItem).equals(anchoredHeaderString)) {
                mView.setAnchoredHeader(anchoredHeaderString);
                mView.showAnchoredHeader(true);
                getContactHolder(firstVisibleItem).header.setVisibility(View.INVISIBLE);
                getContactHolder(firstCompletelyVisible).header.setVisibility(View.INVISIBLE);
            } else {
                mView.showAnchoredHeader(false);
                getContactHolder(firstVisibleItem).header.setVisibility(View.VISIBLE);
                getContactHolder(firstCompletelyVisible).header.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onContactItemClick(Contact contact) {
        mView.openContact(contact);
    }

    @Override
    public void onContactItemLongClick(Contact contact) {

    }
}
