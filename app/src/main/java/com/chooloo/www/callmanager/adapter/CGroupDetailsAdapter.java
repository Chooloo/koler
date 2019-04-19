package com.chooloo.www.callmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.helper.ItemTouchHelperViewHolder;
import com.chooloo.www.callmanager.adapter.helper.ItemTouchHelperListener;
import com.chooloo.www.callmanager.adapter.helper.SimpleItemTouchHelperCallback;
import com.chooloo.www.callmanager.database.entity.Contact;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CGroupDetailsAdapter extends RecyclerView.Adapter<CGroupDetailsAdapter.ContactHolder>
        implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {

    private Context mContext;
    private List<Contact> mData;

    private boolean mEditModeEnabled = false;

    private ItemTouchHelperListener mItemTouchHelperListener;

    public CGroupDetailsAdapter(Context context, ItemTouchHelperListener itemTouchHelperListener) {
        mContext = context;
        mItemTouchHelperListener = itemTouchHelperListener;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_editable, parent, false);
        ContactHolder holder = new ContactHolder(view);
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = mData.get(position);

        holder.name.setText(contact.getName());
        holder.number.setText(contact.getMainPhoneNumber());

        holder.dragHandle.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() ==
                    MotionEvent.ACTION_DOWN) {
                mItemTouchHelperListener.onStartDrag(holder);
            }
            return false;
        });

        holder.removeItem.setOnClickListener(v -> onItemDismiss(position));

        int visibility;
        if (mEditModeEnabled) visibility = View.VISIBLE;
        else visibility = View.GONE;
        holder.dragHandle.setVisibility(visibility);
        holder.removeItem.setVisibility(visibility);
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position)
    {
        notifyItemRemoved(position);
    }

    public void setData(List<Contact> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void enableEditMode(boolean enable) {
        if (mEditModeEnabled == enable) return;
        mEditModeEnabled = enable;
        notifyDataSetChanged();
    }

    class ContactHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.item_photo) ImageView image;
        @BindView(R.id.item_name_text) TextView name;
        @BindView(R.id.item_number_text) TextView number;

        @BindView(R.id.drag_handle) ImageView dragHandle;
        @BindView(R.id.item_remove) ImageView removeItem;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            enableEditMode(true);
            if (mItemTouchHelperListener != null) mItemTouchHelperListener.onItemSelected(this);
        }

        @Override
        public void onItemClear() {
            itemView.setElevation(0f);
        }
    }
}
