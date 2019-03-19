package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.database.entity.CGroupAndItsContacts;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CGroupAdapter extends RecyclerView.Adapter<CGroupAdapter.CGroupHolder> {

    private Context mContext;
    private List<CGroupAndItsContacts> mData;

    private OnChildClickListener mListener;

    public CGroupAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public CGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        CGroupHolder holder = new CGroupHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CGroupHolder holder, int position) {
        CGroupAndItsContacts cgroupAndItsContacts = mData.get(position);

        List<String> names = new ArrayList<>();
        for(Contact contact : cgroupAndItsContacts.getContacts()) {
            names.add(contact.getName());
        }
        String namesStr = Utilities.joinStringsWithSeparator(names, ", ");

        holder.name.setText(cgroupAndItsContacts.getCgroup().getName());
        holder.number.setText(namesStr);

        if (mListener != null) {
            holder.itemView.setOnClickListener(v -> mListener.onClick(v, cgroupAndItsContacts.getCgroup()));
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    public void setData(List<CGroupAndItsContacts> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setListener(OnChildClickListener listener) {
        mListener = listener;
    }

    public interface OnChildClickListener {
        void onClick(View v, CGroup cgroup);
    }

    class CGroupHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo) ImageView image;
        @BindView(R.id.item_name_text) TextView name;
        @BindView(R.id.item_number_text) TextView number;

        public CGroupHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
