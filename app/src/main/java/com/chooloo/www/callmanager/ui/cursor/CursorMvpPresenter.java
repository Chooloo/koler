package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;

import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface CursorMvpPresenter<V extends CursorMvpView> extends MvpPresenter<V> {
    void onRequestPermissionsResult(String[] permissions);

    void onScrolled();

    void onRefresh();

    void onLoadFinished(Loader<Cursor> loader, Cursor data);

    void onLoaderReset(Loader<Cursor> loader);

    void onEnablePermissionClick();

    void onItemClick();

    boolean onItemLongClick();
}
