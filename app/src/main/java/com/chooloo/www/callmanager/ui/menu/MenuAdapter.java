package com.chooloo.www.callmanager.ui.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.ui.listitem.ListItemPerson;

public class MenuAdapter extends RecyclerView.Adapter<ListItemHolder<ListItemPerson>> {

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
    public ListItemHolder<ListItemPerson> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder<>(new ListItemPerson(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder<ListItemPerson> holder, int position) {
        MenuItem menuItem = mMenu.getItem(position);
        ListItemPerson listItem = holder.getListItem();

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
