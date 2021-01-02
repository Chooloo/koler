package com.chooloo.www.callmanager.ui.recents;

import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpPresenter;

public interface RecentsMvpPresenter<V extends RecentsMvpView> extends CursorMvpPresenter<V> {
    void onRecentItemClick(RecentCall recentCall);

    boolean onRecentItemLongClick(RecentCall recentCall);
}
