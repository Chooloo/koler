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
import com.chooloo.www.callmanager.util.RelativeTime;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.Date;

import timber.log.Timber;

public class RecentsAdapter<VH extends ListItemHolder> extends CursorAdapter<VH> {

    private OnRecentItemClickListener mOnRecentItemClickListener;

    public RecentsAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (VH) new ListItemHolder(new ListItem(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, Cursor cursor) {
        ListItem listItem = holder.mListItem;
        RecentCall recentCall = new RecentCall(mContext, cursor);

        String name = recentCall.getCallerName();
        String number = recentCall.getCallerNumber();
        Date date = recentCall.getCallDate();

        listItem.setBigText(name == null ? number : name + (recentCall.getCount() > 0 ? " (" + recentCall.getCount() + ")" : ""));
        listItem.setSmallText(RelativeTime.getTimeAgo(date.getTime()));
        listItem.setImageDrawable(ContextCompat.getDrawable(mContext, Utilities.getCallTypeImage(recentCall.getCallType())));
        listItem.showHeader(false);
        listItem.setOnClickListener(view -> {
            if (mOnRecentItemClickListener != null) {
                mOnRecentItemClickListener.onRecentItemClick(recentCall);
            }
        });
        listItem.setOnLongClickListener(view -> {
            if (mOnRecentItemClickListener != null) {
                mOnRecentItemClickListener.onRecentItemLongClick(recentCall);
            }
            return true;
        });
    }

    @Override
    public int getIdColumn() {
        return mCursor != null ? mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID) : 1;
    }

    public void setOnRecentItemClickListener(OnRecentItemClickListener onRecentItemClickListener) {
        mOnRecentItemClickListener = onRecentItemClickListener;
    }

    public interface OnRecentItemClickListener {
        void onRecentItemClick(RecentCall recentCall);

        void onRecentItemLongClick(RecentCall recentCall);
    }

}
