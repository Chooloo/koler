package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface CursorMvpView extends MvpView {

    void setUp();

    void updateData(Cursor cursor);

    Loader<Cursor> getLoader(Bundle args);

    int getItemCount();

    void load();

    void runLoader();

    void showEmptyPage(boolean isShow);

    void setRefreshing(boolean isRefreshing);

    void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener);
}
