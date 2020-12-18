package com.chooloo.www.callmanager.ui.recents;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.ui.widgets.ListItem;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.RelativeTime;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.Date;

import timber.log.Timber;

public class RecentsAdapter<VH extends ListItemHolder> extends CursorAdapter<VH> {

    private OnRecentItemClickListener mOnRecentItemClickListener;
    private OnRecentItemLongClickListener mOnRecentItemLongClickListener;

    public RecentsAdapter(Context context) {
        super(context);
        mOnRecentItemClickListener = recentCall -> {
        };
        mOnRecentItemLongClickListener = recentCall -> {
        };
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (VH) new ListItemHolder(new ListItem(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, Cursor cursor) {
        ListItem listItem = holder.getListItem();
        RecentCall recentCall = new RecentCall(mContext, cursor);

        String name = ContactUtils.getContact(mContext, recentCall.getCallerNumber(), null).getName();
        String number = recentCall.getCallerNumber();
        Date date = recentCall.getCallDate();
        Timber.d("Recent call with details: number | %s, date | %s", recentCall.getCallerNumber(), recentCall.getCallDate());
        listItem.setBigText(name == null ? number : name + (recentCall.getCount() > 0 ? " (" + recentCall.getCount() + ")" : ""));
        listItem.setSmallText(RelativeTime.getTimeAgo(date.getTime()));
        listItem.setImageDrawable(ContextCompat.getDrawable(mContext, Utilities.getCallTypeImage(recentCall.getCallType())));
        listItem.showHeader(false);

        listItem.setOnClickListener(view -> mOnRecentItemClickListener.onRecentItemClick(recentCall));
        listItem.setOnLongClickListener(view -> {
            mOnRecentItemLongClickListener.onRecentItemLongClick(recentCall);
            return true;
        });
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
        void onRecentItemLongClick(RecentCall recentCall);
    }

}
