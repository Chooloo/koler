package com.chooloo.www.callmanager.ui.contacts;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter;

public class ContactsPresenter<V extends ContactsMvpView> extends CursorPresenter<V> implements ContactsMvpPresenter<V> {
    @Override
    public void onRefreshHeaders() {
        mMvpView.refreshHeaders();
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        mMvpView.updateScroll();
    }

    @Override
    public void onContactItemClick(Contact contact) {
        mMvpView.openContact(contact);
    }

    @Override
    public void onContactItemLongClick(Contact contact) {
    }

}
