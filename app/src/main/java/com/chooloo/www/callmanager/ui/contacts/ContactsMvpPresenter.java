package com.chooloo.www.callmanager.ui.contacts;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface ContactsMvpPresenter<V extends ContactsMvpView> extends MvpPresenter<V> {
    void onRefreshHeaders();

    void onScrolled();

    void onContactItemClick(Contact contact);

    void onContactItemLongClick(Contact contact);
}
