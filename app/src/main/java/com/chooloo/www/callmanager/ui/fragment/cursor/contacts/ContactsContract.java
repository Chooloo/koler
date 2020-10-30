package com.chooloo.www.callmanager.ui.fragment.cursor.contacts;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.fragment.cursor.CursorContract;

public interface ContactsContract extends CursorContract {
    interface View extends CursorContract.View {
        void openContact(Contact contact);

        String getHeader(int position);

        void showAnchoredHeader(boolean isShow);

        void setAnchoredHeader(String header);

        void refreshHeaders();

        void updateFastScrollerPosition();

        int getFirstVisibleItem();

        int getFirstCompletelyVisibleItem();
    }

    interface Presenter<V extends View> extends CursorContract.Presenter<V> {
        void onContactItemClick(Contact contact);

        void onContactItemLongClick(Contact contact);
    }
}
