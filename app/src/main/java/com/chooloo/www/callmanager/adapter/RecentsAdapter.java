package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.listener.OnItemClickListener;
import com.chooloo.www.callmanager.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.ui.ListItemHolder;
import com.chooloo.www.callmanager.util.RelativeTime;

import java.util.Date;

public class RecentsAdapter extends AbsFastScrollerAdapter<ListItemHolder> {

    // Click listeners
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * Constructor
     *
     * @param context
     * @param cursor
     * @param onItemClickListener
     * @param onItemLongClickListener
     */
    public RecentsAdapter(Context context,
                          Cursor cursor,
                          OnItemClickListener onItemClickListener,
                          OnItemLongClickListener onItemLongClickListener) {
        super(context, cursor);
        mOnItemClickListener = onItemClickListener;
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ListItemHolder holder, Cursor cursor) {

        // get the recent call
        RecentCall recentCall = new RecentCall(this.mContext, cursor);

        // get information
        String callerName = recentCall.getCallerName();
        String phoneNumber = recentCall.getCallerNumber();
        Date date = recentCall.getCallDate();

        // hide header
        holder.header.setVisibility(View.GONE);

        // append calls in a row count
        if (recentCall.getCount() > 1)
            callerName += (" (" + recentCall.getCount() + ")");

        // set name
        holder.bigText.setText(callerName != null ? callerName : phoneNumber);

        // set date
        holder.smallText.setText(RelativeTime.getTimeAgo(date.getTime()));

        // set image
        holder.photo.setVisibility(View.VISIBLE);
        holder.photoPlaceholder.setVisibility(View.GONE);

        // set call type icon
        switch (recentCall.getCallType()) {
            case RecentCall.mIncomingCall:
                holder.photo.setImageResource(R.drawable.ic_call_received_black_24dp);
                break;
            case RecentCall.mOutgoingCall:
                holder.photo.setImageResource(R.drawable.ic_call_made_black_24dp);
                break;
            case RecentCall.mMissedCall:
                holder.photo.setImageResource(R.drawable.ic_call_missed_black_24dp);
                break;
            default:
                holder.photo.setVisibility(View.GONE);
                break;
        }

        // set click listeners
        if (mOnItemClickListener != null)
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder, recentCall));

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                mOnItemLongClickListener.onItemLongClick(holder, recentCall);
                return true;
            });
        }
    }

    @Override
    public String getHeaderString(int position) {
        return null;
    }

    @Override
    public void refreshHeaders() {
    }

}
