package com.chooloo.www.callmanager.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter;

public class ContactsPresenter<V extends ContactsMvpView> extends CursorPresenter<V> implements ContactsMvpPresenter<V> {

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mMvpView.setupFastScroller();
    }

    @Override
    public void onRefreshHeaders() {
        mMvpView.refreshHeaders();
    }

    @Override
    public void onScrolled() {
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
