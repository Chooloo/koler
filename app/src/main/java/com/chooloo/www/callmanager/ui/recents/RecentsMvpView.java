package com.chooloo.www.callmanager.ui.recents;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView;

public interface RecentsMvpView extends CursorMvpView {
    void openRecent(RecentCall recentCall);

    int getItemCount();

    void load(@Nullable String phoneNumber, @Nullable String contactName);
}
