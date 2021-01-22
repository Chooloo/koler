package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;

import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class CursorPresenter<V extends CursorMvpView> extends BasePresenter<V> implements CursorMvpPresenter<V> {

    @Override
    public void onRefresh() {
        mMvpView.load();
    }

    @Override
    public void onNoPermissions() {
        mMvpView.askForPermissions();
        onNoResults();
    }

    @Override
    public void onResults() {
        mMvpView.showEmptyPage(false);
    }

    @Override
    public void onNoResults() {
        mMvpView.showEmptyPage(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mMvpView.setRefreshing(true);
        return mMvpView.onGetLoader(args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMvpView.setRefreshing(false);
        mMvpView.updateData(data);
        mMvpView.animateListView();
        if (mMvpView.getItemCount() > 0) {
            onResults();
        } else {
            onNoResults();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mMvpView != null) {
            mMvpView.updateData(null);
        }
    }

    @Override
    public void onEnablePermissionClick() {
        mMvpView.askForPermissions();
    }
}
