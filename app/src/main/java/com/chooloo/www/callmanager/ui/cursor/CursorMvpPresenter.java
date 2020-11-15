package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;

import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface CursorMvpPresenter<V extends CursorMvpView> extends MvpPresenter<V> {
    void onRequestPermissionsResult(String[] permissions);

    void onRefresh();

    void onNoPermissions();

    void onResults();

    void onNoResults();

    Loader<Cursor> onCreateLoader(int id, Bundle args);

    void onLoadFinished(Loader<Cursor> loader, Cursor data);

    void onLoaderReset(Loader<Cursor> loader);

    void onEnablePermissionClick();
}
