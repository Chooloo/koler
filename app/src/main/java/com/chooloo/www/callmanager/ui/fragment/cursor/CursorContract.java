package com.chooloo.www.callmanager.ui.fragment.cursor;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public interface CursorContract extends BaseContract {
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

        void onItemClick();

        boolean onItemLongClick();
    }
}
