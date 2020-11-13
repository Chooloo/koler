package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface CursorMvpView extends MvpView {
    void setUp();

    void togglePermissionButton();

    void setData(Cursor cursor);

    void load();

    void load(Bundle args);

    void runLoader();

    int getSize();

    void showEmptyPage(boolean isShow);

    void setRefreshing(boolean isRefreshing);

    void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener);
}
