package com.chooloo.www.callmanager.ui.recents;

import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter;

public class RecentsPresenter<V extends RecentsMvpView> extends CursorPresenter<V> implements RecentsMvpPresenter<V> {

    @Override
    public void onRecentItemClick(RecentCall recentCall) {
        mMvpView.showRecentCallPopup(recentCall);
    }

    @Override
    public void onRecentItemLongClick(RecentCall recentCall) {
        // TODO implement
    }
}
