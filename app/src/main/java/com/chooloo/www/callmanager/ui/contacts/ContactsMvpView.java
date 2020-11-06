package com.chooloo.www.callmanager.ui.contacts;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.MvpView;

public interface ContactsMvpView extends MvpView {
    void openContact(Contact contact);

    String getHeader(int position);

    void showAnchoredHeader(boolean isShow);

    void setAnchoredHeader(String header);

    void refreshHeaders();

    void updateFastScrollerPosition();

    int getFirstVisibleItem();

    int getFirstCompletelyVisibleItem();
}
