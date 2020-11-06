package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;

import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface CursorMvpPresenter<V extends CursorMvpView> extends MvpPresenter<V> {
    void onRequestPermissionsResult(String[] permissions);

    void onScrollChange(android.view.View view, int i, int i1, int i2, int i3);

    void onRefresh();

    void onLoadFinished(Loader<Cursor> loader, Cursor data);

    void onLoaderReset(Loader<Cursor> loader);

    void onEnablePermissionClick();

    void onItemClick();

    boolean onItemLongClick();
}
