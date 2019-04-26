package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.listener.OnItemClickListener;
import com.chooloo.www.callmanager.adapter.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.database.entity.RecentCall;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentsAdapter extends AbsFastScrollerAdapter<RecentsAdapter.RecentCallHolder> {

    // Click listeners
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * Constructor
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
        RecentCall recentCall = new RecentCall(this.mContext, cursor);

        String phoneNumber = recentCall.getCallerNumber();

        holder.time.setText(recentCall.getCallDateString());

        // Check for name
        if (recentCall.getCallerName() != null) holder.name.setText(recentCall.getCallerName());
        else holder.name.setText(phoneNumber);

        Contact caller = recentCall.getCaller();
        if (caller != null && caller.getPhotoUri() != null) {
            holder.photo.setImageURI(Uri.parse(recentCall.getCaller().getPhotoUri()));
            holder.photo.setVisibility(View.VISIBLE);
            holder.photoPlaceholder.setVisibility(View.GONE);
        } else {
            holder.photo.setVisibility(View.GONE);
            holder.photoPlaceholder.setVisibility(View.VISIBLE);
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

    class RecentCallHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
        @BindView(R.id.item_photo) ImageView photo;
        @BindView(R.id.item_big_text) TextView name;
        @BindView(R.id.item_small_text) TextView time;

        /**
         * Constructor
         *
         * @param itemView the layout view
         */
        public RecentCallHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
