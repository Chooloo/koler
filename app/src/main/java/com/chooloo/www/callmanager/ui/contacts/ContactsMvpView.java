package com.chooloo.www.callmanager.ui.contacts;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView;

public interface ContactsMvpView extends CursorMvpView {

    void load(@Nullable String phoneNumber, @Nullable String contactName);

    void openContact(Contact contact);
}
