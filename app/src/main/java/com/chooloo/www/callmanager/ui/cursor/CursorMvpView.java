package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.os.Bundle;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface CursorMvpView extends MvpView {
    void setUp();

    void togglePermissionButton();

    void setData(Cursor cursor);

    void changeCursor(Cursor cursor);

    void load();

    void runLoader(Bundle args);
}
