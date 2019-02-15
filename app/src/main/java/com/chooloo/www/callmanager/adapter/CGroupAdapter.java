package com.chooloo.www.callmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.CGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CGroupAdapter extends RecyclerView.Adapter<CGroupAdapter.CGroupHolder> {

    private List<CGroup> mData;

    @NonNull
    @Override
    public CGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cgroup, parent, false);
        CGroupHolder holder = new CGroupHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CGroupHolder holder, int position) {
        CGroup cgroup = mData.get(position);
        holder.title.setText(cgroup.getName());
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    public void setData(List<CGroup> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class CGroupHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image) ImageView image;
        @BindView(R.id.item_title) TextView title;
        @BindView(R.id.item_desc) TextView description;

        public CGroupHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
