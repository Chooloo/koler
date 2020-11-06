package com.chooloo.www.callmanager.ui.recents;

import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpPresenter;

public interface RecentsMvpPresenter<V extends RecentsMvpView> extends CursorMvpPresenter<V> {
    void onRecentItemClick(RecentCall recentCall);

    void onRecentItemLongClick(RecentCall recentCall);
}
