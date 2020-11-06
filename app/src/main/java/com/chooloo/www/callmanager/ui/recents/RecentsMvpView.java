package com.chooloo.www.callmanager.ui.recents;

import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.ui.base.MvpView;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView;

public interface RecentsMvpView extends CursorMvpView {
    void showRecentCallPopup(RecentCall recentCall);
}
