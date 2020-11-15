package com.chooloo.www.callmanager.ui.contacts;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.MvpView;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;

public interface ContactsMvpView extends CursorMvpView {

    void load(@Nullable String phoneNumber, @Nullable String contactName);

    String getHeader(int position);

    ListItemHolder getContactHolder(int position);

    void openContact(Contact contact);

    void refreshHeaders();

    void updateScroll();

    void setupFastScroller();
}
