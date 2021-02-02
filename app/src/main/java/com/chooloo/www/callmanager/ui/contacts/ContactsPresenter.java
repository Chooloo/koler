package com.chooloo.www.callmanager.ui.contacts;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.list.ListPresenter;

public class ContactsPresenter<V extends ContactsMvpView> extends ListPresenter<V> implements ContactsMvpPresenter<V> {

    @Override
    public void onContactItemClick(Contact contact) {
        mvpView.openContact(contact);
    }

    @Override
    public boolean onContactItemLongClick(Contact contact) {
        return true;
    }
}
