package com.chooloo.www.callmanager.ui.recents;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.util.RelativeTime;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.Date;

public class RecentsAdapter<VH extends ListItemHolder> extends CursorAdapter<VH> {

    private OnRecentItemClickListener mOnRecentItemClickListener;

    public RecentsAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (VH) new ListItemHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, Cursor cursor) {
        RecentCall recentCall = new RecentCall(mContext, cursor);

        String name = recentCall.getCallerName();
        String number = recentCall.getCallerNumber();
        Date date = recentCall.getCallDate();

        holder.setBigText(name == null ? number : name + (recentCall.getCount() > 0 ? " (" + recentCall.getCount() + ")" : ""));
        holder.setSmallText(RelativeTime.getTimeAgo(date.getTime()));
        holder.showPhoto(true, Utilities.getCallTypeImage(recentCall.getCallType()));
        holder.showHeader(false);
        holder.setOnItemClickListener(new ListItemHolder.OnItemClickListener() {
            @Override
            public void onItemClickListener() {
                if (mOnRecentItemClickListener != null) {
                    mOnRecentItemClickListener.onRecentItemClick(recentCall);
                }
            }

            @Override
            public void onItemLongClickListener() {
                if (mOnRecentItemClickListener != null) {
                    mOnRecentItemClickListener.onRecentItemLongClick(recentCall);
                }
            }
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
