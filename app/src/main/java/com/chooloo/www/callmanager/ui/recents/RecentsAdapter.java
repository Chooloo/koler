package com.chooloo.www.callmanager.ui.recents;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.ui.listitem.ListItem;
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.RelativeTime;
import com.chooloo.www.callmanager.util.Utilities;

public class RecentsAdapter extends CursorAdapter<ListItemHolder> {

    private OnRecentItemClickListener mOnRecentItemClickListener;
    private OnRecentItemLongClickListener mOnRecentItemLongClickListener;

    public RecentsAdapter(Context context) {
        super(context);
        mOnRecentItemClickListener = recentCall -> {
        };
        mOnRecentItemLongClickListener = recentCall -> true;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder(new ListItem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        ListItem listItem = holder.getListItem();
        RecentCall recentCall = RecentCall.fromCursor(mCursor);

        String name = ContactUtils.getContact(mContext, recentCall.getCallerNumber(), null).getName();
        String number = recentCall.getCallerNumber();
        String callDate = recentCall.getCallDate() != null ? RelativeTime.getTimeAgo(recentCall.getCallDate().getTime()) : null;
        int count = recentCall.getCount();
        int callType = recentCall.getCallType();

        listItem.setBigText(name == null ? number : name + (count > 0 ? " (" + count + ")" : ""));
        listItem.setSmallText(callDate);
        listItem.setImageDrawable(ContextCompat.getDrawable(mContext, Utilities.getCallTypeImage(callType)));

        listItem.setOnClickListener(view -> mOnRecentItemClickListener.onRecentItemClick(recentCall));
        listItem.setOnLongClickListener(view -> mOnRecentItemLongClickListener.onRecentItemLongClick(recentCall));

        mCursor.moveToNext();
    }

    public void setOnRecentItemClickListener(OnRecentItemClickListener onRecentItemClickListener) {
        mOnRecentItemClickListener = onRecentItemClickListener;
    }

    public void setOnRecentItemLongClickListener(OnRecentItemLongClickListener onRecentItemLongClickListener) {
        mOnRecentItemLongClickListener = onRecentItemLongClickListener;
    }

    public interface OnRecentItemClickListener {
        void onRecentItemClick(RecentCall recentCall);

    }

    public interface OnRecentItemLongClickListener {
        boolean onRecentItemLongClick(RecentCall recentCall);
    }

}