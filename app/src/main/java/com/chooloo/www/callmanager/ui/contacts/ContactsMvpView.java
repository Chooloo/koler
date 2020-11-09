package com.chooloo.www.callmanager.ui.contacts;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.MvpView;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView;

public interface ContactsMvpView extends CursorMvpView {
    void openContact(Contact contact);

    String getHeader(int position);

    void refreshHeaders();

    void updateScroll();
}
