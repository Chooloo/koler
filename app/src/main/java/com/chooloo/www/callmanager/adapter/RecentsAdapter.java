package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.listener.OnItemClickListener;
import com.chooloo.www.callmanager.adapter.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.util.RelativeTime;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class RecentsAdapter extends AbsFastScrollerAdapter<RecentsAdapter.RecentCallHolder> {

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
    public RecentCallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        return new RecentCallHolder(v);
    }

    @Override
    public void onBindViewHolder(RecentCallHolder holder, Cursor cursor) {

        holder.letterText.setVisibility(GONE);

        // Get the recent call
        RecentCall recentCall = new RecentCall(this.mContext, cursor);

        // Get information
        String callerName = recentCall.getCallerName();
        String phoneNumber = recentCall.getCallerNumber();
        Date date = recentCall.getCallDate();
        int count = recentCall.getCount();
        String stringCount = String.valueOf(count);

        if (cursor.getPosition() != 0) {
            cursor.moveToPosition(cursor.getPosition() - 1);
            // Check if there are more of the same number ahead
            RecentCall nextCall = new RecentCall(this.mContext, cursor);
            if (nextCall.getCount() > 1) {
//                holder.itemView.setVisibility(GONE);
            }
        }

        // Set date
        holder.time.setText(RelativeTime.getTimeAgo(date.getTime()));

        // Set display name (phone number / name)
        if (count > 1) {
            callerName += (" (" + stringCount + ")");
//            holder.photo.setVisibility(GONE);
        }
        if (callerName != null) {
            holder.name.setText(callerName);
        } else holder.name.setText(phoneNumber);

        // Set image
        holder.photo.setVisibility(View.VISIBLE);
        holder.photoPlaceholder.setVisibility(GONE);
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
                break;
        }

        // Set click listeners
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder, recentCall));
        }
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

    class RecentCallHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
        @BindView(R.id.item_big_text) TextView name;
        @BindView(R.id.item_small_text) TextView time;
        @BindView(R.id.item_header) TextView letterText;
        @BindView(R.id.item_recent_photo) ImageView photo;

        /**
         * Constructor
         *
         * @param itemView the layout view
         */
        public RecentCallHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            photo.setVisibility(View.VISIBLE);
        }
    }
}
