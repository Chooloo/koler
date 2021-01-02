package com.chooloo.www.callmanager.ui.contacts;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface ContactsMvpPresenter<V extends ContactsMvpView> extends MvpPresenter<V> {
    void onContactItemClick(Contact contact);

    boolean onContactItemLongClick(Contact contact);
}
