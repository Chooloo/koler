package com.chooloo.www.callmanager.ui.recents;

import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView;

public interface RecentsMvpView extends CursorMvpView {
    void showRecentCallPopup(RecentCall recentCall);

    int getSize();
}
