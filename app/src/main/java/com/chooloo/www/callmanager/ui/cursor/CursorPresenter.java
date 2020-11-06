package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.PermissionUtils;

public class CursorPresenter<V extends CursorMvpView> extends BasePresenter<V> implements CursorMvpPresenter<V> {
    @Override
    public void onRequestPermissionsResult(String[] permissions) {
        mMvpView.togglePermissionButton();
        mMvpView.load();
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMvpView.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMvpView.changeCursor(null);
    }

    @Override
    public void onEnablePermissionClick() {
        PermissionUtils.askForPermissions((Fragment) mMvpView, mMvpView.getRequiredPermissions());
    }

    @Override
    public void onItemClick() {

    }

    @Override
    public boolean onItemLongClick() {
        return false;
    }
}
