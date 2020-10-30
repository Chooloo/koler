package com.chooloo.www.callmanager.ui.fragment.cursor.contacts;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.fragment.cursor.CursorContract;

public interface ContactsContract extends CursorContract {
    interface View extends CursorContract.View {
        void openContact(Contact contact);
    }

    interface Presenter<V extends View> extends CursorContract.Presenter<V> {

    }
}
