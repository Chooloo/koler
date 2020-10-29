package com.chooloo.www.callmanager.ui.fragment.cursor;

import android.database.Cursor;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.PermissionUtils;

public class CursorPresenter<V extends CursorContract.View> extends BasePresenter<V> implements CursorContract.Presenter<V> {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        mView.togglePermissionButton();
        mView.load();
    }

    @Override
    public void onRequestPermissionsResult(String[] permissions) {
        mView.togglePermissionButton();
        mView.load();
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mView.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mView.changeCursor(null);
    }

    @Override
    public void onEnablePermissionClick() {
        PermissionUtils.askForPermissions((Fragment) mView, mView.getRequiredPermissions());
    }
}
