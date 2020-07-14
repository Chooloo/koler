package com.chooloo.www.callmanager.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.item_photo_placeholder) ImageView photoPlaceholder;
    public @BindView(R.id.item_photo) ImageView photo;
    public @BindView(R.id.item_big_text) TextView bigText;
    public @BindView(R.id.item_small_text) TextView smallText;
    public @BindView(R.id.item_header) TextView header;

    /**
     * Constructor
     *
     * @param itemView the layout view
     */
    public ListItemHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        photo.setVisibility(View.VISIBLE);
    }
}
