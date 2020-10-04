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
import com.chooloo.www.callmanager.util.PhoneNumberUtils;
import com.chooloo.www.callmanager.util.RelativeTime;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import timber.log.Timber;

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

        if (callerName == null) callerName = phoneNumber;
//            callerName = PhoneNumberUtils.formatPhoneNumber(mContext, phoneNumber);

        // hide header
        holder.header.setVisibility(View.GONE);

        // append calls in a row count
        if (recentCall.getCount() > 1) callerName += (" (" + recentCall.getCount() + ")");

        // set date
        holder.bigText.setText(callerName != null ? callerName : phoneNumber);
        holder.smallText.setText(RelativeTime.getTimeAgo(date.getTime()));

        // set image
        holder.photo.setVisibility(View.VISIBLE);
        holder.photoPlaceholder.setVisibility(View.GONE);

        Map<Integer, Integer> callTypeImage = new HashMap<Integer, Integer>();
        callTypeImage.put(RecentCall.TYPE_INCOMING, R.drawable.ic_call_received_black_24dp);
        callTypeImage.put(RecentCall.TYPE_OUTGOING, R.drawable.ic_call_made_black_24dp);
        callTypeImage.put(RecentCall.TYPE_MISSED, R.drawable.ic_call_missed_black_24dp);
        callTypeImage.put(RecentCall.TYPE_REJECTED, R.drawable.ic_call_missed_outgoing_black_24dp);
        callTypeImage.put(RecentCall.TYPE_VOICEMAIL, R.drawable.ic_voicemail_black_24dp);

        try {
            holder.photo.setImageResource(callTypeImage.get(recentCall.getCallType()));
        } catch (Exception e) {
            holder.photo.setVisibility(View.GONE);
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
