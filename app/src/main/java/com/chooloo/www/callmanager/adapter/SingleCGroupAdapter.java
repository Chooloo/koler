package com.chooloo.www.callmanager.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.helper.ItemTouchHelperListener;
import com.chooloo.www.callmanager.adapter.helper.ItemTouchHelperViewHolder;
import com.chooloo.www.callmanager.adapter.helper.SimpleItemTouchHelperCallback;
import com.chooloo.www.callmanager.database.AppDatabase;
import com.chooloo.www.callmanager.database.DataRepository;
import com.chooloo.www.callmanager.database.entity.Contact;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleCGroupAdapter extends RecyclerView.Adapter<SingleCGroupAdapter.ContactHolder>
        implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {

    private AppCompatActivity mContext;
    private DataRepository mRepository;

    private List<Contact> mData;

    private boolean mEditModeEnabled = false;

    private RecyclerView mRecyclerView;
    private ItemTouchHelperListener mItemTouchHelperListener;

    public SingleCGroupAdapter(AppCompatActivity context, RecyclerView recyclerView, ItemTouchHelperListener itemTouchHelperListener) {
        mContext = context;
        mRecyclerView = recyclerView;
        mItemTouchHelperListener = itemTouchHelperListener;

        mRepository = DataRepository.getInstance(AppDatabase.getDatabase(mContext));
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_editable, parent, false);
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

        holder.removeItem.setOnClickListener(v -> onItemDismiss(holder.getAdapterPosition()));

        ConstraintLayout itemRoot = (ConstraintLayout) holder.itemView;
        ConstraintSet set = new ConstraintSet();
        int layoutId = mEditModeEnabled ? R.layout.list_item_editable_modified : R.layout.list_item_editable;
        set.load(mContext, layoutId);
        set.applyTo(itemRoot);
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        //Switch in database
        Contact firstContact = mData.get(fromPosition);
        Contact secondContact = mData.get(toPosition);
        long temp = firstContact.getContactId();
        firstContact.setContactId(secondContact.getContactId());
        secondContact.setContactId(temp);
        mRepository.update(firstContact, secondContact);

        //Switch in list - has to be this way for smooth dragging
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //Remove in database
        long id = mData.get(position).getContactId();

        //Remove in list
        mData.remove(position);

        mRepository.deleteContact(id);

        notifyItemRemoved(position);
    }

    /**
     * Sets the data by a given Contact List
     *
     * @param data
     */
    public void setData(List<Contact> data) {
        if (mData != null) return;
        mData = data;
        notifyDataSetChanged();
    }

    /**
     * Ummm... Enables edit mode
     *
     * @param enable true/false
     */
    public void enableEditMode(boolean enable) {
        if (mEditModeEnabled == enable) return;
        mEditModeEnabled = enable;

        //Animate all the RecyclerView items:
        for (int i = 0; i < getItemCount(); i++) {
            ContactHolder holder = (ContactHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null) holder.animate();
        }
    }

    public class ContactHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.item_photo) ImageView image;
        @BindView(R.id.item_big_text) TextView name;
        @BindView(R.id.item_small_text) TextView number;

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
        }

        /**
         * Animates the ContactHolder
         */
        public void animate() {
            ConstraintLayout itemRoot = (ConstraintLayout) itemView;
            ConstraintSet set = new ConstraintSet();
            set.clone(itemRoot);

            Transition transition = new AutoTransition();
            transition.setInterpolator(new OvershootInterpolator());
            TransitionManager.beginDelayedTransition(itemRoot, transition);
            int layoutId = mEditModeEnabled ? R.layout.list_item_editable_modified : R.layout.list_item_editable;
            set.load(mContext, layoutId);
            set.applyTo(itemRoot);
        }
    }
}
