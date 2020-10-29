package com.chooloo.www.callmanager.ui.fragment.cursor;

import android.database.Cursor;
import android.os.Bundle;

import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public class CursorContract implements BaseContract {
    interface View extends BaseContract.View {
        void setUp();

        String[] getRequiredPermissions();

        boolean isPermissionGranted();

        void togglePermissionButton();

        void setData(Cursor cursor);

        void changeCursor(Cursor cursor);

        void load();

        void runLoader(Bundle args);
    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {
        void onRequestPermissionsResult(String[] permissions);

        void onScrollChange(android.view.View view, int i, int i1, int i2, int i3);

        void onRefresh();

        void onLoadFinished(Loader<Cursor> loader, Cursor data);

        void onLoaderReset(Loader<Cursor> loader);

        void onEnablePermissionClick();
    }
}
