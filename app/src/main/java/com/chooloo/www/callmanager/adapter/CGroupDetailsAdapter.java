package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CGroupDetailsAdapter extends RecyclerView.Adapter<CGroupDetailsAdapter.ContactHolder> {

    private Context mContext;
    private List<Contact> mData;

    public CGroupDetailsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        ContactHolder holder = new ContactHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = mData.get(position);

        holder.name.setText(contact.getName());
        holder.number.setText(contact.getMainPhoneNumber());
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    public void setData(List<Contact> data) {
        mData = data;
        notifyDataSetChanged();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo) ImageView image;
        @BindView(R.id.item_name_text) TextView name;
        @BindView(R.id.item_number_text) TextView number;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
