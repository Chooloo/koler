package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.util.Utilities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentsAdapter extends CursorRecyclerViewAdapter<RecentsAdapter.RecentCallHolder> {

    private OnChildClickListener mOnChildClickListener;

    public RecentsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @NonNull
    @Override
    public RecentCallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        return new RecentCallHolder(v);
    }

    @Override
    public void onBindViewHolder(RecentCallHolder viewHolder, Cursor cursor) {
        RecentCall recentCall = new RecentCall(this.mContext, cursor);

        String phoneNumber = recentCall.getCallerNumber();
        viewHolder.time.setText(Utilities.formatPhoneNumber(Long.toString(recentCall.getCallDate())));

        if (recentCall.getCallerName() != null) viewHolder.name.setText(recentCall.getCallerName());
        else viewHolder.name.setText(phoneNumber);

        if (recentCall.getCaller().getPhotoUri() == null) {
            viewHolder.photo.setVisibility(View.GONE);
            viewHolder.photoPlaceholder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.photo.setImageURI(Uri.parse(recentCall.getCaller().getPhotoUri()));
            viewHolder.photo.setVisibility(View.VISIBLE);
            viewHolder.photoPlaceholder.setVisibility(View.GONE);
        }

        if (mOnChildClickListener != null) {
            viewHolder.itemView.setOnClickListener(v -> mOnChildClickListener.onChildClick(phoneNumber));
            viewHolder.itemView.setOnLongClickListener(v -> mOnChildClickListener.onChildLongClick(recentCall.getCaller()));
        }
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }

    public interface OnChildClickListener {
        void onChildClick(String normPhoneNumber);

        boolean onChildLongClick(Contact contact);
    }

    class RecentCallHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
        @BindView(R.id.item_photo) ImageView photo;
        @BindView(R.id.item_name_text) TextView name;
        @BindView(R.id.item_number_text) TextView time;

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
