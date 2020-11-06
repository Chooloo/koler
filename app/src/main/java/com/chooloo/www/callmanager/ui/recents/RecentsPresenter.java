package com.chooloo.www.callmanager.ui.recents;

import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.ui.base.MvpPresenter;
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter;

public class RecentsPresenter<V extends RecentsMvpView> extends CursorPresenter<V> implements RecentsMvpPresenter<V> {
    @Override
    public void onRecentItemClick(RecentCall recentCall) {
        mMvpView.showRecentCallPopup(recentCall);
    }

    @Override
    public void onRecentItemLongClick(RecentCall recentCall) {
        // TODO add functionality
    }
}
