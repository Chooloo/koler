package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.listitem.ListItem;
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder;

public class MenuAdapter extends RecyclerView.Adapter<ListItemHolder> {

    private final Menu mMenu;
    private final Context mContext;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public MenuAdapter(Context context, Menu menu) {
        mContext = context;
        mMenu = menu;
        mOnMenuItemClickListener = menuItem -> {
        };
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder(mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        MenuItem menuItem = mMenu.getItem(position);
        ListItem listItem = holder.getListItem();

        listItem.setOnClickListener(view -> mOnMenuItemClickListener.onMenuItemClick(menuItem));
        listItem.setBigText((String) menuItem.getTitle());
        listItem.setImageDrawable(menuItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuItem menuItem);
    }
}
