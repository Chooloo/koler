package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.listener.OnItemClickListener;
import com.chooloo.www.callmanager.database.AppDatabase;
import com.chooloo.www.callmanager.database.DataRepository;
import com.chooloo.www.callmanager.database.entity.CGroupAndItsContacts;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CGroupsAdapter extends RecyclerView.Adapter<CGroupsAdapter.CGroupHolder> {

    private Context mContext;
    private List<CGroupAndItsContacts> mData;
    private DataRepository mRepository;
    private OnItemClickListener mOnItemClickListener;

    public CGroupsAdapter(Context context,
                          List<CGroupAndItsContacts> data,
                          OnItemClickListener onItemClickListener) {

        mContext = context;
        mData = data;
        mOnItemClickListener = onItemClickListener;

        mRepository = DataRepository.getInstance(AppDatabase.getDatabase(mContext));
    }

    @NonNull
    @Override
    public CGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        CGroupHolder holder = new CGroupHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CGroupHolder holder, int position) {
        CGroupAndItsContacts cgroupAndItsContacts = mData.get(position);

        List<String> names = new ArrayList<>();

        for (Contact contact : cgroupAndItsContacts.getContacts()) {
            names.add(contact.getName());
        }

        String namesStr = Utilities.joinStringsWithSeparator(names, ", ");

        // Set texts
        holder.name.setText(cgroupAndItsContacts.getCgroup().getName());
        holder.number.setText(namesStr);

        // Set onClick/LongClick listeners
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder, cgroupAndItsContacts.getCgroup()));
        }
        holder.itemView.setOnLongClickListener(v -> {
            //Create a dialog with options regarding the selected list
            new MaterialDialog.Builder(mContext)
                    .title(R.string.dialog_long_click_cgroup_title)
                    .items(R.array.dialog_long_click_cgroup_options)
                    .itemsCallback((dialog, itemView, listPosition, text) -> {
                        switch (listPosition) {
                            case 0:
                                mRepository.deleteCGroup(cgroupAndItsContacts.getCgroup().getListId());
                                break;
                        }
                    })
                    .show();
            notifyItemRemoved(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    /**
     * Sets the data
     *
     * @param data
     */
    public void setData(List<CGroupAndItsContacts> data) {
        mData = data;
        notifyDataSetChanged();
    }

    class CGroupHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo) ImageView image;
        @BindView(R.id.item_big_text) TextView name;
        @BindView(R.id.item_small_text) TextView number;

        public CGroupHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
